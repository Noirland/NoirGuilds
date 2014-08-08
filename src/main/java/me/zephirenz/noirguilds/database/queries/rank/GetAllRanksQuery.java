package me.zephirenz.noirguilds.database.queries.rank;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;

public class GetAllRanksQuery extends GuildsQuery {

    public GetAllRanksQuery() {
        super("SELECT * FROM {PREFIX}_ranks;");
    }

}
