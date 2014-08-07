package me.zephirenz.noirguilds.config;

public class PluginConfig extends Config {

    private static PluginConfig instance;

    private PluginConfig() {
        super("config.yml");
    }

    public static PluginConfig inst() {
        if(instance == null) {
            instance = new PluginConfig();
        }

        return instance;
    }

    // MYSQL

    public String getDBPrefix()   { return config.getString("noirguilds.db.prefix", "guild"); }
    public String getDBName()     { return config.getString("noirguilds.db.name"); }
    public String getDBUser()     { return config.getString("noirguilds.db.username"); }
    public String getDBPassword() { return config.getString("noirguilds.db.password"); }
    public int    getDBPort()     { return config.getInt   ("noirguilds.db.port", 3306); }
    public String getDBHost()     { return config.getString("noirguilds.db.host", "localhost"); }

    public double getKillMoney()   { return config.getDouble("noirguilds.kill-money", 10); }
    public int    getMemberLimit() { return config.getInt   ("noirguilds.member-limit", 0); }
}
