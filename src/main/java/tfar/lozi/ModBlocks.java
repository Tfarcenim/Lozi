package tfar.lozi;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.IForgeRegistry;
import tfar.lozi.block.LockedDoorBlock;
import tfar.lozi.block.MasterLockedDoorBlock;
import tfar.lozi.item.JarBlock;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ModBlocks {

    public static final Block JAR = new JarBlock(Material.GLASS);
    public static final Block LOCKED_DOOR = new LockedDoorBlock(Material.IRON).setResistance(6000000.0F).setBlockUnbreakable();
    public static final Block LOCKED_MASTER_DOOR = new MasterLockedDoorBlock(Material.IRON).setResistance(6000000.0F).setBlockUnbreakable();
    //public static final Block MASTER_DOOR = new Block(Material.IRON);

    private static List<Block> cache;

    public static void register(IForgeRegistry<Block> registry) {
        for (Field field : ModBlocks.class.getFields()) {
            try {
                Block item = (Block) field.get(null);
                String name = field.getName().toLowerCase(Locale.ROOT);
                item.setRegistryName(name).setTranslationKey(LegendOfZeldaItems.MODID + "."+name).setCreativeTab(ModItems.tab);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        registry.registerAll(getBlocks().toArray(new Block[0]));
    }

    public static List<Block> getBlocks() {
        if (cache == null) {
            cache = Arrays.stream(ModBlocks.class.getFields()).map(field -> {
                try {
                    return field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).map(Block.class::cast).collect(Collectors.toList());
        }
        return cache;
    }
}
