package net.akki697222.createjetengine.system;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FilteredFluidTankBehaviour extends SmartFluidTankBehaviour {
    public FilteredFluidTankBehaviour(BehaviourType<SmartFluidTankBehaviour> type, SmartBlockEntity be, int tanks, int tankCapacity, boolean enforceVariety) {
        super(type, be, tanks, tankCapacity, enforceVariety);
    }

    public static FilteredFluidTankBehaviour single(SmartBlockEntity be, int capacity) {
        return new FilteredFluidTankBehaviour(TYPE, be, 1, capacity, false);
    }
    public class InternalFluidHandler extends CombinedTankWrapper {
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.getFluid() != Fluids.LAVA)
                return 0;
            return super.fill(resource, action);
        }
    }
}
