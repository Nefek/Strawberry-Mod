package com.strawberry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Куст клубники — аналог ванильного куста сладких ягод.
 *
 * Стадии роста: age = 0 (росток), 1 (молодой куст), 2 (цветущий куст),
 * 3 (взрослый, с ягодами). Куст растёт сам на свету (random tick),
 * ускоряется костной мукой, а собрать ягоды можно только на последней стадии.
 */
public class StrawberryBushBlock extends VegetationBlock implements BonemealableBlock {

    public static final MapCodec<StrawberryBushBlock> CODEC = simpleCodec(StrawberryBushBlock::new);

    /** Максимальная стадия роста. */
    public static final int MAX_AGE = 3;

    /** Стадия роста 0..3. */
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final VoxelShape SAPLING_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
    private static final VoxelShape GROWING_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public StrawberryBushBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public MapCodec<StrawberryBushBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AGE)) {
            case 0 -> SAPLING_SHAPE;
            case 1 -> GROWING_SHAPE;
            default -> Shapes.block();
        };
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    /**
     * Рост на случайном тике — ровно как у ванильного куста сладких ягод:
     * шанс 1 к 5 и только если сверху достаточно света (9+).
     */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE && random.nextInt(5) == 0 && level.getRawBrightness(pos.above(), 0) >= 9) {
            level.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
        }
    }

    /** Сбор урожая правым кликом пустой рукой. */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(AGE) < MAX_AGE) {
            return super.useWithoutItem(state, level, pos, player, hit);
        }
        return this.harvest(state, level, pos);
    }

    /** Сбор урожая правым кликом любым предметом (только со взрослого куста). */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(AGE) < MAX_AGE) {
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        }
        return this.harvest(state, level, pos);
    }

    /**
     * Собрать ягоды: 1-3 клубники, куст возвращается на стадию 1,
     * чтобы мог вырасти снова.
     */
    private InteractionResult harvest(BlockState state, Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            int count = 1 + serverLevel.getRandom().nextInt(3);
            Block.popResource(serverLevel, pos, new ItemStack(ModItems.STRAWBERRY, count));
            serverLevel.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
                    1.0F, 0.8F + serverLevel.getRandom().nextFloat() * 0.4F);
            serverLevel.setBlockAndUpdate(pos, state.setValue(AGE, 1));
        }
        return InteractionResult.SUCCESS;
    }

    /** Средний клик по кусту в творческом режиме даёт клубнику, а не сам куст. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(ModItems.STRAWBERRY);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    // ----- костная мука -----

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        int nextAge = Math.min(MAX_AGE, age + 1);
        level.setBlockAndUpdate(pos, state.setValue(AGE, nextAge));
    }
}
