package me.zephirenz.noirguilds.commandsnew;

import com.sk89q.intake.parametric.ParameterException;
import com.sk89q.intake.parametric.argument.ArgumentStack;
import com.sk89q.intake.parametric.binding.BindingBehavior;
import com.sk89q.intake.parametric.binding.BindingHelper;
import com.sk89q.intake.parametric.binding.BindingMatch;
import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;

public class GuildBindings extends BindingHelper {

    private static final GuildsHandler gHandler = NoirGuilds.inst().getGuildsHandler();

    @BindingMatch(type = Guild.class,
            behavior = BindingBehavior.CONSUMES,
            consumedCount = 1
    )
    public static Guild getGuild(ArgumentStack context) throws ParameterException {
        String name = context.next();
        Guild guild = gHandler.getGuildByName(name);
        if(guild == null) {
            guild = gHandler.getGuildByTag(name);
            if(guild == null) {
                throw new ParameterException("Could not find a guild named " + name);
            }
        }
        return guild;
    }

    @BindingMatch(type = GuildMember.class,
            behavior = BindingBehavior.CONSUMES,
            consumedCount = 1
    )
    public static GuildMember getMember(ArgumentStack context) throws ParameterException {
        String name = context.next();
        GuildMember member = gHandler.getMember(name);
        if(member == null) {
            throw new ParameterException(name + " is not a guild member");
        }

        return member;
    }

}
