package tfar.lozi;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tfar.lozi.item.QuiverItem;
import tfar.lozi.storage.HeartContainerWSD;

import java.util.List;

@Mod(modid = LegendOfZeldaItems.MODID, name = LegendOfZeldaItems.NAME, version = LegendOfZeldaItems.VERSION)
@Mod.EventBusSubscriber
public class LegendOfZeldaItems {
    public static final String MODID = "lozi";
    public static final String NAME = "Legend of Zelda Items";
    public static final String VERSION = "1.0";

    public static LegendOfZeldaItems instance;

    public LegendOfZeldaItems() {
        instance = this;
    }

    @SubscribeEvent
    public static void items(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void blocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
        GameRegistry.registerTileEntity(MasterDoorBlockEntity.class,new ResourceLocation(MODID,"locked_master_door"));
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityEntry> e) {
        e.getRegistry().register(ModEntities.BOOMERANG);
        e.getRegistry().register(ModEntities.BOMB);
    }

    @SubscribeEvent
    public static void playerRespawn(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityPlayer && !e.getWorld().isRemote) {
            HeartContainerWSD.getDefaultInstance((WorldServer) e.getWorld()).updateHearts((EntityPlayer) e.getEntity());
        }
    }

    @SubscribeEvent
    public static void arrowNock(ArrowNockEvent e) {
        if (e.hasAmmo()) {
            return;
        }

        EntityPlayer player = e.getEntityPlayer();
        ItemStack stack = e.getBow();
        ItemStack quiver = getQuiver(player);

        if (!quiver.isEmpty()) {
            if (QuiverItem.hasArrows(quiver)) {
                player.setActiveHand(e.getHand());
                e.setAction(ActionResult.newResult(EnumActionResult.SUCCESS,stack));
            }
        }
    }

    @SubscribeEvent
    public static void arrowLoose(ArrowLooseEvent e) {
        if (e.hasAmmo()) {
            return;
        }

        EntityPlayer player = e.getEntityPlayer();
        ItemStack stack = e.getBow();
        ItemStack quiver = getQuiver(player);

        if (!quiver.isEmpty()) {
            if (QuiverItem.hasArrows(quiver)) {
                e.setCharge(1);
            }
        }
    }

    @SubscribeEvent
    public static void preventPickup(EntityItemPickupEvent event) {
        ItemStack stack = event.getItem().getItem();
        EntityPlayer player = event.getEntityPlayer();
        if (stack.getItem() == ModItems.BOMB) {
            if (hasBombBag(player)) {
                ItemStack bombBag = getBombBag(player);
                if (!bombBag.hasTagCompound()) {
                    bombBag.setTagCompound(new NBTTagCompound());
                }
                int bombs = bombBag.getTagCompound().getInteger("bombs");

                int pickedup = Math.min(stack.getCount(), 20 - bombs);

                if (pickedup > 0) {
                    bombBag.getTagCompound().setInteger("bombs", bombs + pickedup);
                    stack.shrink(pickedup);
                }
            }
            event.setCanceled(true);
            return;
        }

        if (stack.getItem() == Items.ARROW) {
            if (hasQuiver(player)) {
                ItemStack quiver = getQuiver(player);
                if (!quiver.hasTagCompound()) {
                    quiver.setTagCompound(new NBTTagCompound());
                }
                int arrows = quiver.getTagCompound().getInteger("arrows");

                int pickedup = Math.min(stack.getCount(), 20 - arrows);

                if (pickedup > 0) {
                    quiver.getTagCompound().setInteger("arrows", arrows + pickedup);
                    stack.shrink(pickedup);
                }
            }
            event.setCanceled(true);
            return;
        }
    }

    public static ItemStack getBombBag(EntityPlayer player) {
        InventoryPlayer inv = player.inventory;
        for (ItemStack stack : inv.mainInventory) {
            if (stack.getItem() == ModItems.BOMB_BAG) {
                return stack;
            }
        }

        for (ItemStack stack : inv.offHandInventory) {
            if (stack.getItem() == ModItems.BOMB_BAG) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getQuiver(EntityPlayer player) {
        InventoryPlayer inv = player.inventory;
        for (ItemStack stack : inv.mainInventory) {
            if (stack.getItem() == ModItems.QUIVER) {
                return stack;
            }
        }

        for (ItemStack stack : inv.offHandInventory) {
            if (stack.getItem() == ModItems.QUIVER) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static final ItemStack bomb_bag = new ItemStack(ModItems.BOMB_BAG);
    public static final ItemStack quiver = new ItemStack(ModItems.QUIVER);

    //baubles support?
    public static boolean hasBombBag(EntityPlayer player) {
        return (player.inventory.hasItemStack(bomb_bag));
    }

    public static boolean hasQuiver(EntityPlayer player) {
        return (player.inventory.hasItemStack(quiver));
    }
}
