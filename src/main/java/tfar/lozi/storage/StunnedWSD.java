package tfar.lozi.storage;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import tfar.lozi.LegendOfZeldaItems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StunnedWSD extends WorldSavedData {

    private final HashMap<Entity,Integer> uuids = new HashMap<>();

    public StunnedWSD(String name) {
        super(name);
    }

    public static StunnedWSD getDefaultInstance(WorldServer world) {
        return get(world.getMinecraftServer().getWorld(0));
    }

    public static StunnedWSD get(WorldServer world) {
        MapStorage storage = world.getPerWorldStorage();
        String name = LegendOfZeldaItems.MODID+":"+world.provider.getDimension();
        StunnedWSD instance = (StunnedWSD) storage.getOrLoadData(StunnedWSD.class, name);

        if (instance == null) {
            StunnedWSD wsd = new StunnedWSD(name);
            storage.setData(name, wsd);
            instance = (StunnedWSD) storage.getOrLoadData(StunnedWSD.class, name);
        }
        return instance;
    }

    public void addStunned(Entity entity) {

    }

    public void tickStunned() {
        for (Map.Entry<Entity,Integer> entry: uuids.entrySet()) {

        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return compound;
    }
}
