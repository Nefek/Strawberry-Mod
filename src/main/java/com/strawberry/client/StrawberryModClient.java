package com.strawberry.client;

import com.strawberry.ModBlocks;
import com.strawberry.StrawberryMod;
import com.strawberry.client.entity.StrawberryCowRenderer;
import com.strawberry.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

/**
 * Клиентская часть мода.
 *
 * В 1.21.9 тип рендера блока в чанках выбирается через
 * ItemBlockRenderTypes.getChunkRenderType(state), и для всех блоков, которых нет
 * в служебной карте, по умолчанию возвращается ChunkSectionLayer.SOLID.
 * Для крестовых блоков с прозрачностью это даёт чёрный фон.
 * BlockRenderLayerMap.putBlock(...) добавляет наш куст со слоем CUTOUT.
 */
public class StrawberryModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Все стадии куста должны рендериться с прозрачностью
        BlockRenderLayerMap.putBlock(ModBlocks.STRAWBERRY_BUSH, ChunkSectionLayer.CUTOUT);

        // Листва куста берёт цвет травы текущего биома: в саванне он теплее,
        // в тайге — холоднее. Ягоды и цветы лежат отдельным слоем и не тонируются.
        ColorProviderRegistry.BLOCK.register(
                (state, level, pos, tintIndex) -> {
                    if (tintIndex != 0) {
                        return 0xFFFFFF;
                    }
                    if (level == null || pos == null) {
                        // Запасной цвет травы равнин, если мира или позиции нет.
                        return 0x91BD59;
                    }
                    return BiomeColors.getAverageGrassColor(level, pos);
                },
                ModBlocks.STRAWBERRY_BUSH
        );

        EntityRendererRegistry.register(ModEntities.STRAWBERRY_COW, StrawberryCowRenderer::new);
        EntityRendererRegistry.register(ModEntities.STRAWBERRY_CALF, StrawberryCowRenderer::new);
        StrawberryMod.LOGGER.info("Registered strawberry bush render layer (cutout) and biome tint");
    }
}
