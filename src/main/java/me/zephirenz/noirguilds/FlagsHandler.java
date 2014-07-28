package me.zephirenz.noirguilds;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import me.zephirenz.noirguilds.config.PluginConfig;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class FlagsHandler implements Listener {

    private NoirGuilds plugin;
    private GuildsHandler gHandler;
    private Economy eco;
    private WorldGuardPlugin worldGuard;

    public static final StateFlag FRIENDLY_FIRE_FLAG = new StateFlag("friendly-fire", true);
    public static final StateFlag GUILD_PVP_FLAG = new StateFlag("guild-pvp", false);

    public FlagsHandler() throws Exception {
        plugin = NoirGuilds.inst();
        gHandler = plugin.getGuildsHandler();

        Bukkit.getPluginManager().registerEvents(this, plugin);
        registerFlags();

        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            eco = economyProvider.getProvider();
        }else{
            NoirGuilds.debug().disable("Could not find an economy to use! (Is vault installed?)");
            throw new Exception("No economy found!");
        }
    }

    private void registerFlags() throws ClassNotFoundException {
        try {
            worldGuard = WGBukkit.getPlugin();
        }catch(NoClassDefFoundError e) {
            NoirGuilds.debug().disable("Could not find WorldGuard!");
            throw e;
        }

        WGCustomFlagsPlugin customFlags = (WGCustomFlagsPlugin) Bukkit.getPluginManager().getPlugin("WGCustomFlags");
        if(customFlags == null) {
            NoirGuilds.debug().disable("Could not find WGCustomFlags!");
            throw new ClassNotFoundException();
        }
        customFlags.addCustomFlag(FRIENDLY_FIRE_FLAG);
        customFlags.addCustomFlag(GUILD_PVP_FLAG);
    }

    @EventHandler(ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player to = (Player) event.getEntity();
        Player from = (Player) event.getDamager();

        GuildMember toMember = gHandler.getMember(to.getName());
        GuildMember fromMember = gHandler.getMember(from.getName());
        if(toMember == null || fromMember == null) return;

        if(toMember.getGuild() != fromMember.getGuild()) {
            return;
        }

        ApplicableRegionSet toRegion = worldGuard.getRegionManager(to.getWorld()).getApplicableRegions(to.getLocation());
        ApplicableRegionSet fromRegion = worldGuard.getRegionManager(from.getWorld()).getApplicableRegions(from.getLocation());
        if(toRegion.allows(FRIENDLY_FIRE_FLAG) || fromRegion.allows(FRIENDLY_FIRE_FLAG)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if(killer == null) return;

        GuildMember pMember = gHandler.getMember(player.getName());
        GuildMember kMember = gHandler.getMember(killer.getName());

        if(kMember == null || pMember == null) return; // Killer/Killed has no guild

        Guild pGuild = pMember.getGuild();
        Guild kGuild = kMember.getGuild();

        if(pGuild == kGuild) return;

        ApplicableRegionSet pRegion = worldGuard.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
        ApplicableRegionSet kRegion = worldGuard.getRegionManager(killer.getWorld()).getApplicableRegions(killer.getLocation());
        if(!pRegion.allows(GUILD_PVP_FLAG) || !kRegion.allows(GUILD_PVP_FLAG)) {
            return;
        }

        // Kill count
        pMember.incrDeaths();
        pMember.getGuild().incrDeaths();
        pMember.updateDB();
        pMember.getGuild().updateDB();

        kMember.incrKills();
        kMember.getGuild().incrKills();
        kMember.updateDB();
        kMember.getGuild().updateDB();

        // Money for Guild

        double killMoney = PluginConfig.inst().getKillMoney();

        if(pGuild.getBalance() >= killMoney) {
            pGuild.setBalance(pGuild.getBalance() - killMoney);
            plugin.sendMessage(player, String.format(Strings.FLAG_PVP_GUILD_TAKEN, eco.format(killMoney)));
        } else if(eco.has(player, killMoney)) {
            EconomyResponse result = eco.withdrawPlayer(player, killMoney);
            if(!result.transactionSuccess()) return;
            plugin.sendMessage(player, String.format(Strings.FLAG_PVP_PLAYER_TAKEN, eco.format(killMoney)));
        }  else return;

        kGuild.setBalance(kGuild.getBalance() + killMoney);
        plugin.sendMessage(player, String.format(Strings.FLAG_PVP_WON, eco.format(killMoney)));
    }


}
