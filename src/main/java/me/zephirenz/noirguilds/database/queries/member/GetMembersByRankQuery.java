package me.zephirenz.noirguilds.database.queries.member;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.GuildRank;

public class GetMembersByRankQuery extends GuildsQuery {

    public GetMembersByRankQuery(GuildRank rank) {
        super(1, "SELECT * FROM {PREFIX}_members WHERE guild=?;");
        setValue(1, rank.getId());
    }

}
