package me.zephirenz.noirguilds.config;

import java.io.InputStream;

public class GuildConfig extends Config {

    public GuildConfig(String guild) {
        super("guilds", guild.toLowerCase() + ".yml");
    }

    @Override
    protected InputStream getResource() {
        return plugin.getResource("guild.yml");
    }


}
