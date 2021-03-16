package tfar.lozi.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import tfar.lozi.item.HookShotItem;
import tfar.lozi.storage.HookshotWSD;

import java.util.List;
import java.util.UUID;

public class HookshotEntity extends Entity implements IEntityAdditionalSpawnData {


    private EntityPlayer angler;
    private boolean inGround;
    private int ticksInGround;
    private State currentState = State.FLYING;
    private int ticksInAir;
    private Entity caughtEntity;
    private static final DataParameter<Boolean> PULLING = EntityDataManager.createKey(HookshotEntity.class, DataSerializers.BOOLEAN);

    public HookshotEntity(World world) {
        super(world);
    }

    public HookshotEntity(World worldIn, EntityPlayer fishingPlayer) {
        super(worldIn);
        this.init(fishingPlayer);
        this.shoot();
    }

    private void init(EntityPlayer p_190626_1_) {
        this.setSize(0.25F, 0.25F);
        this.ignoreFrustumCheck = true;
        this.angler = p_190626_1_;
    }

    private void shoot() {
        float f = this.angler.prevRotationPitch + (this.angler.rotationPitch - this.angler.prevRotationPitch);
        float f1 = this.angler.prevRotationYaw + (this.angler.rotationYaw - this.angler.prevRotationYaw);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        double d0 = this.angler.prevPosX + (this.angler.posX - this.angler.prevPosX) - (double) f3 * 0.3D;
        double d1 = this.angler.prevPosY + (this.angler.posY - this.angler.prevPosY) + (double) this.angler.getEyeHeight();
        double d2 = this.angler.prevPosZ + (this.angler.posZ - this.angler.prevPosZ) - (double) f2 * 0.3D;
        this.setLocationAndAngles(d0, d1, d2, f1, f);
        this.motionX = -f3;
        this.motionY = MathHelper.clamp(-(f5 / f4), -5.0F, 5.0F);
        this.motionZ = -f2;
        float f6 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX *= 0.9D / (double) f6 + 0.5D;
        this.motionY *= 0.9D / (double) f6 + 0.5D;
        this.motionZ *= 0.9D / (double) f6 + 0.5D;
        float f7 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f7) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }


    @Override
    protected void entityInit() {
        this.dataManager.register(PULLING,false);
    }

    public void setPulling() {
        this.dataManager.set(PULLING,true);
    }

    public boolean isPulling() {
        return dataManager.get(PULLING);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (angler == null) {
            setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (angler != null) {
            if (isPulling()) {

                Vec3d direction = this.getPositionVector().subtract(angler.getPositionVector()).normalize();

                angler.motionX = direction.x;
                angler.motionY = direction.y;
                angler.motionZ = direction.z;
            }
        }

        if (this.angler == null) {
            this.setDead();
        } else if (this.world.isRemote || !this.shouldStopFishing()) {
            if (this.inGround) {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200) {
                    this.setDead();
                    return;
                }
            }

            float f = 0.0F;
            BlockPos blockpos = new BlockPos(this);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == Material.WATER) {
                f = BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos);
            }

            if (this.currentState == State.FLYING) {
                if (this.caughtEntity != null) {
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                    this.currentState = State.HOOKED_IN_ENTITY;
                    return;
                }

                if (!this.world.isRemote) {
                    this.checkCollision();
                }

                if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
                    ++this.ticksInAir;
                } else {
                    this.ticksInAir = 0;
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
            } else {
                if (this.currentState == State.HOOKED_IN_ENTITY) {
                    if (this.caughtEntity != null) {
                        if (this.caughtEntity.isDead) {
                            this.caughtEntity = null;
                            this.currentState = State.FLYING;
                        } else {
                            this.posX = this.caughtEntity.posX;
                            double d2 = this.caughtEntity.height;
                            this.posY = this.caughtEntity.getEntityBoundingBox().minY + d2 * 0.8D;
                            this.posZ = this.caughtEntity.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }

                    return;
                }

                if (this.currentState == State.BOBBING) {
                    this.motionX *= 0.9D;
                    this.motionZ *= 0.9D;
                    double d0 = this.posY + this.motionY - (double) blockpos.getY() - (double) f;

                    if (Math.abs(d0) < 0.01D) {
                        d0 += Math.signum(d0) * 0.1D;
                    }

                    this.motionY -= d0 * (double) this.rand.nextFloat() * 0.2D;

                    if (!this.world.isRemote && f > 0.0F) {
                        //this.catchingFish(blockpos);
                    }
                }
            }

            if (iblockstate.getBlock() != Blocks.AIR) {
                this.motionY = 0.0D;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            //this.updateRotation();
            double d1 = 0.92D;
            //this.motionX *= 0.92D;
            //this.motionY *= 0.92D;
            //this.motionZ *= 0.92D;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private void checkCollision() {
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1, false, true, false);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (raytraceresult != null) {
            vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }

        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double d0 = 0.0D;

        for (Entity entity1 : list) {
            if (this.canBeHooked(entity1) && (entity1 != this.angler || this.ticksInAir >= 5)) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                if (raytraceresult1 != null) {
                    double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS) {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.ENTITY) {
                this.caughtEntity = raytraceresult.entityHit;
                //  this.setHookedEntity();
            } else {
                this.inGround = true;
            }
        }
    }

    protected boolean canBeHooked(Entity entity) {
        return entity.canBeCollidedWith() || entity instanceof EntityItem;
    }

    private boolean shouldStopFishing() {
        ItemStack itemstack = this.angler.getHeldItemMainhand();
        ItemStack itemstack1 = this.angler.getHeldItemOffhand();
        boolean flag = itemstack.getItem() instanceof HookShotItem;
        boolean flag1 = itemstack1.getItem() instanceof HookShotItem;

        int maxDist = 64;

        if (!this.angler.isDead && this.angler.isEntityAlive() && (flag || flag1) && this.getDistanceSq(this.angler) <= maxDist * maxDist) {
            return false;
        } else {
            this.setDead();
            return true;
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        if (ticksExisted > 5) {
            setDead();
        }
    }

    public int handleHookRetraction() {
        if (!this.world.isRemote && this.angler != null) {
            if (inGround) {
                setPulling();
                int i = 0;

                if (this.caughtEntity != null) {
                    // this.bringInHookedEntity();
                    this.world.setEntityState(this, (byte) 31);
                    i = this.caughtEntity instanceof EntityItem ? 3 : 5;
                }

                if (this.inGround) {
                    i = 2;
                }

                //this.setDead();
                return i;
            } else {
                setDead();
                return 0;
            }
        } else {
            return 0;
        }
    }

    public EntityPlayer getAngler() {
        return angler;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (angler != null && !world.isRemote)
        HookshotWSD.get((WorldServer) world).removeHookshot(angler);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        if (angler != null) {
            UUID uuid = angler.getGameProfile().getId();
            buffer.writeLong(uuid.getMostSignificantBits());
            buffer.writeLong(uuid.getLeastSignificantBits());
        }
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        UUID uuid = new UUID(additionalData.readLong(), additionalData.readLong());
        angler = world.getPlayerEntityByUUID(uuid);
    }

    enum State {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING
    }
}
