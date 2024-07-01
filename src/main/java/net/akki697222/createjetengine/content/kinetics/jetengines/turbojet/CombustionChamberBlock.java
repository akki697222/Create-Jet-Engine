package net.akki697222.createjetengine.content.kinetics.jetengines.turbojet;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.akki697222.createjetengine.register.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class CombustionChamberBlock extends DirectionalKineticBlock implements IBE<CombustionChamberBlockEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final IntegerProperty TEMPERATURE = IntegerProperty.create("temperature", 0, 1000);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty REBURNING = BooleanProperty.create("reburning");
    public CombustionChamberBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                    .setValue(TEMPERATURE, 20)
                    .setValue(POWERED, false)
                    .setValue(REBURNING, false));

    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.isEmpty())
            return InteractionResult.PASS;
        if (level.getBlockEntity(pos) instanceof SmartBlockEntity be) {
            IFluidHandler tank = be.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
            if (tank == null)
                return InteractionResult.PASS;
            if (itemInHand.getItem() instanceof BucketItem bucketItem) {
                if (!tank.getFluidInTank(0).isEmpty())
                    return InteractionResult.FAIL;
                tank.fill(new FluidStack(bucketItem.getFluid(), 1000), IFluidHandler.FluidAction.EXECUTE);
                if (!player.isCreative())
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                return InteractionResult.SUCCESS;
            }
            IFluidHandlerItem itemTank = itemInHand.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
            if (itemTank == null)
                return InteractionResult.PASS;
            itemTank.drain(tank.fill(itemTank.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        }
        return super.use(state, level, pos ,player, hand, hit);
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return super.getMinimumRequiredSpeedLevel();
    }

    @Override
    public Class<CombustionChamberBlockEntity> getBlockEntityClass() {
        return CombustionChamberBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CombustionChamberBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.COMBUSTION_CHAMBER.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        Direction facing = state.getValue(FACING);
        return face == facing || face == facing.getOpposite();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TEMPERATURE, POWERED, REBURNING);
        super.createBlockStateDefinition(builder);
    }
}
