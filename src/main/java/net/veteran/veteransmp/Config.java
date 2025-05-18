package net.veteran.veteransmp;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config
{
//    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
//
//    private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
//            .comment("Whether to log the dirt block on common setup")
//            .define("logDirtBlock", true);
//
//    private static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
//            .comment("A magic number")
//            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
//
//    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
//            .comment("What you want the introduction message to be for the magic number")
//            .define("magicNumberIntroduction", "The magic number is... ");

    public static class Server {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.BooleanValue OVERRIDE_LC;
        public static final ModConfigSpec.LongValue OFFLINE_LIMIT;

        static {
            BUILDER.push("Lightman's Currency");
            OVERRIDE_LC = BUILDER
                    .comment("Where if it should override the default Lightman's Currency Interest logic")
                    .define("lightmans_override", true);

            OFFLINE_LIMIT = BUILDER
                    .comment("The amount of time the user can be offline before it does not receive interest,",
                            "the value should be in milliseconds. Where you can use 1000 for a sec and 1000 * 60 for a min",
                            "Default value is a week",
                            "On team accounts if any of the team members meets the logOn interval interest is applied")
                    .defineInRange("offline_limit", 1000*60*60*24*7, 0, ((long) Integer.MAX_VALUE));
            BUILDER.pop();

            SPEC = BUILDER.build();
        }
    }
}
