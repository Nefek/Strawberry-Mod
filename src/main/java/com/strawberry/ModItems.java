package com.strawberry;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import com.strawberry.item.StrawberryCowSpawnEggItem;
import com.strawberry.item.StrawberryMilkBucketItem;

public class ModItems {

    public static final ResourceKey<Item> STRAWBERRY_KEY = ResourceKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(StrawberryMod.MOD_ID, "strawberry")
    );

    /**
     * Клубника: еда (2 голода) и «семечко» для куста —
     * правый клик по земле сажает куст (см. {@link StrawberryItem}).
     */
    public static final Item STRAWBERRY = Registry.register(
            BuiltInRegistries.ITEM,
            STRAWBERRY_KEY.location(),
            new StrawberryItem(
                    new Item.Properties()
                            .setId(STRAWBERRY_KEY)
                            .food(
                                    new FoodProperties.Builder()
                                            .nutrition(2)
                                            .saturationModifier(0.3F)
                                            .build(),

                                    Consumable.builder()
                                            .consumeSeconds(0.8F)
                                            .build()
                            )
            )
    );

    public static final ResourceKey<Item> STRAWBERRY_JAM_KEY = ResourceKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(StrawberryMod.MOD_ID, "strawberry_jam")
    );

    /**
     * Клубничное варенье — характеристики как у тыквенного пирога:
     * nutrition = 8, saturationModifier = 0.3F.
     */
    public static final Item STRAWBERRY_JAM = Registry.register(
            BuiltInRegistries.ITEM,
            STRAWBERRY_JAM_KEY.location(),
            new Item(
                    new Item.Properties()
                            .setId(STRAWBERRY_JAM_KEY)
                            .stacksTo(16)
                            .food(
                                    new FoodProperties.Builder()
                                            .nutrition(8)
                                            .saturationModifier(0.3F)
                                            .build(),

                                    Consumable.builder()
                                            .consumeSeconds(1.6F)
                                            .build()
                            )
            )
    );

    public static final ResourceKey<Item> STRAWBERRY_MILK_BUCKET_KEY = ResourceKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(StrawberryMod.MOD_ID, "strawberry_milk_bucket")
    );

    public static final Item STRAWBERRY_MILK_BUCKET = Registry.register(
            BuiltInRegistries.ITEM,
            STRAWBERRY_MILK_BUCKET_KEY.location(),
            new StrawberryMilkBucketItem(
                    new Item.Properties()
                            .setId(STRAWBERRY_MILK_BUCKET_KEY)
                            .craftRemainder(Items.BUCKET)
                            .stacksTo(1)
                            // Ванильная настройка питья: звук GENERIC_DRINK во время
                            // использования, 32 тика и анимация DRINK.
                            .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
            )
    );

    public static final ResourceKey<Item> STRAWBERRY_COW_SPAWN_EGG_KEY = ResourceKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(StrawberryMod.MOD_ID, "strawberry_cow_spawn_egg")
    );

    public static final Item STRAWBERRY_COW_SPAWN_EGG = Registry.register(
            BuiltInRegistries.ITEM,
            STRAWBERRY_COW_SPAWN_EGG_KEY.location(),
            new StrawberryCowSpawnEggItem(
                    new Item.Properties()
                            .setId(STRAWBERRY_COW_SPAWN_EGG_KEY)
            )
    );

    public static void registerModItems() {
        StrawberryMod.LOGGER.info("Registering Mod Items");
    }
}
