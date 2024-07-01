package net.akki697222.createjetengine.content.kinetics.jetengines.components;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.akki697222.createjetengine.register.AllPartialModels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class CompressorInstance extends KineticBlockEntityInstance<CompressorBlockEntity> {

    protected final RotatingData compressor_blade;
    protected final RotatingData shaft;
    final Direction direction;
    private final Direction opposite;
    public CompressorInstance(MaterialManager materialManager, CompressorBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(AllPartialModels.SHAFT, blockState, opposite).createInstance();

        compressor_blade = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(AllPartialModels.COMPRESSOR_BLADE, blockState, opposite)
                .createInstance();
        setup(compressor_blade, getSpeed());
        setup(shaft, getSpeed());
    }

    public float getSpeed() {
        float speed = blockEntity.getSpeed() * 5;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update() {
        updateRotation(compressor_blade);
        updateRotation(shaft);
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, compressor_blade);
    }

    @Override
    protected void remove() {
        compressor_blade.delete();
        shaft.delete();
    }
}
