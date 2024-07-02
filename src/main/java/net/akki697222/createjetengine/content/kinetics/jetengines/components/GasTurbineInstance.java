package net.akki697222.createjetengine.content.kinetics.jetengines.components;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.akki697222.createjetengine.register.AllPartialModels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class GasTurbineInstance extends KineticBlockEntityInstance<GasTurbineBlockEntity> {
    protected final RotatingData compressor_blade;
    protected final RotatingData shaft;
    protected final RotatingData shaft_half;
    final Direction direction;
    private final Direction opposite;
    public GasTurbineInstance(MaterialManager materialManager, GasTurbineBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(AllPartialModels.SHAFT, blockState, opposite).createInstance();
        shaft_half = getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF_UP, blockState, direction).createInstance();

        compressor_blade = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(AllPartialModels.COMPRESSOR_BLADE, blockState, opposite)
                .createInstance();
        setup(compressor_blade, getSpeed());
        setup(shaft, getSpeed());
        setup(shaft_half, getSpeed());
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
        updateRotation(shaft_half, Direction.Axis.Y);
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);
        BlockPos up = pos.relative(Direction.UP);
        relight(behind, shaft);
        relight(up, shaft_half);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, compressor_blade);
    }

    @Override
    protected void remove() {
        compressor_blade.delete();
        shaft.delete();
        shaft_half.delete();
    }
}
