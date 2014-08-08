package me.zephirenz.noirguilds.database.queries.guild;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.Guild;

public class RemoveGuildQuery extends GuildsQuery {

    public RemoveGuildQuery(Guild guild) {
        super(1, "DELETE FROM {PREFIX}_guilds WHERE id=?;");
        setValue(1, guild.getId());
    }

}
