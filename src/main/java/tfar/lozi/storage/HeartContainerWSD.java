package tfar.lozi.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tfar.lozi.LegendofZeldaItems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartContainerWSD extends WorldSavedData {

    private final Map<UUID,Integer> uuidStorageHashMap = new HashMap<>();

    //this is called via reflection, do not remove
    public HeartContainerWSD(String name) {
        super(name);
    }

    public static HeartContainerWSD getInstance(int dimension) {
        return get(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension));
    }

    public static HeartContainerWSD getDefaultInstance() {
        return getInstance(0);
    }

    public static HeartContainerWSD get(WorldServer world) {
        MapStorage storage = world.getPerWorldStorage();
        String name = LegendofZeldaItems.MODID+":"+world.provider.getDimension();
        HeartContainerWSD instance = (HeartContainerWSD) storage.getOrLoadData(HeartContainerWSD.class, name);

        if (instance == null) {
            HeartContainerWSD wsd = new HeartContainerWSD(name);
            storage.setData(name, wsd);
            instance = (HeartContainerWSD) storage.getOrLoadData(HeartContainerWSD.class, name);
        }
        return instance;
    }

    public void addHeart(EntityPlayer player) {
        addHearts(player,1);
    }

    public void addHearts(EntityPlayer player,int hearts) {
        uuidStorageHashMap.put(player.getUniqueID(),uuidStorageHashMap.getOrDefault(player.getUniqueID(),0) + hearts);
    }

    public void removeHeart(EntityPlayer player) {
        removeHearts(player,1);
    }

    public void removeHearts(EntityPlayer player,int hearts) {
        addHearts(player,-hearts);
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
