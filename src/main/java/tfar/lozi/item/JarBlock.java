package tfar.lozi.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tfar.lozi.ModItems;

import java.util.Random;

public class JarBlock extends Block {

    public JarBlock(Material materialIn) {
        super(materialIn);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        Random rand = ((World) world).rand;
        if (rand.nextInt(5) == 0) {
            drops.add(new ItemStack(Items.ARROW, 5));
        }

        if (rand.nextInt(5) == 0) {
            drops.add(new ItemStack(ModItems.BOMB, 5));
        }
    }
}
