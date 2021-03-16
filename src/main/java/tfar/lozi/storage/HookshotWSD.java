package tfar.lozi.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import tfar.lozi.LegendOfZeldaItems;
import tfar.lozi.entity.HookshotEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HookshotWSD extends WorldSavedData {

    private final Map<UUID, HookshotEntity> playerHookshotEntityMap = new HashMap<>();

    public HookshotWSD(String name) {
        super(name);
    }

    public static HookshotWSD getDefaultInstance(WorldServer world) {
        return get(world.getMinecraftServer().getWorld(0));
    }

    public static HookshotWSD get(WorldServer world) {
        MapStorage storage = world.getPerWorldStorage();
        String name = LegendOfZeldaItems.MODID+":hookshot"+world.provider.getDimension();
        HookshotWSD instance = (HookshotWSD) storage.getOrLoadData(HookshotWSD.class, name);

        if (instance == null) {
            HookshotWSD wsd = new HookshotWSD(name);
            storage.setData(name, wsd);
            instance = (HookshotWSD) storage.getOrLoadData(HookshotWSD.class, name);
        }
        return instance;
    }

    public boolean hasHookshot(EntityPlayer player) {
        return playerHookshotEntityMap.containsKey(player.getGameProfile().getId());
    }

    public HookshotEntity getHookshot(EntityPlayer player) {
        return playerHookshotEntityMap.get(player.getGameProfile().getId());
    }

    public void addHookshot(EntityPlayer player,HookshotEntity hookShot) {
        playerHookshotEntityMap.put(player.getGameProfile().getId(), hookShot);
    }

    public void removeHookshot(EntityPlayer player) {
        playerHookshotEntityMap.remove(player.getGameProfile().getId());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return null;
    }
}
