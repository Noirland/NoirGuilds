package me.zephirenz.noirguilds.database.queries.guild;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.Guild;

public class UpdateGuildQuery extends GuildsQuery {

    private static final String QUERY = "UPDATE {PREFIX}_guilds WHERE id=? SET tag=?, name=?, balance=?, motd=?, kills=?, deaths=?;";

    public UpdateGuildQuery(Guild guild) {
        super(7, QUERY);
        setValue(1, guild.getId());
        setValue(2, guild.getTag());
        setValue(3, guild.getName());
        setValue(4, guild.getBalance());
        setValue(5, guild.getMotd());
        setValue(6, guild.getKills());
        setValue(7, guild.getDeaths());
    }

}
