package com.strawberry.entity;

import com.strawberry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StrawberryCalfEntity extends Cow {
    public StrawberryCalfEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
        this.setBaby(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof TemptGoal);
        this.goalSelector.addGoal(3, new TemptGoal(
                this,
                1.25,
                stack -> stack.is(ModItems.STRAWBERRY),
                false
        ));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModItems.STRAWBERRY);
    }

    @Override
    public Cow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        StrawberryCalfEntity calf = ModEntities.STRAWBERRY_CALF.create(serverLevel, EntitySpawnReason.BREEDING);
        if (calf != null) {
            calf.setBaby(true);
        }
        return calf;
    }

    /**
     * Вызывается, когда возраст пересекает границу детёныш/взрослый.
     * Телёнок не должен оставаться сущностью типа {@code strawberry_calf}
     * после взросления — иначе у него останутся маленькие размеры.
     */
    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby()) {
            this.strawberry$growUp();
        }
    }

    /**
     * Заменяет телёнка на взрослую клубничную корову.
     *
     * @return {@code true}, если замена действительно произошла
     */
    private boolean strawberry$growUp() {
        if (this.isRemoved() || !(this.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        StrawberryCowEntity adult = ModEntities.STRAWBERRY_COW.create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
        if (adult == null) {
            return false;
        }

        adult.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        adult.setDeltaMovement(this.getDeltaMovement());
        adult.setBaby(false);
        adult.setAge(0);
        adult.setHealth(this.getHealth());
        adult.setNoAi(this.isNoAi());

        if (this.isPersistenceRequired()) {
            adult.setPersistenceRequired();
        }
        if (this.hasCustomName()) {
            adult.setCustomName(this.getCustomName());
            adult.setCustomNameVisible(this.isCustomNameVisible());
        }

        this.discard();
        serverLevel.addFreshEntity(adult);
        return true;
    }

    /**
     * Страховка для старых сохранений: если сущность {@code strawberry_calf}
     * уже взрослая, заменяем её на обычную взрослую клубничную корову.
     * {@link #strawberry$growUp()} сам проверяет {@code isRemoved()},
     * поэтому повторного спавна не будет.
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isBaby()) {
            this.strawberry$growUp();
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Малышей доить нельзя — как и у обычной коровы.
        if (itemStack.is(Items.BUCKET) && !this.isBaby()) {
            if (this.level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack strawberryMilk = new ItemStack(ModItems.STRAWBERRY_MILK_BUCKET);
            ItemStack result = ItemUtils.createFilledResult(itemStack, player, strawberryMilk);
            player.setItemInHand(hand, result);
            return InteractionResult.CONSUME;
        }

        // Клубничное яйцо призыва мгновенно выращивает телёнка.
        if (itemStack.is(ModItems.STRAWBERRY_COW_SPAWN_EGG)) {
            if (this.level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            if (!this.strawberry$growUp()) {
                return InteractionResult.PASS;
            }

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }
}
