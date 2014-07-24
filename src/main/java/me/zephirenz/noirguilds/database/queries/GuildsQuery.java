package me.zephirenz.noirguilds.database.queries;

import me.zephirenz.noirguilds.database.GuildsDatabase;
import nz.co.noirland.zephcore.database.MySQLDatabase;
import nz.co.noirland.zephcore.database.queries.Query;

public class GuildsQuery extends Query {

    public GuildsQuery(int nargs, String query) {
        super(nargs, query);
    }

    public GuildsQuery(String query) {
        super(query);
    }

    public GuildsQuery(Object[] values, String query) {
        super(values, query);
    }

    protected MySQLDatabase getDB() {
        return GuildsDatabase.inst();
    }
}
