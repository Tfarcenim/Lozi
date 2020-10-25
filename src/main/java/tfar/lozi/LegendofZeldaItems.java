package tfar.lozi;

import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
}
