package tfar.lozi.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfar.lozi.ModItems;

public class LockedDoorBlock extends BlockDoor {
    public LockedDoorBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!worldIn.isRemote && isKey(stack, tileEntity, playerIn)) {
                unlockDoor(worldIn, pos);
            if (!playerIn.capabilities.isCreativeMode && !isMasterDoor()) {
                stack.shrink(1);
            }
        }
        return true;
    }

    public boolean isKey(ItemStack stack, TileEntity tileEntity,EntityPlayer player) {
        return stack.getItem() == ModItems.KEY;
    }

    public boolean isMasterDoor() {
        return false;
    }

    public void unlockDoor(World world, BlockPos pos) {
        IBlockState oldDoor = world.getBlockState(pos);
        EnumDoorHalf half = oldDoor.getValue(HALF);
        world.setBlockState(pos, getUnlockedForm().getDefaultState().withProperty(HALF, half).withProperty(OPEN,oldDoor.getValue(OPEN)).withProperty(FACING,oldDoor.getValue(FACING)));
        if (half == EnumDoorHalf.LOWER) {
            world.setBlockState(pos.up(),getUnlockedForm().getDefaultState().withProperty(HALF,EnumDoorHalf.UPPER).withProperty(OPEN,oldDoor.getValue(OPEN)).withProperty(FACING,oldDoor.getValue(FACING)));
        } else {
            world.setBlockState(pos.down(), getUnlockedForm().getDefaultState().withProperty(HALF, EnumDoorHalf.LOWER).withProperty(OPEN,oldDoor.getValue(OPEN)).withProperty(FACING,oldDoor.getValue(FACING)));
        }
    }

    public Block getUnlockedForm() {
        return Blocks.OAK_DOOR;
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    }
}
