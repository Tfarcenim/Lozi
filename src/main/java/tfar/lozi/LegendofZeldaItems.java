package tfar.lozi;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import tfar.lozi.entity.BoomerangEntity;
import tfar.lozi.render.BoomerangRenderer;

@Mod(modid = LegendofZeldaItems.MODID, name = LegendofZeldaItems.NAME, version = LegendofZeldaItems.VERSION)
@Mod.EventBusSubscriber
public class LegendofZeldaItems {
    public static final String MODID = "lozi";
    public static final String NAME = "Legend of Zelda Items";
    public static final String VERSION = "1.0";

    public LegendofZeldaItems() {
        OBJLoader.INSTANCE.addDomain(LegendofZeldaItems.MODID);
    }

    @SubscribeEvent
    public static void items(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(BoomerangEntity.class, renderManager -> new BoomerangRenderer(renderManager, ModItems.BOOMERANG,
                Minecraft.getMinecraft().getRenderItem()));
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityEntry> e) {

        final ResourceLocation resourceLocation = new ResourceLocation(MODID, "boomerang");

        e.getRegistry().register(
                EntityEntryBuilder.create()
                        .entity(BoomerangEntity.class)
                        .id(resourceLocation, 0)
                        .name(resourceLocation.getPath())
                        .tracker(64, 1, true)
                        .build());
    }
}
