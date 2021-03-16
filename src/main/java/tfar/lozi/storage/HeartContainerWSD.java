package tfar.lozi.storage;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import tfar.lozi.LegendOfZeldaItems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartContainerWSD extends WorldSavedData {

    private final Map<UUID,Integer> uuidStorageHashMap = new HashMap<>();


    public static final UUID uuid = UUID.fromString("90a78391-f56c-4e88-9415-5afd8be43844");

    //this is called via reflection, do not remove
    public HeartContainerWSD(String name) {
        super(name);
    }

    public static HeartContainerWSD getDefaultInstance(WorldServer world) {
        return get(world.getMinecraftServer().getWorld(0));
    }

    public static HeartContainerWSD get(WorldServer world) {
        MapStorage storage = world.getPerWorldStorage();
        String name = LegendOfZeldaItems.MODID+":"+world.provider.getDimension();
        HeartContainerWSD instance = (HeartContainerWSD) storage.getOrLoadData(HeartContainerWSD.class, name);

        if (instance == null) {
            HeartContainerWSD wsd = new HeartContainerWSD(name);
            storage.setData(name, wsd);
            instance = (HeartContainerWSD) storage.getOrLoadData(HeartContainerWSD.class, name);
        }
        return instance;
    }

    public void addHeart(EntityPlayer player) {
        addHearts(player,2);
    }

    public void addHearts(EntityPlayer player,int hearts) {
        uuidStorageHashMap.put(player.getUniqueID(),uuidStorageHashMap.getOrDefault(player.getUniqueID(),0) + hearts);
        updateHearts(player);
        player.heal(2);
        markDirty();
    }

    public void removeHeart(EntityPlayer player) {
        removeHearts(player,2);
    }

    public void removeHearts(EntityPlayer player,int hearts) {
        addHearts(player,-hearts);
    }

    public int getHearts(EntityPlayer player) {
        return uuidStorageHashMap.getOrDefault(player.getUniqueID(),0);
    }

    public void updateHearts(EntityPlayer player) {
        IAttributeInstance iattributeinstance = player.getAttributeMap()
                .getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
        if (iattributeinstance.getModifier(uuid) != null) {
            iattributeinstance.removeModifier(uuid);
        }
        iattributeinstance.applyModifier( new AttributeModifier(HeartContainerWSD.uuid,"Extra Hearts",getHearts(player), 0));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("data", Constants.NBT.TAG_COMPOUND);
        for (NBTBase nbtBase : list) {
            NBTTagCompound compound = (NBTTagCompound)nbtBase;
            UUID uuid = compound.getUniqueId("uuid");
            uuidStorageHashMap.put(uuid,compound.getInteger("hearts"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<UUID,Integer> entry: uuidStorageHashMap.entrySet()) {
            UUID uuid = entry.getKey();
            NBTTagCompound compound1 = new NBTTagCompound();
            compound1.setUniqueId("uuid",uuid);
            compound1.setInteger("hearts",entry.getValue());
            list.appendTag(compound1);
        }
        compound.setTag("data",list);
        return compound;
    }
}
