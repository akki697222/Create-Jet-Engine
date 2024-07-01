package net.akki697222.createjetengine.content.kinetics.jetengines.turbojet;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.akki697222.createjetengine.CreateJetEngine;
import net.akki697222.createjetengine.content.kinetics.jetengines.components.GasTurbineBlock;
import net.akki697222.createjetengine.register.AllBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberBlock.TEMPERATURE;

public class CombustionChamberBlockEntity extends KineticBlockEntity {
    private final BlockState state;
    public SmartFluidTankBehaviour tank;
    public CombustionChamberBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.state = state;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return tank.getCapability().cast();
        else if(state.getValue(CombustionChamberBlock.FACING) == Direction.UP) {
            if (cap == ForgeCapabilities.FLUID_HANDLER && side == Direction.NORTH)
                return tank.getCapability().cast();
            if (cap == ForgeCapabilities.FLUID_HANDLER && side == Direction.SOUTH)
                return tank.getCapability().cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

        tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(tank);
        super.addBehaviours(behaviours);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.literal(spacing).append(Component.translatable(CreateJetEngine.MODID + ".tooltip.combustion_chamber").withStyle(ChatFormatting.GRAY)));
        tooltip.add(Component.literal(spacing).append(Component.literal(temperature + "°C ")
                .withStyle(ChatFormatting.AQUA)).append(Component.translatable(CreateJetEngine.MODID + ".tooltip.chamber_temp").withStyle(ChatFormatting.DARK_GRAY)));
        tooltip.add(Component.literal(spacing).append(Component.literal(tankFluidAmount + "mb " + tankFluidName + " ")
                .withStyle(ChatFormatting.AQUA)).append(Component.translatable(CreateJetEngine.MODID + ".tooltip.chamber_fuel").withStyle(ChatFormatting.DARK_GRAY)));
        return true;
    }
    public void reduceTankFluid(int amount) {
        tank.getPrimaryHandler().setFluid(FluidHelper.copyStackWithAmount(tank.getPrimaryHandler().getFluid(), tank.getPrimaryHandler().getFluidAmount() - amount));
    }
    private int tankFluidAmount = 0;
    private String tankFluidName;
    public int temperature = 0;
    @Override
    public void tick() {
        super.tick();
        tankFluidAmount = tank.getPrimaryHandler().getFluid().getAmount();
        tankFluidName = tank.getPrimaryHandler().getFluid().getDisplayName().getString();
        CreateJetEngine.LOGGER.debug(tankFluidName);
        Direction facing = level.getBlockState(worldPosition).getValue(GasTurbineBlock.FACING);
        BlockPos frontPos = worldPosition.relative(facing);
        BlockPos backPos = worldPosition.relative(facing.getOpposite());

        Block frontBlock = level.getBlockState(frontPos).getBlock();
        Block backBlock = level.getBlockState(backPos).getBlock();


        if (frontBlock == AllBlocks.GAS_TURBINE.get() || backBlock == AllBlocks.GAS_TURBINE.get()) {
            if (tank.getPrimaryHandler().getFluidAmount() != 0 && temperature < 600) {
                reduceTankFluid(1);
                temperature++; temperature++;
            }
        } else {
            if (tank.getPrimaryHandler().getFluidAmount() != 0) {
                reduceTankFluid(1);
                temperature++; temperature++;
            }
        }

        BlockPos changePos = new BlockPos(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
        if (temperature > 999) {
            level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 5, Level.ExplosionInteraction.BLOCK);
        } else {
            level.setBlock(changePos, level.getBlockState(changePos).setValue(TEMPERATURE, temperature), 3);
            if (temperature > 0 && tankFluidAmount == 0) {
                temperature--;
            }
        }
    }
}
