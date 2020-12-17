package tfar.lozi.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tfar.lozi.MasterDoorBlockEntity;
import tfar.lozi.ModItems;

import javax.annotation.Nullable;

public class MasterLockedDoorBlock extends LockedDoorBlock {
    public MasterLockedDoorBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean isKey(ItemStack stack, TileEntity tileEntity, EntityPlayer player) {

        if (stack.getItem() != ModItems.MASTER_KEY) {
            player.sendMessage(new TextComponentString("This can only be unlocked with a master key with the code ")
                    .appendSibling(new TextComponentString(((MasterDoorBlockEntity)tileEntity).getCode()).setStyle(new Style().setColor(TextFormatting.GOLD))));
            return false;
        } else if (stack.hasTagCompound() &&
                stack.getTagCompound().getString("code").equals(((MasterDoorBlockEntity)tileEntity).getCode())) {
            return true;
        } else {
            player.sendMessage(new TextComponentString("Master Key has incorrect code, requires ")
                    .appendSibling(new TextComponentString(((MasterDoorBlockEntity)tileEntity).getCode()).setStyle(new Style().setColor(TextFormatting.GOLD))));
            return false;
        }
    }

    @Override
    public boolean isMasterDoor() {
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new MasterDoorBlockEntity();
    }
}
