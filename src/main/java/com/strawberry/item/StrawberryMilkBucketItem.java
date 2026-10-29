package com.strawberry.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StrawberryMilkBucketItem extends Item {
    public StrawberryMilkBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public net.minecraft.world.InteractionResult use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        player.startUsingItem(hand);
        return net.minecraft.world.InteractionResult.CONSUME;
    }

    @Override
    public ItemStack finishUsingItem(
            ItemStack itemStack,
            Level level,
            LivingEntity livingEntity
    ) {
        if (!level.isClientSide()) {
            livingEntity.addEffect(
                    new MobEffectInstance(MobEffects.REGENERATION, 300, 0)
            );
        }

        if (livingEntity instanceof Player player) {
            return ItemUtils.createFilledResult(
                    itemStack,
                    player,
                    new ItemStack(Items.BUCKET)
            );
        }

        itemStack.shrink(1);

        if (itemStack.isEmpty()) {
            return new ItemStack(Items.BUCKET);
        }

        return itemStack;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.DRINK;
    }
}