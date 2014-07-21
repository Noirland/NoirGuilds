package me.zephirenz.noirguilds.database.queries.member;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.GuildMember;

public class RemoveMemberQuery extends GuildsQuery {

    public RemoveMemberQuery(GuildMember member) {
        super(1, "DELETE FROM {PREFIX}_members WHERE uuid=?;");
        setValue(1, member.getPlayer());
    }

}
