package tfar.lozi.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HoverBootsItem extends ItemArmor {
    public HoverBootsItem(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn, equipmentSlotIn);
    }

    //called on client and server
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!player.onGround && !player.isInWater()) {
            int ticksLeft = itemStack.getTagCompound().getInteger("hover");
            if (ticksLeft > 0) {
                if (player.motionY < .001) {
                    player.setNoGravity(true);
                    player.motionY = 0;
                }
                ticksLeft--;
                itemStack.getTagCompound().setInteger("hover",ticksLeft);
            } else {
                player.setNoGravity(false);
            }
        } else {
            player.setNoGravity(false);
            if (!itemStack.hasTagCompound()) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            itemStack.getTagCompound().setInteger("hover",30);
        }
    }
}
