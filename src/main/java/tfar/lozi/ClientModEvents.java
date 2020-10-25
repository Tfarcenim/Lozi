package tfar.lozi;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientModEvents {

	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		OBJLoader.INSTANCE.addDomain(LegendofZeldaItems.MODID);
		//ModItems.getItems().forEach(ClientModEvents::registerItemModel);
		ModelLoader.setCustomModelResourceLocation(ModItems.BOOMERANG, 0, new ModelResourceLocation(ModItems.BOOMERANG.getRegistryName().toString(), "inventory"));
	}

	public static void registerItemModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item,0,new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
