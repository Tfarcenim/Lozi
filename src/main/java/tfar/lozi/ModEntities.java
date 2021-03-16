package tfar.lozi;

import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import tfar.lozi.entity.BombEntity;
import tfar.lozi.entity.BoomerangEntity;
import tfar.lozi.entity.HookshotEntity;

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

    public static final EntityEntry HOOK_SHOT = EntityEntryBuilder.create()
            .entity(HookshotEntity.class)
            .id(ModItems.HOOK_SHOT.getRegistryName(), i++)
            .name(ModItems.HOOK_SHOT.getRegistryName().getPath())
            .tracker(64, 1, true)
            .build();
}
