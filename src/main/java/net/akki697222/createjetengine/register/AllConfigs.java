package net.akki697222.createjetengine.register;

import net.akki697222.createjetengine.CreateJetEngine;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = CreateJetEngine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllConfigs {
    public static final ForgeConfigSpec CONFIG;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.DoubleValue AFTERBURNERED_ENGINE_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue FUEL_TANK_AMOUNT;
    static {
        BUILDER.push("General");

        AFTERBURNERED_ENGINE_MULTIPLIER = BUILDER
                .comment("Afterburnered JET Engine generate speed multiplier")
                .defineInRange("afterburnered_engine_multiplier", 1.0, 0.0, Double.MAX_VALUE);
        FUEL_TANK_AMOUNT = BUILDER
                .comment("Combustion Chamber Fuel Tank Max Amount")
                .defineInRange("fuel_tank_amount", 1000, 1000, Integer.MAX_VALUE);

        BUILDER.pop();

        CONFIG = BUILDER.build();
    }
}
