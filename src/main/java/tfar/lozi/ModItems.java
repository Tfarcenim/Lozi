package tfar.lozi;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraftforge.registries.IForgeRegistry;
import tfar.lozi.item.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ModItems {

    protected static final CreativeTabs tab = new CreativeTabs(LegendOfZeldaItems.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BOOMERANG);
        }
    };

    public static final Item BOOMERANG = new BoomerangItem();
    public static final Item BOMB = new BombItem();
    public static final Item JAR = new ItemBlock(ModBlocks.JAR);
    public static final Item QUIVER = new QuiverItem().setMaxStackSize(1);
    public static final Item BOMB_BAG = new BombBagItem().setMaxStackSize(1);
    public static final Item KEY = new Item();
    public static final Item MASTER_KEY = new MasterKeyItem();
    public static final Item LOCKED_DOOR = new ItemDoor(ModBlocks.LOCKED_DOOR);
    public static final Item LOCKED_MASTER_DOOR = new LockedMasterDoorItem(ModBlocks.LOCKED_MASTER_DOOR);
    public static final Item HEART_CONTAINER = new HeartContainerItem();
    public static final Item HOOK_SHOT = new HookShotItem();
    public static final Item HOVER_BOOTS = new HoverBootsItem(ItemArmor.ArmorMaterial.DIAMOND,1, EntityEquipmentSlot.FEET);

    private static List<Item> cache;

    public static void register(IForgeRegistry<Item> registry) {
        for (Field field : ModItems.class.getFields()) {
            try {
                Item item = (Item) field.get(null);
                String name = field.getName().toLowerCase(Locale.ROOT);
                item.setRegistryName(name).setTranslationKey(LegendOfZeldaItems.MODID + "."+name).setCreativeTab(tab);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        registry.registerAll(getItems().toArray(new Item[0]));
    }

    public static List<Item> getItems() {
        if (cache == null) {
            cache = Arrays.stream(ModItems.class.getFields()).map(field -> {
                try {
                    return field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).map(Item.class::cast).collect(Collectors.toList());
        }
        return cache;
    }
}
