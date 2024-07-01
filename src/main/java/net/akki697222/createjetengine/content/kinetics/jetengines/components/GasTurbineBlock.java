package net.akki697222.createjetengine.content.kinetics.jetengines.components;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.akki697222.createjetengine.register.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import static net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberBlock.TEMPERATURE;

public class GasTurbineBlock extends DirectionalKineticBlock implements IBE<GasTurbineBlockEntity> {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public GasTurbineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<GasTurbineBlockEntity> getBlockEntityClass() {
        return GasTurbineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GasTurbineBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.GAS_TURBINE.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        Direction facing = state.getValue(FACING);
        return face == facing || face == facing.getOpposite() || face == Direction.UP;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }
}
