package de.verdox.mccreativelab.behaviour.block;

import de.verdox.mccreativelab.ItemInteractionResult;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.CraftRayTraceResult;
import org.bukkit.inventory.EquipmentSlot;
import java.util.function.Supplier;

/** Bridges the former separate item interaction result to the current unified native result. */
public final class BlockInteractionHooks {
    private BlockInteractionHooks() {}
    public static net.minecraft.world.InteractionResult use(BlockState state, Level level, Player player, InteractionHand hand, BlockHitResult hit, Supplier<net.minecraft.world.InteractionResult> vanilla) {
        var behaviour = BehaviorBlockState.getBehaviour(state);
        var block = CraftBlock.at(level, hit.getBlockPos());
        var bukkitPlayer = (org.bukkit.entity.Player) player.getBukkitEntity();
        var slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
        var ray = CraftRayTraceResult.convertFromInternal(level, hit);
        var result = BehaviourUtil.evaluate(behaviour, value -> value.use(block, bukkitPlayer, slot, ray), vanilla,
            new BehaviourUtil.Converter<ItemInteractionResult, net.minecraft.world.InteractionResult>() {
                public net.minecraft.world.InteractionResult bukkitToNMS(ItemInteractionResult value) {
                    return switch (value) {
                        case SUCCESS -> net.minecraft.world.InteractionResult.SUCCESS;
                        case CONSUME -> net.minecraft.world.InteractionResult.CONSUME;
                        case CONSUME_PARTIAL -> net.minecraft.world.InteractionResult.CONSUME.withoutItem();
                        case PASS_TO_DEFAULT_BLOCK_INTERACTION -> net.minecraft.world.InteractionResult.TRY_WITH_EMPTY_HAND;
                        case SKIP_DEFAULT_BLOCK_INTERACTION -> net.minecraft.world.InteractionResult.PASS;
                        case FAIL -> net.minecraft.world.InteractionResult.FAIL;
                    };
                }
                public ItemInteractionResult nmsToBukkitValue(net.minecraft.world.InteractionResult value) {
                    if (value == net.minecraft.world.InteractionResult.TRY_WITH_EMPTY_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                    if (value == net.minecraft.world.InteractionResult.FAIL) return ItemInteractionResult.FAIL;
                    if (value.consumesAction()) return value instanceof net.minecraft.world.InteractionResult.Success success && success.swingSource() == net.minecraft.world.InteractionResult.SwingSource.NONE ? ItemInteractionResult.CONSUME : ItemInteractionResult.SUCCESS;
                    return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                }
            });
        BehaviourUtil.evaluateCallback(behaviour, value -> value.onUseCallback(block, bukkitPlayer, slot, ray,
                BehaviourUtil.Converter.InteractionResult.INSTANCE.nmsToBukkitValue(result)), () -> {});
        return result;
    }
}
