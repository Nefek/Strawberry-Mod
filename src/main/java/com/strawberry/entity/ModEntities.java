package com.strawberry.entity;

import com.strawberry.StrawberryMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> STRAWBERRY_COW_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(StrawberryMod.MOD_ID, "strawberry_cow")
    );

    public static final EntityType<StrawberryCowEntity> STRAWBERRY_COW = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            STRAWBERRY_COW_KEY,
            EntityType.Builder.of(StrawberryCowEntity::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.4F)
                    .eyeHeight(1.3F)
                    .clientTrackingRange(10)
                    .build(STRAWBERRY_COW_KEY)
    );

    public static final ResourceKey<EntityType<?>> STRAWBERRY_CALF_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(StrawberryMod.MOD_ID, "strawberry_calf")
    );

    public static final EntityType<StrawberryCalfEntity> STRAWBERRY_CALF = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            STRAWBERRY_CALF_KEY,
            EntityType.Builder.of(StrawberryCalfEntity::new, MobCategory.CREATURE)
                    .sized(0.45F, 0.7F)
                    .eyeHeight(0.665F)
                    .clientTrackingRange(10)
                    .build(STRAWBERRY_CALF_KEY)
    );

    public static void registerModEntities() {
        FabricDefaultAttributeRegistry.register(STRAWBERRY_COW, Cow.createAttributes());
        FabricDefaultAttributeRegistry.register(STRAWBERRY_CALF, Cow.createAttributes());
    }
}
