package tfar.lozi.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tfar.lozi.ModItems;

import java.util.List;

public class BoomerangEntity extends EntityThrowable {

    private EntityPlayer owner;
    private boolean returning;
    private int airTime;

    public BoomerangEntity(World worldIn) {
        super(worldIn);
    }

    public BoomerangEntity(World worldIn, EntityPlayer playerIn) {
        super(worldIn);
        owner = playerIn;
        this.posX = playerIn.posX;
        this.posY = playerIn.posY + playerIn.getEyeHeight();
        this.posZ = playerIn.posZ;
        setNoGravity(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!returning && airTime > 30) {
            returning = true;
        }

        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));

        for (Entity entity1 : list) {
            if (entity1 instanceof EntityItem) {
                entity1.startRiding(this);
            }
        }

        adjustVelocity();
    }

    private void adjustVelocity() {
        if (returning && owner != null) {
            Vec3d playerPos = new Vec3d(owner.posX, owner.posY + 1, owner.posZ);
            Vec3d dirVec = playerPos.subtract(getPositionVector()).normalize().scale(.3);
            this.motionX = dirVec.x;
            this.motionY = dirVec.y;
            this.motionZ = dirVec.z;

        } else {
            //this.motionX -= .01;
            //this.motionY -= .01;
            //this.motionZ -= .01;
        }
        markVelocityChanged();
    }

    protected void onImpact(RayTraceResult raytraceresult) {
        if (!world.isRemote) {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                returning = true;
            } else if (raytraceresult.typeOfHit == RayTraceResult.Type.ENTITY) {
                if (raytraceresult.entityHit == owner) {
                    owner.addItemStackToInventory(new ItemStack(ModItems.BOOMERANG));
                    this.setDead();
                } else if (raytraceresult.entityHit instanceof EntityLivingBase) {
                    raytraceresult.entityHit.updateBlocked = true;
                    returning = true;
                } else if (raytraceresult.entityHit instanceof EntityItem) {
                    raytraceresult.entityHit.startRiding(this);
                }
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    public void throwBoomerang(Vec3d direction) {
        Vec3d vector = direction.scale(1);
        motionX = vector.x;
        motionY = vector.y;
        motionZ = vector.z;
    }
}
