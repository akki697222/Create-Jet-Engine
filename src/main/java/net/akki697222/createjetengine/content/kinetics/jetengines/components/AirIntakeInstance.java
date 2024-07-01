package net.akki697222.createjetengine.content.kinetics.jetengines.components;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

import com.jozufozu.flywheel.api.MaterialManager;
import net.akki697222.createjetengine.register.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;


public class AirIntakeInstance extends KineticBlockEntityInstance<AirIntakeBlockEntity> {
    protected final RotatingData shaft;
    protected final RotatingData fan;

    protected final RotatingData nose;
    final Direction direction;
    private final Direction opposite;

    public AirIntakeInstance(MaterialManager materialManager, AirIntakeBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockState.getValue(FACING);

        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(com.simibubi.create.AllPartialModels.SHAFT_HALF, blockState, opposite).createInstance();
        fan = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(AllPartialModels.JET_ENGINE_FAN_EIGHT_BLADE, blockState, opposite)
                .createInstance();

        nose = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(AllPartialModels.JET_ENGINE_NOSE_CORN, blockState, opposite)
                .createInstance();


        setup(shaft);
        setup(fan, getFanSpeed());
        setup(nose, getFanSpeed());
    }

    private float getFanSpeed() {
        float speed = blockEntity.getSpeed() * 5;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update() {
        updateRotation(shaft);
        updateRotation(fan, getFanSpeed());
        updateRotation(nose, getFanSpeed());
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, fan);
        relight(inFront, nose);
    }

    @Override
    public void remove() {
        shaft.delete();
        fan.delete();
        nose.delete();
    }
}
