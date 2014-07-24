package me.zephirenz.noirguilds.database.schema;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import nz.co.noirland.zephcore.database.Schema;
import nz.co.noirland.zephcore.database.queries.Query;

import java.sql.SQLException;

public class Schema1 implements Schema {

    public void run() {
        try {
            createGuildsTable();
            createRanksTable();
            createMembersTable();
            createSchemaTable();
        } catch (SQLException e) {
            NoirGuilds.debug().disable("Unable to setup database!", e);
        }
    }

    private void createSchemaTable() throws SQLException {
        new GuildsQuery("CREATE TABLE `{PREFIX}_schema` (`version` TINYINT UNSIGNED);").execute();
        new GuildsQuery("INSERT INTO `{PREFIX}_schema` VALUES(1);").execute();
    }

    private void createGuildsTable() throws SQLException {
        Query query = new GuildsQuery("CREATE TABLE {PREFIX}_guilds (" +
                "`id` SMALLINT UNSIGNED, " +
                "`tag` VARCHAR(4), " +
                "`name` VARCHAR(255), " +
                "`balance` DOUBLE UNSIGNED, " +
                "`motd` TEXT, " +
                "`hq` TEXT, " +
                "`kills` MEDIUMINT UNSIGNED, " +
                "`deaths` MEDIUMINT UNSIGNED, " +
                " PRIMARY KEY (`id`)" +
                ");");
        query.execute();
    }

    private void createRanksTable() throws SQLException {
        Query query = new GuildsQuery("CREATE TABLE {PREFIX}_ranks (" +
                "`id` SMALLINT UNSIGNED, " +
                "`guild` SMALLINT UNSIGNED, " +
                "`name` VARCHAR(255), " +
                "`colour` VARCHAR(16), " +
                "`leader` BOOLEAN DEFAULT FALSE, " +
                "`default` BOOLEAN DEFAULT FALSE, " +
                "`perms` TEXT, " +
                " PRIMARY KEY (`id`) " +
                ");");
        query.execute();
    }

    private void createMembersTable() throws SQLException {
        Query query = new GuildsQuery("CREATE TABLE {PREFIX}_members (" +
                "`uuid` VARCHAR(36) NOT NULL, " +
                "`rank` SMALLINT UNSIGNED, " +
                "`kills` MEDIUMINT UNSIGNED, " +
                "`deaths` MEDIUMINT UNSIGNED, " +
                " PRIMARY KEY (`uuid`) " +
                ");");
        query.execute();
    }
}
