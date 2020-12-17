package tfar.lozi.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.World;

public class BombEntity extends EntityTNTPrimed {

    public BombEntity(World worldIn) {
        super(worldIn);
    }

    public BombEntity(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
        super(worldIn, x, y, z, igniter);
    }

}
