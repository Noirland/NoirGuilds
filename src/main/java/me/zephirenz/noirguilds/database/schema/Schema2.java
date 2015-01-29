package me.zephirenz.noirguilds.database.schema;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.GuildsConfig;
import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import nz.co.noirland.zephcore.database.Schema;

import java.sql.SQLException;

public class Schema2 implements Schema {

    @Override
    public void run() {
        try {
            addMemberLimit();
        } catch(SQLException e) {
            NoirGuilds.debug().disable("Unable to upgrade database to schema 2!", e);
        }
    }


    private void addMemberLimit() throws SQLException {
        new GuildsQuery("ALTER TABLE `{PREFIX}_guilds` ADD COLUMN `limit` INT UNSIGNED");
        int limit = GuildsConfig.inst().getInitialMemberLimit();
        new GuildsQuery("UPDATE `{PREFIX}_guilds` SET `limit` = " + limit);
    }

    private void updateSchema() throws SQLException {
        new GuildsQuery("UPDATE `{PREFIX}_schema` SET `version` = 2").execute();
    }
}
