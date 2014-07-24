package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.commands.*;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.listeners.PlayerChatListener;
import me.zephirenz.noirguilds.listeners.PlayerJoinListener;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.AsyncDatabaseUpdateTask;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class NoirGuilds extends JavaPlugin {

    private static NoirGuilds inst;
    private GuildsHandler guildsHandler;
    private GuildBankManager bankManager;
    private static Debug debug;

    @Override
    public void onEnable() {
        inst = this;
        debug = new Debug(this);
        GuildsDatabase.inst().checkSchema();
        guildsHandler = new GuildsHandler();
        bankManager = new GuildBankManager();
        try {
            new FlagsHandler();
        } catch(Exception e) {
            return;
        }
        addCommands();
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(bankManager, this);
    }

    @Override
    public void onDisable() {
        AsyncDatabaseUpdateTask.inst().stop(); // Finishes any remaining queries
        GuildsDatabase.inst().close();
    }

    public static NoirGuilds inst() {
        return inst;
    }

    public static Debug debug() { return debug; }

    public GuildsHandler getGuildsHandler() {
        return guildsHandler;
    }

    public GuildBankManager getBankManager() {
        return bankManager;
    }

    public void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(Strings.MESSAGE_PREFIX + msg);
    }

    public void sendGlobalMessage(String msg) {
        getServer().broadcastMessage(Strings.MESSAGE_PREFIX + msg);
    }

    private void addCommands() {
        getCommand("guild").setExecutor(new GuildCommand());
        getCommand("guilds").setExecutor(new GuildsCommand());
        getCommand("ga").setExecutor(new GuildAdminChatCommand());
        getCommand("g").setExecutor(new GuildChatCommand());
//        getCommand("gtp").setExecutor(new GuildTpCommand());
//        getCommand("gtphere").setExecutor(new GuildTpHereCommand());
        getCommand("grank").setExecutor(new GuildRankCommand());
        getCommand("hq").setExecutor(new HQCommand());
    }
}
