package net.akki697222.createjetengine.content.kinetics.jetengines.components;

import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import net.akki697222.createjetengine.register.AllBlocks;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.akki697222.createjetengine.CreateJetEngine;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static net.akki697222.createjetengine.content.kinetics.jetengines.components.GasTurbineBlock.POWERED;
import static net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberBlock.COMPRESSED_AIR;
import static net.akki697222.createjetengine.content.kinetics.jetengines.turbojet.CombustionChamberBlock.TEMPERATURE;

public class GasTurbineBlockEntity extends GeneratingKineticBlockEntity {
    private int temperatureFront;

    protected ScrollValueBehaviour generatedSpeed;
    private int temperatureBack;
    private boolean airSupplyFront;
    private boolean airSupplyBack;
    private boolean active = false;
    public GasTurbineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public int t;
    @Override
    public void tick() {
        super.tick();
        if (getBlockState().getValue(POWERED) && !level.hasNeighborSignal(getBlockPos()))
            level.setBlock(getBlockPos(), getBlockState().cycle(POWERED), 2);
        t++;
        Direction facing = level.getBlockState(worldPosition).getValue(GasTurbineBlock.FACING);
        BlockPos frontPos = worldPosition.relative(facing);
        BlockPos backPos = worldPosition.relative(facing.getOpposite());

        temperatureFront = getTemperature(frontPos);
        temperatureBack = getTemperature(backPos);
        airSupplyFront = getAirSupply(frontPos);
        airSupplyBack = getAirSupply(backPos);

        if (t > 19) {
            //CreateJetEngine.LOGGER.debug("Front " + temperatureFront + " | Back " + temperatureBack);
            t = 0;
        }
        if (temperatureFront != 0) {
            if (!active) {
                if (temperatureFront > 299 && airSupplyFront) {
                    active = true;
                    generatedSpeed.value = temperatureBack / 256 * 100;
                    updateGeneratedRotation();
                }
            } else {
                if(temperatureFront < 300 || getBlockState().getValue(GasTurbineBlock.POWERED)) {
                    active = false;
                    updateGeneratedRotation();
                }
            }
        } else if (temperatureBack != 0) {
            if (!active) {
                if (temperatureBack > 299 && airSupplyBack) {
                    active = true;
                    generatedSpeed.value = temperatureBack / 256 * 100;
                    updateGeneratedRotation();
                }
            } else {
                if(temperatureBack < 300 || getBlockState().getValue(GasTurbineBlock.POWERED)) {
                    active = false;
                    updateGeneratedRotation();
                }
            }
        } else if (temperatureBack == 0 && temperatureFront == 0){
            active = false;
            updateGeneratedRotation();
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.literal(spacing).append(Component.translatable(CreateJetEngine.MODID + ".tooltip.jet_engine").withStyle(ChatFormatting.GRAY)));
        int temp = 0;
        if (temperatureBack != 0)
            temp = temperatureBack;
        if (temperatureFront != 0)
            temp = temperatureFront;
        String status;
        if (active) {
            status = I18n.get(CreateJetEngine.MODID + ".tooltip.status.active");
        } else {
            status = I18n.get(CreateJetEngine.MODID + ".tooltip.status.disable");
        }
        tooltip.add(Component.literal(spacing).append(Component.literal(temp + "°C ")
                .withStyle(ChatFormatting.AQUA)).append(Component.translatable(CreateJetEngine.MODID + ".tooltip.exhaust_temp").withStyle(ChatFormatting.DARK_GRAY)));
        tooltip.add(Component.literal(spacing).append(Component.literal(status + " ")
                .withStyle(ChatFormatting.AQUA)).append(Component.translatable(CreateJetEngine.MODID + ".tooltip.status").withStyle(ChatFormatting.DARK_GRAY)));
        return true;
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        active = compound.getBoolean("active");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("active", active);
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        CenteredSideValueBoxTransform slot =
                new CenteredSideValueBoxTransform((motor, side) -> motor.getValue(GasTurbineBlock.FACING) == side.getOpposite());

        generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("generic.speed"), this, slot);
        generatedSpeed.between(-256, 256);
        generatedSpeed.value = 16;
        generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
        behaviours.add(generatedSpeed);
    }
    private int getTemperature(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(TEMPERATURE)) {
            return state.getValue(TEMPERATURE);
        }
        return 0;
    }
    private boolean getAirSupply(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(COMPRESSED_AIR)) {
            return state.getValue(COMPRESSED_AIR);
        }
        return false;
    }
    public void updateGeneratedRotation() {
        super.updateGeneratedRotation();
    }
    public float calculateAddedStressCapacity() {
        float capacity = 8192;
        this.lastCapacityProvided = capacity;
        return capacity;
    }
    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }
    @Override
    public float getGeneratedSpeed() {
        if (!AllBlocks.GAS_TURBINE.has(getBlockState())) return 0;
        return convertToDirection(active ? generatedSpeed.getValue() : 0, getBlockState().getValue(GasTurbineBlock.FACING));
    }
}
