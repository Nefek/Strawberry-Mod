package com.strawberry;

import com.strawberry.entity.ModEntities;
import com.strawberry.entity.StrawberryCalfEntity;
import com.strawberry.entity.StrawberryCowEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrawberryMod implements ModInitializer {
	public static final String MOD_ID = "strawberry-mod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Map<UUID, Integer> STRAWBERRY_FEED_COUNT = new HashMap<>();

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroups.register();
		ModEntities.registerModEntities();

		// Feeding handled via mixin on Animal.mobInteract

		ResourceKey<PlacedFeature> strawberryBushPlaced = ResourceKey.create(
				Registries.PLACED_FEATURE,
				ResourceLocation.fromNamespaceAndPath(MOD_ID, "patch_strawberry_bush")
		);

		BiomeModifications.addFeature(
				BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.FLOWER_FOREST),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				strawberryBushPlaced
		);

		LOGGER.info("Strawberry bushes registered in Plains and Flower Forest");
	}
}
