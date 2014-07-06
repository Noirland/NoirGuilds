package me.zephirenz.noirguilds.config;

public class PluginConfig extends Config {

    private static PluginConfig instance;

    private PluginConfig() {
        super("config.yml");
    }

    public static PluginConfig getInstance() {
        if(instance == null) {
            instance = new PluginConfig();
        }

        return instance;
    }

    // MYSQL

    public String getDatabaseType()      { return config.getString("noirguilds.database.type", "flatfile"); }
    public String getMySQLTablePrefix()  { return config.getString("noirguilds.database.mysql.prefix", "guild_"); }
    public String getMySQLDatabaseName() { return config.getString("noirguilds.database.mysql.database"); }
    public String getMySQLUserName()     { return config.getString("noirguilds.database.mysql.username"); }
    public String getMySQLPassword()     { return config.getString("noirguilds.database.mysql.password"); }
    public int    getMySQLServerPort()   { return config.getInt   ("noirguilds.database.mysql.port", 3306); }
    public String getMySQLServerAddr()   { return config.getString("noirguilds.database.mysql.hostname", "localhost"); }

    public double getKillMoney()         { return config.getDouble("noirguilds.kill-money", 10); }
    public int    getMemberLimit()       { return config.getInt   ("noirguilds.member-limit", 0); }
}
