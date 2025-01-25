package de.verdox.mccreativelab.behaviour;

import de.verdox.mccreativelab.random.CraftVanillaRandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Locale;
import java.util.function.Supplier;

public class BlockBehaviourUtil extends BehaviourUtil{
    public static BlockBehaviourUtil INSTANCE;
    public static BlockBehaviourUtil getInstance() {
        if(INSTANCE == null)
            INSTANCE = new BlockBehaviourUtil();
        return INSTANCE;
    }

    public boolean isVanillaRandomTickReplaced(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        return runIfVanillaLogicReplaced(BlockBehaviour.BLOCK_BEHAVIOUR, BlockBehaviourUtil.getMaterial(state), blockBehaviour -> blockBehaviour.randomTick(world
            .getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), new CraftVanillaRandomSource(random)));
    }

    public boolean isStepOnLogicReplaced(Level world, BlockPos pos, BlockState state, Entity entity) {
        return runIfVanillaLogicReplaced(BlockBehaviour.BLOCK_BEHAVIOUR, BlockBehaviourUtil.getMaterial(state), blockBehaviour -> blockBehaviour.stepOn(world
            .getWorld()
            .getBlockAt(pos.getX(), pos.getY(), pos.getZ()), state.createCraftBlockData(), entity.getBukkitEntity()));
    }

    public boolean isVanillaTickReplaced(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        return runIfVanillaLogicReplaced(BlockBehaviour.BLOCK_BEHAVIOUR, BlockBehaviourUtil.getMaterial(state), blockBehaviour -> blockBehaviour.tick(world
            .getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), new CraftVanillaRandomSource(random)));
    }

    public boolean isVanillaBlockAttackReplaced(BlockState state, Level world, BlockPos pos, Player player){
        return runIfVanillaLogicReplaced(BlockBehaviour.BLOCK_BEHAVIOUR, BlockBehaviourUtil.getMaterial(state), blockBehaviour ->
            blockBehaviour.attack(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), (org.bukkit.entity.Player) player.getBukkitEntity()));
    }

    public boolean isVanillaNeighbourBlockUpdateReplaced(BlockState state, Level world, BlockPos pos, net.minecraft.world.level.block.Block sourceBlock, BlockPos sourcePos, boolean notify){
        return runIfVanillaLogicReplaced(BlockBehaviour.BLOCK_BEHAVIOUR, BlockBehaviourUtil.getMaterial(state), blockBehaviour ->
            blockBehaviour.onNeighbourBlockUpdate(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), world.getWorld().getBlockAt(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ()), notify));
    }

    public boolean isRandomlyTicking(BlockState blockState, boolean vanillaValue) {
        return BlockBehaviourUtil.evaluateBoolean(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(blockState), blockBehaviour -> blockBehaviour.isBlockDataRandomlyTicking(blockState.createCraftBlockData()), () -> vanillaValue);
    }

    public boolean isRandomlyTicking(Level world, int x, int y, int z, BlockState blockState, boolean vanillaValue) {
        return BlockBehaviourUtil.evaluateBoolean(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(blockState), blockBehaviour -> blockBehaviour.isBlockRandomlyTicking(world
            .getWorld().getBlockAt(x, y, z), blockState.createCraftBlockData()), () -> vanillaValue);
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos, Supplier<Boolean> vanillaLogic) {
        return BlockBehaviourUtil.evaluateBoolean(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(state), blockBehaviour -> {
            if (!(world instanceof ServerLevel serverLevel))
                return BehaviourResult.Bool.DEFAULT_INSTANCE;
            return blockBehaviour.canSurvive(serverLevel.getWorld()
                                                        .getBlockAt(pos.getX(), pos.getY(), pos.getZ()), serverLevel.getWorld());
        }, vanillaLogic);
    }

    public ItemInteractionResult useItemOn(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, Supplier<ItemInteractionResult> vanillaLogic){
        return BlockBehaviourUtil.evaluate(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(state), blockBehaviour -> {
            Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
            BlockFace blockFace = toBukkitBlockFace(hit.getDirection());
            EquipmentSlot equipmentSlot = hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
            RayTraceResult rayTraceResult = new RayTraceResult(new Vector(hit.getLocation().x(), hit.getLocation()
                                                                                                    .y(), hit
                .getLocation().z()), block, blockFace);
            return blockBehaviour.use(block, (org.bukkit.entity.Player) player.getBukkitEntity(), equipmentSlot, rayTraceResult);
        }, vanillaLogic, Converter.ItemInteractionResult.INSTANCE);
    }

    public void onUseCallback(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, InteractionResult interactionResult) {
        BlockBehaviourUtil.evaluateCallback(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(state), blockBehaviour -> {
            Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
            BlockFace blockFace = toBukkitBlockFace(hit.getDirection());
            EquipmentSlot equipmentSlot = hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
            RayTraceResult rayTraceResult = new RayTraceResult(new Vector(hit.getLocation().x(), hit.getLocation().y(), hit.getLocation().z()), block, blockFace);
            return blockBehaviour.onUseCallback(block, (org.bukkit.entity.Player) player.getBukkitEntity(), equipmentSlot, rayTraceResult, Converter.InteractionResult.INSTANCE.nmsToBukkitValue(interactionResult));
        });
    }

    public void onPlaceCallback(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify, boolean isProcessingBlockPlaceEvent) {
        BlockBehaviourUtil.evaluateCallback(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(state), blockBehaviour ->
            blockBehaviour.onPlace(new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ()), state.createCraftBlockData(), oldState.createCraftBlockData(), notify, isProcessingBlockPlaceEvent));
    }

    public void onPlayerPlaceCallback(Player player, ItemStack stackUsedToPlaceBlock, BlockState placedState, Level world, BlockPos pos) {
        BlockBehaviourUtil.evaluateCallback(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(placedState), blockBehaviour ->
            blockBehaviour.onPlayerPlace((org.bukkit.entity.Player) player.getBukkitEntity(), stackUsedToPlaceBlock.asBukkitCopy(), new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ()), placedState.createCraftBlockData()));
    }

    public void onPlayerBreakCallback(Player player, BlockState brokenState, Level world, BlockPos pos) {
        BlockBehaviourUtil.evaluateCallback(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(brokenState), blockBehaviour ->
            blockBehaviour.onPlayerBreak((org.bukkit.entity.Player) player.getBukkitEntity(), new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ()), brokenState.createCraftBlockData()));
    }

    public float getExplosionResistance(BlockGetter world, BlockPos pos, BlockState blockState, float vanillaValue) {
        return BlockBehaviourUtil
            .evaluate(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(blockState), blockBehaviour -> {
                if (!(world instanceof ServerLevel serverLevel))
                    return FLOAT_DEFAULT;
                Block block = ((ServerLevel) world).getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());

                return blockBehaviour.getExplosionResistance(block, blockState.createCraftBlockData());
            }, () -> vanillaValue, Converter.DummyConverter.getInstance(Float.class));
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos, Supplier<BlockState> vanillaLogic) {
        return BlockBehaviourUtil.evaluate(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(state), blockBehaviour -> {
                if (!(world instanceof ServerLevel serverLevel))
                    return BehaviourResult.Object.DEFAULT_INSTANCE;
                BlockFace blockFace = toBukkitBlockFace(direction);
                return blockBehaviour.blockUpdate(new Location(serverLevel.getWorld(), pos.getX(), pos.getY(), pos.getZ()), state.createCraftBlockData(), blockFace, neighborState.createCraftBlockData(), new Location(serverLevel.getWorld(), neighborPos.getX(), neighborPos.getY(), neighborPos.getZ()));
            }, vanillaLogic, Converter.BlockData.INSTANCE);
    }

    public void onRemoveCallback(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        BlockBehaviourUtil.evaluateCallback(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(state), blockBehaviour ->
            blockBehaviour.onRemove(new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ()), newState.createCraftBlockData(), state.createCraftBlockData(), moved));
    }

    public void onDestroyCallback(BlockState state, Level world, BlockPos pos, boolean drop, @javax.annotation.Nullable Entity breakingEntity, int maxUpdateDepth) {
        BlockBehaviourUtil.evaluateCallback(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(state), blockBehaviour ->
            blockBehaviour.onDestroy(new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ()), drop, breakingEntity != null ? breakingEntity.getBukkitEntity() : null, maxUpdateDepth));
    }

    public boolean growCrop(ItemStack stack, Level world, BlockPos pos, Supplier<Boolean> vanillaLogic) {
        BlockState state = world.getBlockState(pos);
        return evaluateBoolean(BlockBehaviour.BLOCK_BEHAVIOUR,
            getMaterial(state),
            blockBehaviour -> {
                world.captureTreeGeneration = false;
                world.captureBlockStates = false;
                BehaviourResult.Bool result = blockBehaviour.fertilizeAction(world.getWorld()
                                                    .getBlockAt(pos.getX(), pos.getY(), pos.getZ()), stack.getBukkitStack());
                world.capturedBlockStates.clear(); // We do this to prevent bukkit logic since it kills our custom logic
                return result;
            },
            vanillaLogic
            );
    }

    public void onPistonMove(BlockState movedState, Level world, BlockPos positionBeforeMove, BlockPos positionAfterMove, BlockPos positionOfPiston, Direction moveDirection) {
        BlockBehaviourUtil.evaluateCallback(BlockBehaviour.BLOCK_BEHAVIOUR, getMaterial(movedState), blockBehaviour -> {

            BlockData movedBlockData = movedState.createCraftBlockData();
            Location posBeforeMove = new Location(world.getWorld(), positionBeforeMove.getX(), positionBeforeMove.getY(), positionBeforeMove.getZ());
            Location posAfterMove = new Location(world.getWorld(), positionAfterMove.getX(), positionAfterMove.getY(), positionAfterMove.getZ());
            Block piston = world.getWorld().getBlockAt(positionOfPiston.getX(), positionOfPiston.getY(), positionOfPiston.getZ());
            Vector dir = new Vector(moveDirection.getStepX(), moveDirection.getStepY(), moveDirection.getStepZ());


            return blockBehaviour.onPistonMoveBlock(movedBlockData, posBeforeMove, posAfterMove, piston, dir);
        });
    }

    private static BlockFace toBukkitBlockFace(Direction direction) {
        return switch (direction) {
            case DOWN -> BlockFace.DOWN;
            case UP -> BlockFace.UP;
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
        };
    }



    private static Material getMaterial(BlockState blockState) {
        Material material;
        if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isReady())
            material = blockState.getBukkitMaterial();
        else
            material = Material.getMaterial(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).getPath()
                                                                   .toUpperCase(Locale.ROOT));
        return material;
    }
}
