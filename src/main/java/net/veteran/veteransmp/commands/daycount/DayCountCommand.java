package net.veteran.veteransmp.commands.daycount;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class DayCountCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("day_count")
                .executes(DayCountCommand::dayCount));
    }

    private static int dayCount(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();

        long time = level.getDayTime();
        long day = time / 24000;

        source.sendSuccess(() -> Component.literal("Current day: " + day), false);
        return 1;
    }
}
