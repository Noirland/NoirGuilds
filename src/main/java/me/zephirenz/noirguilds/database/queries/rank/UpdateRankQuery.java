package me.zephirenz.noirguilds.database.queries.rank;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.json.simple.JSONValue;

public class UpdateRankQuery extends GuildsQuery {

    private static final String QUERY = "UPDATE {PREFIX}_ranks WHERE id=? SET guild=?, name=?, colour=?, leader=?, `default`=?, perms=?;";

    public UpdateRankQuery(GuildRank rank) {
        super(7, QUERY);
        setValue(1, rank.getId());
        setValue(2, rank.getGuild().getId());
        setValue(3, rank.getName());
        setValue(4, rank.getColour().toString());
        setValue(5, rank.isLeader());
        setValue(6, rank.isDefault());
        setValue(7, JSONValue.toJSONString(rank.getPerms()));
    }

}
