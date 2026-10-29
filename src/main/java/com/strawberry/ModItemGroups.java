package com.strawberry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * Собственная вкладка мода в творческом инвентаре.
 *
 * Раньше предметы добавлялись в ванильные вкладки («Естественные блоки»,
 * «Еда и напитки») через ItemGroupEvents. Теперь у мода своя вкладка,
 * а в ванильных его предметов нет.
 *
 * FabricItemGroup.builder() — из fabric-item-group-api-v1, он создаёт
 * обычный CreativeModeTab.Builder.
 */
public final class ModItemGroups {

    public static final ResourceKey<CreativeModeTab> STRAWBERRY_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(StrawberryMod.MOD_ID, "strawberry")
    );

    public static final CreativeModeTab STRAWBERRY_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            STRAWBERRY_TAB_KEY,
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.strawberry-mod.strawberry"))
                    .icon(() -> new ItemStack(ModItems.STRAWBERRY))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.STRAWBERRY);
                        output.accept(ModBlocks.STRAWBERRY_BUSH_ITEM);
                        output.accept(ModItems.STRAWBERRY_JAM);
                        output.accept(ModItems.STRAWBERRY_MILK_BUCKET);
                        output.accept(ModItems.STRAWBERRY_COW_SPAWN_EGG);
                    })
                    .build()
    );

    private ModItemGroups() {
    }

    public static void register() {
        StrawberryMod.LOGGER.info("Registering Mod Item Groups");
    }
}
