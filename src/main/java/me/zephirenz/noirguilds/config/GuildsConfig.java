package me.zephirenz.noirguilds.config;

import me.zephirenz.noirguilds.NoirGuilds;
import nz.co.noirland.zephcore.Config;
import nz.co.noirland.zephcore.Debug;
import org.bukkit.plugin.Plugin;

public class GuildsConfig extends Config {

    private static GuildsConfig instance;

    private GuildsConfig() {
        super("config.yml");
    }

    @Override
    protected Plugin getPlugin() {
        return NoirGuilds.inst();
    }

    @Override
    protected Debug getDebug() {
        return NoirGuilds.debug();
    }

    public static GuildsConfig inst() {
        if(instance == null) {
            instance = new GuildsConfig();
        }

        return instance;
    }

    public String getDBPrefix()   { return config.getString("noirguilds.db.prefix", "guild"); }
    public String getDBName()     { return config.getString("noirguilds.db.name"); }
    public String getDBUser()     { return config.getString("noirguilds.db.username"); }
    public String getDBPassword() { return config.getString("noirguilds.db.password"); }
    public int    getDBPort()     { return config.getInt   ("noirguilds.db.port", 3306); }
    public String getDBHost()     { return config.getString("noirguilds.db.host", "localhost"); }

    public double getKillMoney() { return config.getDouble("noirguilds.kill-money", 10); }

    public int    getInitialMemberLimit() { return config.getInt   ("noirguilds.initial-member-limit", 0); }
    public int    getInitialUpgradeCost() { return config.getInt   ("noirguilds.upgrade.initial-cost", 10000); }
    public double getUpgradeMultiplier()  { return config.getDouble("noirguilds.upgrade.multiplier", 1.5); }

}
