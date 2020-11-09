package tfar.lozi;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import tfar.lozi.storage.HeartContainerWSD;

import static tfar.lozi.storage.HeartContainerWSD.SPAWN_REINFORCEMENTS_CHANCE;

@Mod(modid = LegendofZeldaItems.MODID, name = LegendofZeldaItems.NAME, version = LegendofZeldaItems.VERSION)
@Mod.EventBusSubscriber
public class LegendofZeldaItems {
    public static final String MODID = "lozi";
    public static final String NAME = "Legend of Zelda Items";
    public static final String VERSION = "1.0";

    public static LegendofZeldaItems instance;

    public LegendofZeldaItems() {
        instance = this;
    }

    @SubscribeEvent
    public static void items(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {

    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityEntry> e) {
        e.getRegistry().register(ModEntities.BOOMERANG);
    }

    @SubscribeEvent
    public static void playerRespawn(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityPlayer && !e.getWorld().isRemote) {
            HeartContainerWSD.getDefaultInstance((WorldServer) e.getWorld()).updateHearts((EntityPlayer) e.getEntity());
        }
    }
}
