package tfar.lozi;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import tfar.lozi.item.BoomerangItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModItems {
    public static final Item BOOMERANG = new BoomerangItem();

    private static List<Item> cache;

    public static void register(IForgeRegistry<Item> registry) {
        BOOMERANG.setRegistryName("boomerang").setTranslationKey(LegendofZeldaItems.MODID+".boomerang");
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
