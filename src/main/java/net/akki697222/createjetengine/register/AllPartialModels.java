package net.akki697222.createjetengine.register;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.Create;
import net.akki697222.createjetengine.CreateJetEngine;


public class AllPartialModels {
    public static final PartialModel
            JET_ENGINE_FAN_SIX_BLADE = block("jet_engine_fans/fan_six_blade"),
            JET_ENGINE_FAN_EIGHT_BLADE = block("jet_engine_fans/fan_eight_blade"),
            JET_ENGINE_NOSE_CORN = block("jet_engine_parts/nosecone"),
            JET_ENGINE_NOSE_CORN_LONG = block("jet_engine_parts/nosecone_long"),

            COMPRESSOR_BLADE = block("compressor_blades/blade"),
            SHAFT = block("shaft")
    ;

    private static PartialModel block(String path) {
        return new PartialModel(CreateJetEngine.asResource("block/" + path));
    }

    public static void init() {}
}
