package net.akki697222.createjetengine.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.akki697222.createjetengine.content.kinetics.jetengines.components.*;
import net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberBlockEntity;
import net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberInstance;
import net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberRenderer;

import static net.akki697222.createjetengine.CreateJetEngine.REGISTRATE;

public class AllBlockEntityTypes {
    public static final BlockEntityEntry<AirIntakeBlockEntity> AIR_INTAKE = REGISTRATE
            .blockEntity("air_intake", AirIntakeBlockEntity::new)
            .instance(() -> AirIntakeInstance::new, false)
            .validBlocks(AllBlocks.AIR_INTAKE)
            .renderer(() -> AirIntakeRenderer::new)
            .register();
    public static final BlockEntityEntry<CompressorBlockEntity> COMPRESSOR = REGISTRATE
            .blockEntity("compressor", CompressorBlockEntity::new)
            .instance(() -> CompressorInstance::new, false)
            .validBlocks(AllBlocks.COMPRESSOR)
            .renderer(() -> CompressorRenderer::new)
            .register();
    public static final BlockEntityEntry<CombustionChamberBlockEntity> COMBUSTION_CHAMBER = REGISTRATE
            .blockEntity("combustion_chamber", CombustionChamberBlockEntity::new)
            .instance(() -> CombustionChamberInstance::new, false)
            .validBlocks(AllBlocks.COMBUSTION_CHAMBER)
            .renderer(() -> CombustionChamberRenderer::new)
            .register();
    public static final BlockEntityEntry<GasTurbineBlockEntity> GAS_TURBINE = REGISTRATE
            .blockEntity("gas_turbine", GasTurbineBlockEntity::new)
            .instance(() -> GasTurbineInstance::new, false)
            .validBlocks(AllBlocks.GAS_TURBINE)
            .renderer(() -> GasTurbineRenderer::new)
            .register();
    public static void init() {}
}
