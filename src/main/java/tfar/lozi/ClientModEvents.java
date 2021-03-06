package tfar.lozi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import tfar.lozi.entity.BombEntity;
import tfar.lozi.entity.BoomerangEntity;
import tfar.lozi.render.BombRenderer;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientModEvents {

	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		OBJLoader.INSTANCE.addDomain(LegendOfZeldaItems.MODID);

		RenderingRegistry.registerEntityRenderingHandler(BombEntity.class, renderManager -> new BombRenderer(renderManager, ModItems.BOMB,
				Minecraft.getMinecraft().getRenderItem()));


		RenderingRegistry.registerEntityRenderingHandler(BoomerangEntity.class, renderManager -> new RenderSnowball<>(renderManager, ModItems.BOOMERANG,
				Minecraft.getMinecraft().getRenderItem()));

		ModItems.getItems().forEach(ClientModEvents::registerItemModel);
	}

	public static void registerItemModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item,0,new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
