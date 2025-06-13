package net.veteran.veteransmp.commands.tpa;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.veteran.veteransmp.VeteranSmpConfig;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TpaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("tpa")
                    .requires(CommandSourceStack::isPlayer)
                    .then(Commands.literal("request")
                            .then(Commands.argument("target", EntityArgument.player())
                                    .executes(TpaCommand::requestTpa)))
                    .then(Commands.literal("accept")
                            .then(Commands.argument("target", EntityArgument.player())
                                    .executes(TpaCommand::acceptTpa)))
                    .then(Commands.literal("deny")
                            .then(Commands.argument("target", EntityArgument.player())
                                    .executes(TpaCommand::denyTpa)))
                    .then(Commands.literal("list")
                            .executes(TpaCommand::listPendingRequests))
        );
    }

    private static final TpaManager tpaManager = new TpaManager();

    private static int requestTpa(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer host = context.getSource().getPlayerOrException();
        ServerPlayer guest = EntityArgument.getPlayer(context, "target");

        if (!VeteranSmpConfig.Server.TPA_COMMAND.get()) {
            host.sendSystemMessage(Component.literal("The tpa command is disabled")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        if (guest.equals(host)) {
            host.sendSystemMessage(Component.literal("You can't teleport to yourself")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        String hostName = host.getName().getString();
        String guestName = guest.getName().getString();

        tpaManager.addRequest(hostName, guestName, () ->
            host.sendSystemMessage(Component.literal("Your teleport request to " + guestName + " has expired.")
                    .withStyle(ChatFormatting.RED))
        );


        guest.sendSystemMessage(Component.literal(host.getName().getString())
                .withStyle(ChatFormatting.DARK_GREEN)
                .append(Component.literal(" wants to teleport to you!\n")
                        .withStyle(ChatFormatting.GOLD))
                .append(Component.literal("Do you want to ")
                        .withStyle(ChatFormatting.GOLD))
                .append(Component.literal("[Accept]")
                        .withStyle(Style.EMPTY
                                .withColor(ChatFormatting.GREEN)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + host.getName().getString()))))
                .append(Component.literal(" or ")
                        .withStyle(ChatFormatting.GOLD))
                .append(Component.literal("[Deny]")
                        .withStyle(Style.EMPTY
                                .withColor(ChatFormatting.RED)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa deny " + host.getName().getString()))))
        );
        host.sendSystemMessage(Component.literal("Request sent to " + guest.getName().getString()));
        return 1;
    }

    public static int acceptTpa(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer host = context.getSource().getPlayerOrException();
        ServerPlayer guest = EntityArgument.getPlayer(context, "target");


        TpaRequest request = tpaManager.getRequest(guest.getName().getString(), host.getName().getString());

        if (request == null) {
            host.sendSystemMessage(Component.literal("No teleport request from that player!")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        if (request.isAccepted()) {
            host.sendSystemMessage(Component.literal("This teleport request has already been accepted!")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        guest.displayClientMessage(Component.literal("Teleport request accepted!")
                .withStyle(ChatFormatting.RED), true);
        tpaManager.acceptRequest(guest.getName().getString(), host.getName().getString());

        try (ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor()) {
            AtomicInteger counter = new AtomicInteger(6);


            scheduler.scheduleAtFixedRate(
                    () -> {
                        int timeLeft = counter.getAndDecrement();

                        if (timeLeft > 0) {
                            guest.displayClientMessage(
                                    Component.literal("Teleporting to " + host.getName().getString() + " in " + timeLeft + " seconds...")
                                            .withStyle(ChatFormatting.YELLOW),
                                    true
                            );

                            host.displayClientMessage(
                                    Component.literal(guest.getName().getString() + " will arrive in " + timeLeft + " seconds...")
                                            .withStyle(ChatFormatting.YELLOW),
                                    true
                            );
                        } else {
                            guest.teleportTo(
                                    host.serverLevel(),
                                    host.getX(),
                                    host.getY(),
                                    host.getZ(),
                                    host.getYRot(),
                                    host.getXRot()
                            );

                            tpaManager.removeRequest(guest.getName().getString(), host.getName().getString());

                            guest.displayClientMessage(Component.literal("You have been teleported!")
                                    .withStyle(ChatFormatting.GREEN), true);

                            host.displayClientMessage(Component.literal(guest.getName().getString() + " has been teleported to you!")
                                    .withStyle(ChatFormatting.GREEN), true);

                            scheduler.shutdown();
                        }
                    }, 0, 1, TimeUnit.SECONDS);
        }

        return 1;
    }

    public static int denyTpa(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer host = context.getSource().getPlayerOrException();
        ServerPlayer guest = EntityArgument.getPlayer(context, "target");

        TpaRequest request = tpaManager.getRequest(guest.getName().getString(), host.getName().getString());

        if (request == null) {
            host.sendSystemMessage(Component.literal("No teleport request from that player!"));
            return 0;
        }

        request.cancelTask();
        tpaManager.removeRequest(guest.getName().getString(), host.getName().getString());

        host.sendSystemMessage(Component.literal("You denied the teleport request from " + guest.getName().getString())
                .withStyle(ChatFormatting.GREEN));

        guest.sendSystemMessage(Component.literal(host.getName().getString() + " has denied your teleport request!")
                .withStyle(ChatFormatting.RED));
        return 1;
    }

    public static int listPendingRequests(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer host = context.getSource().getPlayerOrException();

        ArrayList<TpaRequest> requests = tpaManager.getRequestsToPlayer(host.getName().getString());

        if (requests.isEmpty()) {
            host.sendSystemMessage(Component.literal("No pending teleport requests!")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        host.sendSystemMessage(Component.literal("» Pending TPA requests «")
                .withStyle(ChatFormatting.GOLD));

        requests.forEach(item -> {
            String name = item.getFrom();

            MutableComponent acceptButton = Component.literal("[Accept]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + name)));

            MutableComponent rejectButton = Component.literal("[Deny]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.RED)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa deny " + name)));

            host.sendSystemMessage(
                    Component.literal("\n» ")
                            .withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(name)
                                    .withStyle(ChatFormatting.DARK_GREEN))
                            .append(Component.literal(" « ")
                                    .withStyle(ChatFormatting.GOLD))
                            .append(acceptButton)
                            .append(Component.literal(" or "))
                            .append(rejectButton));
        });

        return 1;
    }
}