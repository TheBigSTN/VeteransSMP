package net.veteran.veteransmp;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class VeteranSmpConfig {


    public static class Server {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.BooleanValue OVERRIDE_LC;
        public static final ModConfigSpec.LongValue OFFLINE_LIMIT;
        public static final ModConfigSpec.BooleanValue TPA_COMMAND;
        public static final ModConfigSpec.BooleanValue DAY_COUNT_COMMAND;

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
                    .comment("Whether if the command /tpa is enabled")
                    .define("tpa_enabled", true);

            DAY_COUNT_COMMAND = BUILDER
                    .comment("Whether if the command /daycount is enabled")
                    .define("day_count_enabled", true);

            SPEC = BUILDER.build();
        }
    }
}
