package tfar.lozi;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import tfar.lozi.item.BombItem;
import tfar.lozi.item.BoomerangItem;
import tfar.lozi.item.HeartContainerItem;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ModItems {

    protected static final CreativeTabs tab = new CreativeTabs(LegendofZeldaItems.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BOOMERANG);
        }
    };

    public static final Item BOOMERANG = new BoomerangItem();
    public static final Item BOMB = new BombItem();
    public static final Item JAR = new ItemBlock(ModBlocks.JAR);
    public static final Item QUIVER = new Item();
    public static final Item BOMB_BAG = new Item();
    public static final Item KEY = new Item();
    public static final Item MASTER_KEY = new Item();
    public static final Item HEART_CONTAINER = new HeartContainerItem();

    private static List<Item> cache;

    public static void register(IForgeRegistry<Item> registry) {
        for (Field field : ModItems.class.getFields()) {
            try {
                Item item = (Item) field.get(null);
                String name = field.getName().toLowerCase(Locale.ROOT);
                item.setRegistryName(name).setTranslationKey(LegendofZeldaItems.MODID + "."+name).setCreativeTab(tab);
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
