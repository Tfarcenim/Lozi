package tfar.lozi.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfar.lozi.entity.BombEntity;

import javax.annotation.Nullable;
import java.util.List;

public class QuiverItem extends Item {

    public static boolean hasArrows(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getInteger("arrows") > 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int bombs = 0;
        if (stack.hasTagCompound()) {
            bombs = stack.getTagCompound().getInteger("arrows");
        }
        tooltip.add("Arrows: "+bombs);
    }
}
