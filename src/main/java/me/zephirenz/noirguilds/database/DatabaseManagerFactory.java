package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.PluginConfig;

public class DatabaseManagerFactory {

    // Structure for database managers and factories taken from mcMMO - Thanks!

    private static Class<? extends DatabaseManager> dbManager = null;

    public static DatabaseManager getDatabaseManager() {
        if(dbManager != null) {
            try {
                return createCustomDatabaseManager(dbManager);
            } catch (Exception e) {
                NoirGuilds.debug().debug("Could not create database manager", e);
            } catch (Throwable e) {
                NoirGuilds.debug().debug("Failed to create database manager", e);
            }
            NoirGuilds.debug().debug("Falling back on " + (PluginConfig.getInstance().getDatabaseType().equalsIgnoreCase("mysql") ? "MySQL" : "Flatfile") + " database");
        }
        return PluginConfig.getInstance().getDatabaseType().equalsIgnoreCase("mysql") ? new MySQLDatabaseManager() : new FlatfileDatabaseManager();
    }


    /**
     * Sets the custom DatabaseManager class for McMMO to use. This should be
     * called prior to mcMMO enabling.
     * <p>
     * The provided class must have an empty constructor, which is the one
     * that will be used.
     * <p>
     * This method is intended for API use, but it should not be considered
     * stable. This method is subject to change and/or removal in future
     * versions.
     *
     * @param clazz the DatabaseManager class to use
     * @throws IllegalArgumentException if the provided class does not have
     *             an empty constructor
     */
    public static void setCustomDatabaseManager(Class<? extends DatabaseManager> clazz) {
        try {
            clazz.getConstructor((Class<?>) null);
            dbManager = clazz;
        }catch(Throwable e) {
            throw new IllegalArgumentException("Database Manager must not have an empty constructor", e);
        }
    }

    public static Class<? extends DatabaseManager> getCustomDatabaseManagerClass() {
        return dbManager;
    }

    public static MySQLDatabaseManager createMySQLDatabaseManager() {
        return new MySQLDatabaseManager();
    }

    public static FlatfileDatabaseManager createFlatfileDatabaseManager() {
        return new FlatfileDatabaseManager();
    }

    public static DatabaseManager createCustomDatabaseManager(Class<? extends DatabaseManager> clazz) throws Throwable {
        return dbManager.getConstructor((Class<?>) null).newInstance((Object[]) null);
    }

}
