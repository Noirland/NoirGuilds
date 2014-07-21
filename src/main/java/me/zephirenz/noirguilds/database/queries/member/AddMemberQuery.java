package me.zephirenz.noirguilds.database.queries.member;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.GuildMember;

public class AddMemberQuery extends GuildsQuery {

    private static final String QUERY = "INSERT INTO {PREFIX}_members (uuid, rank, kills, deaths) " +
            "VALUES (?, ?, ?, ?);";


    public AddMemberQuery(GuildMember member) {
        super(4, QUERY);
        setValue(1, member.getPlayer());
        setValue(2, member.getRank().getId());
        setValue(3, member.getKills());
        setValue(4, member.getDeaths());
    }
}
