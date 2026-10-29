package com.strawberry.mixin;

import com.strawberry.ModItems;
import com.strawberry.entity.ModEntities;
import com.strawberry.entity.StrawberryCalfEntity;
import com.strawberry.entity.StrawberryCowEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Кормление обычной коровы клубникой.
 *
 * <p>Важно: метод обрабатывается и на клиенте. Если на клиенте вернуть PASS,
 * Minecraft посчитает, что взаимодействие с сущностью не удалось, и начнёт
 * использовать клубнику как еду — отсюда анимация поедания у игрока.
 * Поэтому на клиенте мы тоже возвращаем SUCCESS, но ничего не изменяем:
 * количество предмета и превращение коровы выполняет только сервер.</p>
 */
@Mixin(Animal.class)
public class AnimalFoodMixin {
	private static final Map<UUID, Integer> STRAWBERRY_FEED_COUNT = new HashMap<>();

	private static final int STRAWBERRIES_TO_CONVERT = 10;

	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void strawberry$feedStrawberry(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		Animal animal = (Animal) (Object) this;

		if (!(animal instanceof Cow cow) || cow.getType() != EntityType.COW) {
			return;
		}
		// Клубничных коров и телят обрабатывает обычная логика Animal/Cow.
		if (animal instanceof StrawberryCowEntity || animal instanceof StrawberryCalfEntity) {
			return;
		}

		ItemStack itemStack = player.getItemInHand(hand);
		if (!itemStack.is(ModItems.STRAWBERRY)) {
			return;
		}

		// Клиент должен «поглотить» клик, иначе запустится анимация поедания.
		if (animal.level().isClientSide()) {
			cir.setReturnValue(InteractionResult.SUCCESS);
			return;
		}

		if (!(animal.level() instanceof ServerLevel serverLevel)) {
			return;
		}

		if (!player.getAbilities().instabuild) {
			itemStack.shrink(1);
		}

		UUID uuid = cow.getUUID();
		int count = STRAWBERRY_FEED_COUNT.getOrDefault(uuid, 0) + 1;

		if (count < STRAWBERRIES_TO_CONVERT) {
			STRAWBERRY_FEED_COUNT.put(uuid, count);
			serverLevel.sendParticles(
					ParticleTypes.HAPPY_VILLAGER,
					cow.getX(), cow.getY(0.5), cow.getZ(),
					4, 0.4, 0.4, 0.4, 0.0
			);
			cir.setReturnValue(InteractionResult.SUCCESS);
			return;
		}

		STRAWBERRY_FEED_COUNT.remove(uuid);
		serverLevel.sendParticles(
				ParticleTypes.HEART,
				cow.getX(), cow.getY(0.5), cow.getZ(),
				7, 0.5, 0.5, 0.5, 0.0
		);

		if (cow.isBaby()) {
			StrawberryCalfEntity calf = ModEntities.STRAWBERRY_CALF.create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
			if (calf != null) {
				strawberry$copyEntityData(cow, calf);
				calf.setBaby(true);
				// Возраст может быть уже близок к 0 — тогда телёнок вырос бы почти сразу.
				// Отрицательное значение гарантированно оставляет его малышом.
				calf.setAge(cow.getAge() < 0 ? cow.getAge() : -24000);
				cow.discard();
				serverLevel.addFreshEntity(calf);
			}
		} else {
			StrawberryCowEntity strawberryCow = ModEntities.STRAWBERRY_COW.create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
			if (strawberryCow != null) {
				strawberry$copyEntityData(cow, strawberryCow);
				strawberryCow.setBaby(false);
				strawberryCow.setAge(0);
				cow.discard();
				serverLevel.addFreshEntity(strawberryCow);
			}
		}

		cir.setReturnValue(InteractionResult.SUCCESS);
	}

	@Unique
	private static void strawberry$copyEntityData(Cow from, Cow to) {
		to.snapTo(from.getX(), from.getY(), from.getZ(), from.getYRot(), from.getXRot());
		to.setDeltaMovement(from.getDeltaMovement());
		to.setHealth(from.getHealth());
		to.setNoAi(from.isNoAi());

		if (from.isPersistenceRequired()) {
			to.setPersistenceRequired();
		}
		if (from.hasCustomName()) {
			to.setCustomName(from.getCustomName());
			to.setCustomNameVisible(from.isCustomNameVisible());
		}
	}
}
