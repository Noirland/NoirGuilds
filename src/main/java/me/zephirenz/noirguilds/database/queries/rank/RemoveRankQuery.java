package me.zephirenz.noirguilds.database.queries.rank;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.GuildRank;

public class RemoveRankQuery extends GuildsQuery {

    public RemoveRankQuery(GuildRank rank) {
        super(1, "DELETE FROM {PREFIX}_ranks WHERE id=?;");
        setValue(1, rank.getId());
    }

}
