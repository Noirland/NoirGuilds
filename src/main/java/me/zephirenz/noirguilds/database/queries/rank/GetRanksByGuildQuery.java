package me.zephirenz.noirguilds.database.queries.rank;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.Guild;

public class GetRanksByGuildQuery extends GuildsQuery {

    public GetRanksByGuildQuery(Guild guild) {
        super(1, "SELECT * FROM {PREFIX}_ranks WHERE guild=?;");
        setValue(1, guild.getId());
    }

}
