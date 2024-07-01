package net.akki697222.createjetengine.content.kinetics.jetengines.components;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.akki697222.createjetengine.register.AllPartialModels;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class GasTurbineRenderer extends KineticBlockEntityRenderer<GasTurbineBlockEntity> {
    public GasTurbineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public Direction getVerticalDirection(Direction currentDirection, boolean isUp) {
        return switch (currentDirection) {
            case NORTH, SOUTH, EAST, WEST -> isUp ? Direction.UP : Direction.DOWN;
            case UP, DOWN -> currentDirection;
            default -> throw new IllegalArgumentException("Unknown direction: " + currentDirection);
        };
    }
    @Override
    protected void renderSafe(GasTurbineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel())) return;

        Direction direction = be.getBlockState().getValue(FACING);
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        int lightBehind = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(direction.getOpposite()));
        int lightInFront = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(direction));
        int lightUp = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(getVerticalDirection(direction, true)));
        int lightAbove = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above());

        SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT, be.getBlockState(), direction.getOpposite());
        SuperByteBuffer shaft_half = getRotatedModel(be, be.getBlockState());
        SuperByteBuffer compressor_blade = CachedBufferer.partialFacing(AllPartialModels.COMPRESSOR_BLADE, be.getBlockState(), direction.getOpposite());

        Direction.Axis rotationAxis;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            rotationAxis = Direction.Axis.Y;
        } else if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            rotationAxis = Direction.Axis.Z;
        } else {
            rotationAxis = Direction.Axis.X;
        }

        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float speed = be.getSpeed();
        float angle = (time * speed * 3f / 10) % 360;
        angle = angle / 180f * (float) Math.PI;

        standardKineticRotationTransform(shaft, be, lightBehind).renderInto(ms, vb);
        kineticRotationTransform(shaft_half, be, Direction.Axis.Y, angle, lightAbove)
                .translate(0, 1, 0)  // ブロックの上面に移動
                .renderInto(ms, vb);
        kineticRotationTransform(compressor_blade, be, direction.getAxis(), angle, lightBehind).renderInto(ms, vb);
    }
    protected SuperByteBuffer getRotatedModel(GasTurbineBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF_UP, state, Direction.UP);
    }
}
