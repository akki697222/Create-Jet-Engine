package net.akki697222.createjetengine.register;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.akki697222.createjetengine.content.kinetics.jetengines.components.AirIntakeBlock;
import net.akki697222.createjetengine.content.kinetics.jetengines.components.CompressorBlock;
import net.akki697222.createjetengine.content.kinetics.jetengines.components.GasTurbineBlock;
import net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberBlock;
import net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.ExhaustNozzleBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static net.akki697222.createjetengine.CreateJetEngine.REGISTRATE;
public class AllBlocks {
    public static final BlockEntry<AirIntakeBlock> AIR_INTAKE = REGISTRATE.block("air_intake", AirIntakeBlock::new)
        .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.METAL))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setImpact(1.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<ExhaustNozzleBlock> EXHAUST_NOZZLE = REGISTRATE.block("exhaust_nozzle", ExhaustNozzleBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.METAL))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setImpact(1.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<CompressorBlock> COMPRESSOR = REGISTRATE.block("compressor", CompressorBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.NETHERITE_BLOCK))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setImpact(0.5))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<CombustionChamberBlock> COMBUSTION_CHAMBER = REGISTRATE.block("combustion_chamber", CombustionChamberBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.NETHERITE_BLOCK))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setImpact(2))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<GasTurbineBlock> GAS_TURBINE = REGISTRATE.block("gas_turbine", GasTurbineBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.NETHERITE_BLOCK))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(BlockStressDefaults.setCapacity(16384/256f))
            .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setImpact(2))
            .item()
            .transform(customItemModel())
            .register();
    public static void init() {}
}

