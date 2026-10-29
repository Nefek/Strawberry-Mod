package com.strawberry.entity;

import com.strawberry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StrawberryCowEntity extends Cow {
    public StrawberryCowEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
        this.setAge(0);
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
        StrawberryCalfEntity calf = ModEntities.STRAWBERRY_CALF.create(serverLevel, net.minecraft.world.entity.EntitySpawnReason.BREEDING);
        if (calf != null) {
            calf.setBaby(true);
        }
        return calf;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

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

        if (itemStack.is(ModItems.STRAWBERRY_COW_SPAWN_EGG)) {
            if (this.level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            // Яйцо призыва на телёнке — мгновенно выращивает его.
            if (this.isBaby()) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.setAge(0);
                return InteractionResult.CONSUME;
            }

            // Яйцо призыва на взрослой корове — появляется клубничный телёнок,
            // как у ванильных животных (см. Mob#checkAndHandleImportantInteractions).
            if (!(this.level() instanceof ServerLevel serverLevel)) {
                return InteractionResult.PASS;
            }

            Cow offspring = this.getBreedOffspring(serverLevel, this);
            if (offspring == null) {
                return InteractionResult.PASS;
            }

            offspring.setBaby(true);
            offspring.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            serverLevel.addFreshEntity(offspring);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }
}
