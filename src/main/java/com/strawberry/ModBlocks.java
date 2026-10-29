package com.strawberry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
    public static final ResourceKey<Block> STRAWBERRY_BUSH_KEY = ResourceKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(
                    StrawberryMod.MOD_ID,
                    "strawberry_bush"
            )
    );

    public static final Block STRAWBERRY_BUSH = Registry.register(
            BuiltInRegistries.BLOCK,
            STRAWBERRY_BUSH_KEY.location(),
            new StrawberryBushBlock(
                    BlockBehaviour.Properties.of()
                            .setId(STRAWBERRY_BUSH_KEY)
                            // как у обычной травы: без коллизии, не перекрывает свет,
                            // ломается мгновенно, звук травы
                            .noCollision()
                            .noOcclusion()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            // куст должен получать случайные тики, чтобы расти
                            .randomTicks()
                            // поршни просто ломают куст, а не толкают его
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final ResourceKey<Item> STRAWBERRY_BUSH_ITEM_KEY = ResourceKey.create(
            Registries.ITEM,
            STRAWBERRY_BUSH_KEY.location()
    );

    public static final Item STRAWBERRY_BUSH_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            STRAWBERRY_BUSH_KEY.location(),
            new BlockItem(
                    STRAWBERRY_BUSH,
                    new Item.Properties()
                            .setId(STRAWBERRY_BUSH_ITEM_KEY)
            )
    );

    public static void registerModBlocks() {
        StrawberryMod.LOGGER.info("Registering Mod Blocks");
    }
}
