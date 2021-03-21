package tfar.lozi.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfar.lozi.entity.BombEntity;

import javax.annotation.Nullable;
import java.util.List;

public class BombBagItem extends Item {

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && hasBombs(player.getHeldItem(hand))) {
            ItemStack stack = player.getHeldItem(hand);
            if (hasBombs(stack)) {
                BombEntity bombEntity = new BombEntity(worldIn, pos.getX() + 0.5, pos.getY()+1, pos.getZ() + 0.5, player);
                worldIn.spawnEntity(bombEntity);
                worldIn.playSound(null, bombEntity.posX, bombEntity.posY, bombEntity.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                stack.getTagCompound().setInteger("bombs", stack.getTagCompound().getInteger("bombs") - 1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

    public static boolean hasBombs(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getInteger("bombs") > 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int bombs = 0;
        if (stack.hasTagCompound()) {
            bombs = stack.getTagCompound().getInteger("bombs");
        }
        tooltip.add("Bombs: "+bombs);
    }
}
