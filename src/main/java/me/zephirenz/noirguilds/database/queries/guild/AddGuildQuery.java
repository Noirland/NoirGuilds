package me.zephirenz.noirguilds.database.queries.guild;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.Guild;
import org.json.simple.JSONValue;

public class AddGuildQuery extends GuildsQuery {

    private static final String QUERY = "INSERT INTO {PREFIX}_guilds (id, tag, name, balance, motd, kills, deaths) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";


    public AddGuildQuery(Guild guild) {
        super(7, QUERY);
        setValue(1, Integer.toHexString(guild.getId()));
        setValue(2, guild.getTag());
        setValue(3, guild.getName());
        setValue(4, guild.getBalance());
        setValue(5, JSONValue.toJSONString(guild.getMotd()));
        setValue(6, guild.getKills());
        setValue(7, guild.getDeaths());
    }
}
