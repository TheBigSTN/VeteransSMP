package net.veteran.veteransmp.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.veteran.veteransmp.commands.daycount.DayCountCommand;
import net.veteran.veteransmp.commands.tpa.TpaCommand;

public class CommandLoader {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        TpaCommand.register(dispatcher);
        DayCountCommand.register(dispatcher);
    }
}
