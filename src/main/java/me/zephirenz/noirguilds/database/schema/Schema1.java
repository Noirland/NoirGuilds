package me.zephirenz.noirguilds.database.schema;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import nz.co.noirland.zephcore.database.Schema;
import nz.co.noirland.zephcore.database.queries.Query;

import java.sql.SQLException;

public class Schema1 implements Schema {

    private GuildsDatabase db;

    public void run() {
        db = GuildsDatabase.inst();
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
        db.getStatement(new Query("CREATE TABLE `{PREFIX}_schema` (`version` TINYINT UNSIGNED);")).execute();
        db.getStatement(new Query("INSERT INTO `{PREFIX}`_schema` VALUES(1);")).execute();
    }

    private void createGuildsTable() throws SQLException {
        Query query = new Query("CREATE TABLE {PREFIX}_guilds (" +
                "`id` TINYINT NOT NULL AUTO_INCREMENT, " +
                "`tag` VARCHAR(4), " +
                "`name` VARCHAR(255), " +
                "`leader_rank` TINYINT UNSIGNED, " +
                "`default_rank` TINYINT UNSIGNED, " +
                "`balance` DOUBLE UNSIGNED, " +
                "`motd` TEXT, " +
                "`kills` INT UNSIGNED, " +
                "`deaths` INT UNSIGNED, " +
                " PRIMARY KEY (`id`)" +
                ");");
        db.getStatement(query).execute();
    }

    private void createRanksTable() throws SQLException {
        Query query = new Query("CREATE TABLE {PREFIX}_ranks (" +
                "`id` TINYINT NOT NULL AUTO_INCREMENT, " +
                "`guild` TINYINT" +
                "`colour` VARCHAR(16), " +
                "`invite` BOOLEAN, DEFAULT 0" +
                "`kick` BOOLEAN DEFAULT 0, " +
                "`adminchat` BOOLEAN DEFAULT 0, " +
                "`tp` BOOLEAN DEFAULT 0, " +
                "`tphere` BOOLEAN DEFAULT 0, " +
                "`hq` BOOLEAN DEFAULT 0, " +
                "`withdraw` BOOLEAN DEFAULT 0, " +
                "`pay` BOOLEAN DEFAULT 0, " +
                " PRIMARY KEY (`id`), " +
                " FOREIGN KEY (`guild`) REFERENCES `{PREFIX}_guilds`(`id`)" +
                ");");
        db.getStatement(query).execute();
    }

    private void createMembersTable() throws SQLException {
        Query query = new Query("CREATE TABLE {PREFIX}_members (" +
                "`uuid` VARCHAR(36) NOT NULL, " +
                "`rank` TINYINT UNSIGNED, " +
                "`kills` INT UNSIGNED, " +
                "`deaths` INT UNSIGNED, " +
                " PRIMARY KEY (`uuid`), " +
                " FOREIGN KEY (`rank`) REFERENCES `{PREFIX}_ranks`(`id`)" +
                ");");
        db.getStatement(query).execute();
    }
}
