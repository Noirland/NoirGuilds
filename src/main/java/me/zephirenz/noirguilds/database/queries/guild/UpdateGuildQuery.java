package me.zephirenz.noirguilds.database.queries.guild;

import me.zephirenz.noirguilds.database.queries.GuildsQuery;
import me.zephirenz.noirguilds.objects.Guild;
import nz.co.noirland.zephcore.Util;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UpdateGuildQuery extends GuildsQuery {

    private static final String QUERY = "UPDATE {PREFIX}_guilds SET tag=?, name=?, balance=?, motd=?, hq=?, kills=?, deaths=? WHERE id=?;";

    public UpdateGuildQuery(Guild guild) {
        super(8, QUERY);
        setValue(1, guild.getTag());
        setValue(2, guild.getName());
        setValue(3, guild.getBalance());

        if(guild.getMotd() == null) {
            setValue(4, null);
        }else {
            setValue(4, JSONValue.toJSONString(guild.getMotd()));
        }

        if (guild.getHQ() == null) {
            setValue(5, null);
        } else {
            JSONObject hq = new JSONObject();
            hq.putAll(Util.toMap(guild.getHQ()));
            setValue(5, hq.toString());
        }

        setValue(6, guild.getKills());
        setValue(7, guild.getDeaths());
        setValue(8, guild.getId());
    }

}
