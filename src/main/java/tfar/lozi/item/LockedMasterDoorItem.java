package tfar.lozi.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfar.lozi.MasterDoorBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public class LockedMasterDoorItem extends ItemDoor {
    public LockedMasterDoorItem(Block block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack origDoor = player.getHeldItem(hand).copy();
        EnumActionResult result = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        if (result == EnumActionResult.SUCCESS) {
            BlockPos doorStart = pos.offset(facing);
            TileEntity bottomDoorState = worldIn.getTileEntity(doorStart);
            TileEntity topDoorState = worldIn.getTileEntity(doorStart.up());
            if (bottomDoorState instanceof MasterDoorBlockEntity && origDoor.hasTagCompound()) {
                ((MasterDoorBlockEntity) bottomDoorState).setCode(origDoor.getTagCompound().getString("code"));
                ((MasterDoorBlockEntity) bottomDoorState).setCode(origDoor.getTagCompound().getString("code"));
            }

            if (topDoorState instanceof MasterDoorBlockEntity && origDoor.hasTagCompound()) {
                ((MasterDoorBlockEntity) topDoorState).setCode(origDoor.getTagCompound().getString("code"));
                ((MasterDoorBlockEntity) topDoorState).setCode(origDoor.getTagCompound().getString("code"));
            }
        }
        return result;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("code")) {
            tooltip.add("Unlocked By: "+stack.getTagCompound().getString("code"));
        }
    }
}
