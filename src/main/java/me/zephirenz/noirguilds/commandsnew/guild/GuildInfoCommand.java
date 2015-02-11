package me.zephirenz.noirguilds.commandsnew.guild;

import com.sk89q.intake.Command;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.annotation.Optional;
import me.zephirenz.noirguilds.objects.Guild;
import org.bukkit.command.CommandSender;

public class GuildInfoCommand {

    @Command(
            aliases = "info",
            desc = "Show information about a guild",
            usage = "(guild)"
    )
    public void info(CommandSender sender, @Optional Guild guild) {

    }

    public Dispatcher build(ParametricBuilder builder) {
        return new CommandGraph()
                .builder(builder)
                .commands()
                .registerMethods(this)
                .graph()
                .getDispatcher();
    }
}
