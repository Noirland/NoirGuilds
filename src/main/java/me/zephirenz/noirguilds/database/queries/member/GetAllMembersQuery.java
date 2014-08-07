package me.zephirenz.noirguilds.database.queries.member;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;

public class GetAllMembersQuery extends GuildsQuery {

    public GetAllMembersQuery() {
        super("SELECT * FROM {PREFIX}_members;");
    }

}
