package tfar.lozi;

import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import tfar.lozi.entity.BoomerangEntity;

public class ModEntities {

    public static final EntityEntry BOOMERANG = EntityEntryBuilder.create()
            .entity(BoomerangEntity.class)
            .id(ModItems.BOOMERANG.getRegistryName(), 0)
            .name(ModItems.BOOMERANG.getRegistryName().getPath())
            .tracker(64, 1, true)
            .build();
}
