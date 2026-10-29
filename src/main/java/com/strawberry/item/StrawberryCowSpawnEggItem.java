package com.strawberry.item;

import com.strawberry.entity.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class StrawberryCowSpawnEggItem extends Item {
    public StrawberryCowSpawnEggItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        ServerLevel level = (ServerLevel) context.getLevel();
        Vec3 pos = context.getClickLocation();
        var cow = ModEntities.STRAWBERRY_COW.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        if (cow != null) {
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;
            if (context.getClickedFace() == Direction.UP) {
                y = context.getClickedPos().getY() + 1.0;
            }
            cow.snapTo(x, y, z);
            level.addFreshEntity(cow);
            ItemStack stack = context.getItemInHand();
            if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
