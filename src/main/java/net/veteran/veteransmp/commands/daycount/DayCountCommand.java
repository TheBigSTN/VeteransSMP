package net.veteran.veteransmp.commands.daycount;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.veteran.veteransmp.VeteranSmpConfig;

public class DayCountCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("day_count")
                .executes(DayCountCommand::dayCount));
    }

    private static int dayCount(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();
        if (!VeteranSmpConfig.Server.DAY_COUNT_COMMAND.get()) {
            source.sendSuccess(() -> Component.literal("This command is disabled")
                    .withStyle(ChatFormatting.RED), false);
            return 0;
        }

        long time = level.getDayTime();
        long day = time / 24000;

        source.sendSuccess(() -> Component.literal("Current day: " + day), false);
        return 1;
    }
}
