package net.veteran.veteransmp;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class VConfig
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
        public static final ModConfigSpec.BooleanValue TPA_COMMAND;

        static {
            BUILDER.push("Lightman's Currency");
            OVERRIDE_LC = BUILDER
                    .comment("Where if it should override the default Lightman's Currency Interest logic")
                    .define("lightmans_override", true);

            OFFLINE_LIMIT = BUILDER
                    .comment("The amount of time the user can be offline before it does not receive interest,",
                            "the value should be in minecraft tics.",
                            "Default value is a week",
                            "On team accounts if any of the team members meets the logOn interval interest is applied",
                            "Helpful Notes:",
                            "1s = 20 ticks",
                            "1m = 1200 ticks",
                            "1h = 72000 ticks",
                            "1 day = 1728000 ticks",
                            "1 week = 12096000 ticks",
                            "30 days = 51840000 ticks",
                            "365 days = 630720000 ticks")
                    .defineInRange("offline_limit", 20 * 60 * 60 * 24 * 7, 0L, Integer.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("Command Config");

            TPA_COMMAND = BUILDER
                    .comment("Wether is the command /tpa enabled")
                    .define("tpa_enabled", true);

            SPEC = BUILDER.build();
        }
    }
}
