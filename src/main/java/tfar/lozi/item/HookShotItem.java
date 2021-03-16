package tfar.lozi.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import tfar.lozi.entity.HookshotEntity;
import tfar.lozi.storage.HookshotWSD;

public class HookShotItem extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            HookshotWSD hookshotWSD = HookshotWSD.get((WorldServer)worldIn);
            boolean hasHookshot = hookshotWSD.hasHookshot(playerIn);

            if (hasHookshot) {
                HookshotEntity hookshotEntity = hookshotWSD.getHookshot(playerIn);
                int i = hookshotEntity.handleHookRetraction();
                playerIn.swingArm(handIn);
                worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            } else {
                worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

                HookshotEntity entityfishhook = new HookshotEntity(worldIn, playerIn);
                worldIn.spawnEntity(entityfishhook);

                playerIn.swingArm(handIn);
                playerIn.addStat(StatList.getObjectUseStats(this));
                hookshotWSD.addHookshot(playerIn,entityfishhook);
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
