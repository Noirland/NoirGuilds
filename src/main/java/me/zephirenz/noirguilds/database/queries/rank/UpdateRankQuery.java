package me.zephirenz.noirguilds.database.queries.rank;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.json.simple.JSONValue;

public class UpdateRankQuery extends GuildsQuery {

    private static final String QUERY = "UPDATE {PREFIX}_ranks SET guild=?, name=?, colour=?, leader=?, `default`=?, perms=? WHERE id=?;";

    public UpdateRankQuery(GuildRank rank) {
        super(7, QUERY);
        setValue(1, rank.getGuild().getId());
        setValue(2, rank.getName());
        setValue(3, rank.getColour().name());
        setValue(4, rank.isLeader());
        setValue(5, rank.isDefault());
        setValue(6, JSONValue.toJSONString(rank.getPerms()));
        setValue(7, rank.getId());
    }

}
