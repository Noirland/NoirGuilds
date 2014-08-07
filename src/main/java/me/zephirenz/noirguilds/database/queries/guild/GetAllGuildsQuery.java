package me.zephirenz.noirguilds.database.queries.guild;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;

public class GetAllGuildsQuery extends GuildsQuery {


    public GetAllGuildsQuery() {
        super("SELECT * FROM {PREFIX}_guilds;");
    }
}
