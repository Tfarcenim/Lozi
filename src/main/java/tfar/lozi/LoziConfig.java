package tfar.lozi;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@Config(modid = LegendOfZeldaItems.MODID)
public class LoziConfig {

    @Config.Name("bombable_blocks")
    public static String[] bombable_blocks = new String[]{Blocks.COBBLESTONE.getRegistryName().toString(),Blocks.STONEBRICK.getRegistryName().toString()};

    @Config.Ignore
    public static Set<Block> bombable = new HashSet<>();

    public static void parseConfig() {
        bombable.clear();
        for (String s : bombable_blocks) {
            bombable.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s)));
        }
    }
}
