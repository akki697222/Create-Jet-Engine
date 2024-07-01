package net.akki697222.createjetengine;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.akki697222.createjetengine.register.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CreateJetEngine.MODID)
public class CreateJetEngine {
    public static final String MODID = "create_jetengine";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public CreateJetEngine() {
        LOGGER.info("Registering Contents");
        REGISTRATE.registerEventListeners(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AllConfigs.CONFIG);

        AllBlocks.init();
        AllBlockEntityTypes.init();
        AllPartialModels.init();
        AllCreativeModeTabs.register(modEventBus);
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
