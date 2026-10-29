package com.strawberry;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Клубника. Это еда (как обычный предмет), но если нажать ею по земле —
 * на этом месте вырастает куст клубники (стадия 0).
 */
public class StrawberryItem extends Item {

    public StrawberryItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        // если кликнули по заменяемому блоку (трава, цветок) — сажаем прямо в него,
        // иначе — на блок сверху (как обычная установка блока)
        BlockPos placePos = clickedState.canBeReplaced() ? clickedPos : clickedPos.above();
        BlockState bushState = ModBlocks.STRAWBERRY_BUSH.defaultBlockState();

        if (!level.getBlockState(placePos).canBeReplaced() || !bushState.canSurvive(level, placePos)) {
            // не земля / нет места — пусть предмет работает как еда
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            level.setBlockAndUpdate(placePos, bushState);
            level.playSound(null, placePos, SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
