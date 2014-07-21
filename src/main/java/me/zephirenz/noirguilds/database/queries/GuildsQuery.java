package me.zephirenz.noirguilds.database.queries;

import me.zephirenz.noirguilds.database.GuildsDatabase;
import nz.co.noirland.zephcore.database.queries.Query;

public class GuildsQuery extends Query {

    public GuildsQuery(int nargs, String query) {
        this(new Object[nargs-1], query);
    }

    public GuildsQuery(String query) {
        this(0, query);
    }

    public GuildsQuery(Object[] values, String query) {
        super(GuildsDatabase.inst(), values, query);
    }

}
