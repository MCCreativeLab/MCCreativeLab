package de.verdox.mccreativelab.behaviour.block;

import de.verdox.mccreativelab.behavior.BlockBehaviour;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import de.verdox.mccreativelab.random.CraftVanillaRandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.Location;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftExplosionResult;
import org.bukkit.craftbukkit.CraftPrecipitation;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftProjectile;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftLocation;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public interface BehaviorBlockState extends BehaviourUtil {
    default void mcc$tick(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.tick(CraftBlock.at(level, pos), new CraftVanillaRandomSource(random)),
                () -> asBlock().tick(level, pos, random));
    }

    default void mcc$randomTick(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.randomTick(CraftBlock.at(level, pos), new CraftVanillaRandomSource(random)),
                () -> asBlock().randomTick(level, pos, random));
    }

    default void mcc$onExplosionHit(ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.onExplosionHit(CraftBlock.at(level, pos), CraftExplosionResult.toBukkit(explosion.getBlockInteraction()), new BiConsumer<org.bukkit.inventory.ItemStack, Location>() {
                    @Override
                    public void accept(org.bukkit.inventory.ItemStack itemStack, Location location) {
                        dropConsumer.accept(CraftItemStack.unwrap(itemStack), CraftLocation.toBlockPosition(location));
                    }
                }),
                () -> asBlock().onExplosionHit(level, pos, explosion, dropConsumer));
    }

    default void mcc$onRemove(Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.onRemove(CraftLocation.toBukkit(pos, level), CraftBlockData.fromData(newState), CraftBlockData.fromData(asBlock()), movedByPiston),
                () -> asBlock().onRemove(level, pos, newState, movedByPiston));
    }

    default void mcc$onPlace(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, @Nullable net.minecraft.world.item.context.UseOnContext context) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.onPlace(CraftLocation.toBukkit(pos, level), CraftBlockData.fromData(asBlock()), CraftBlockData.fromData(oldState), movedByPiston),
                () -> asBlock().onPlace(level, pos, oldState, movedByPiston, context));
    }

    default void mcc$wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.wasExploded(CraftBlock.at(level, pos), CraftExplosionResult.toBukkit(explosion.getBlockInteraction())),
                () -> asBlock().getBlock().wasExploded(level, pos, explosion));
    }

    default void mcc$stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.stepOn(CraftBlock.at(level, pos), CraftBlockData.fromData(state), entity.getBukkitEntity()),
                () -> asBlock().getBlock().stepOn(level, pos, state, entity));
    }

    default void mcc$fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.fallOn(CraftBlock.at(level, pos), CraftBlockData.fromData(state), entity.getBukkitEntity(), fallDistance),
                () -> asBlock().getBlock().fallOn(level, state, pos, entity, fallDistance));
    }

    default void mcc$handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.handlePrecipitation(CraftBlock.at(level, pos), CraftBlockData.fromData(state), CraftPrecipitation.toBukkit(precipitation)),
                () -> asBlock().getBlock().handlePrecipitation(state, level, pos, precipitation));
    }

    default void mcc$spawnAfterBreak(ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.spawnAfterBreak(CraftBlock.at(level, pos), stack.asBukkitMirror(), dropExperience),
                () -> asBlock().spawnAfterBreak(level, pos, stack, dropExperience));
    }

    default void mcc$attack(Level level, BlockPos pos, Player player) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour,
                blockBehaviour -> blockBehaviour.attack(CraftBlock.at(level, pos), (org.bukkit.entity.Player) player.getBukkitEntity()),
                () -> asBlock().attack(level, pos, player));
    }

    // We removed the canSurvive method from the handle to implement it here
    default boolean canSurvive(LevelReader level, BlockPos pos) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        return BehaviourUtil.evaluateBoolean(behaviour,
                blockBehaviour -> blockBehaviour.canSurvive(CraftBlock.at((LevelAccessor) level, pos)),
                () -> asBlock().old$canSurvive(level, pos));
    }

    //TODO:
    // - getMenuProvider
    // - getTicker
    // - getPistonPushReaction

/*    default MenuProvider mcc$getMenuProvider(Level level, BlockPos pos) {

    }

    default <T extends BlockEntity> BlockEntityTicker<T> mcc$getTicker(Level level, BlockEntityType<T> blockEntityType) {

    }*/

    default boolean mcc$isRandomlyTicking(Level level, int globalX, int globalY, int globalZ, BlockState blockState) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        return BehaviourUtil.evaluateBoolean(behaviour, blockBehaviour -> blockBehaviour.isBlockRandomlyTicking(CraftBlock.at(level, new BlockPos(globalX, globalY, globalZ)), CraftBlockData.fromData(blockState)), () -> asBlock().isRandomlyTicking());
    }

    default void mcc$onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockBehaviour behaviour = getBehaviour(asBlock());
        BehaviourUtil.evaluateVoid(behaviour, blockBehaviour -> blockBehaviour.onProjectileHit(CraftBlock.at(level, hit.getBlockPos()), (org.bukkit.entity.Projectile) CraftProjectile.getEntity(MinecraftServer.getServer().server, projectile)), () -> asBlock().onProjectileHit(level, state, hit, projectile));
    }

    default void handleNeighborChanged(Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {

    }

    default boolean mcc$growCrop(ItemStack stack, Level level, BlockPos pos) {
        //TODO: BoneMealItem
        return false;
    }

    default void mcc$onPistonMove(BlockState movedState, Level world, BlockPos positionBeforeMove, BlockPos positionAfterMove, BlockPos positionOfPiston, Direction moveDirection) {
        //TODO: PistonBaseBlock
    }

    @org.jetbrains.annotations.Nullable
    public static <V extends BlockState> BlockBehaviour getBehaviour(V nmsBlock) {
        BlockType blockType = CraftBlockType.minecraftToBukkitNew(nmsBlock.getBlock());
        BlockData blockData = CraftBlockData.fromData(nmsBlock);

        if (BlockBehaviour.BLOCK_BEHAVIOUR_SPECIFIC.isImplemented(blockData)) {
            return BlockBehaviour.BLOCK_BEHAVIOUR_SPECIFIC.getBehaviour(blockData);
        }
        else if (BlockBehaviour.BLOCK_BEHAVIOUR.isImplemented(blockType)) {
            return BlockBehaviour.BLOCK_BEHAVIOUR.getBehaviour(blockType);
        }
        return null;
    }

    default BlockState asBlock() {
        return (BlockState) this;
    }
}
