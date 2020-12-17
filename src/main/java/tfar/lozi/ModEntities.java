package tfar.lozi;

import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import tfar.lozi.entity.BombEntity;
import tfar.lozi.entity.BoomerangEntity;

public class ModEntities {

    static int i = 0;

    public static final EntityEntry BOOMERANG = EntityEntryBuilder.create()
            .entity(BoomerangEntity.class)
            .id(ModItems.BOOMERANG.getRegistryName(), i++)
            .name(ModItems.BOOMERANG.getRegistryName().getPath())
            .tracker(64, 1, true)
            .build();

    public static final EntityEntry BOMB = EntityEntryBuilder.create()
            .entity(BombEntity.class)
            .id(ModItems.BOMB.getRegistryName(), i++)
            .name(ModItems.BOMB.getRegistryName().getPath())
            .tracker(64, 1, true)
            .build();
}
