package de.verdox.mccreativelab.behaviour;

import de.verdox.mccreativelab.MultiCustomBehaviour;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;

public class BehaviourUtil {
    public static final BehaviourResult.Object<Float> FLOAT_DEFAULT = new BehaviourResult.Object<>(0f, BehaviourResult.Object.Type.USE_VANILLA);

    // ### Standard Implementation
    public static <I, R, T extends BehaviourResult<I, ?>, V> R evaluate(@Nullable V behaviour, @NotNull Function<V, T> logicFunction, Supplier<R> vanillaLogic, @Nullable Converter<I, R> converter) {
        if (behaviour == null)
            return vanillaLogic.get();
        try {
            BehaviourResult<I, ?> behaviourResult = logicFunction.apply(behaviour);
            if (converter == null)
                return vanillaLogic.get();
            var evaluatedValue = behaviourResult.evaluateReturnValue(() -> {
                var vanillaValue = vanillaLogic.get();
                if (vanillaValue == null)
                    return null;
                return converter.nmsToBukkitValue(vanillaValue);
            });
            if (evaluatedValue == null)
                return null;
            return converter.bukkitToNMS(evaluatedValue);
        } catch (Throwable throwable) {
            Bukkit.getLogger()
                  .log(Level.WARNING, "An error occurred while running custom behaviour logic " + behaviour.getClass(), throwable);
            return vanillaLogic.get();
        }
    }

    public static <V> boolean evaluateBoolean(@Nullable V behaviour, @NotNull Function<V, BehaviourResult.Bool> logicFunction, Supplier<Boolean> vanillaLogic){
        return evaluate(behaviour, logicFunction, vanillaLogic, Converter.DummyConverter.getInstance(Boolean.class));
    }

    public static <V> int evaluateInteger(@Nullable V behaviour, @NotNull Function<V, BehaviourResult.Object<Integer>> logicFunction, Supplier<Integer> vanillaLogic){
        return evaluate(behaviour, logicFunction, vanillaLogic, Converter.DummyConverter.getInstance(Integer.class));
    }

    public static <V> void evaluateVoid(@Nullable V behaviour, @NotNull Function<V, BehaviourResult.Void> logicFunction, Runnable vanillaLogic){
        evaluate(behaviour, logicFunction, () -> {
            vanillaLogic.run();
            return null;
        }, Converter.DummyConverter.getInstance(Void.class));
    }

    public static <V> void evaluateCallback(@Nullable V behaviour, @NotNull Function<V, BehaviourResult.Callback> logicFunction, Runnable vanillaLogic){
        evaluate(behaviour, logicFunction, () -> {
            vanillaLogic.run();
            return null;
        }, Converter.DummyConverter.getInstance(Void.class));
    }

    // ### MultiCustomBehaviour
    public static <I, R, T extends BehaviourResult<I, ?>, K, V> R evaluate(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, T> logicFunction, Supplier<R> vanillaLogic, @Nullable Converter<I, R> converter) {
        if (!multiCustomBehaviour.isImplemented(key))
            return vanillaLogic.get();

        V behaviour = multiCustomBehaviour.getBehaviour(key);
        return evaluate(behaviour, logicFunction, vanillaLogic, converter);
    }

    public static <K, V> boolean evaluateBoolean(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, BehaviourResult.Bool> logicFunction, Supplier<Boolean> vanillaLogic) {
        return evaluate(multiCustomBehaviour, key, logicFunction, vanillaLogic, Converter.DummyConverter.getInstance(Boolean.class));
    }

    public static <K, V> int evaluateInteger(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, BehaviourResult.Object<Integer>> logicFunction, Supplier<Integer> vanillaLogic) {
        return evaluate(multiCustomBehaviour, key, logicFunction, vanillaLogic, Converter.DummyConverter.getInstance(Integer.class));
    }

    public static <K, V> void evaluateVoid(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, BehaviourResult.Void> logicFunction, Runnable vanillaLogic) {
        evaluate(multiCustomBehaviour, key, logicFunction, () -> {
            vanillaLogic.run();
            return null;
        }, Converter.DummyConverter.getInstance(Void.class));
    }

    public static <K, V> void evaluateCallback(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, BehaviourResult.Callback> logicFunction, Supplier<Void> vanillaLogic) {
        evaluate(multiCustomBehaviour, key, logicFunction, vanillaLogic, Converter.DummyConverter.getInstance(Void.class));
    }

    public static <K, V> void evaluateCallback(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, BehaviourResult.Callback> logicFunction) {
        evaluate(multiCustomBehaviour, key, logicFunction);
    }

    public static <T extends BehaviourResult<?, ?>, K, V> boolean runIfVanillaLogicReplaced(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, T> logicFunction) {
        if (!multiCustomBehaviour.isImplemented(key))
            return false;
        V behaviour = multiCustomBehaviour.getBehaviour(key);

        try {
            BehaviourResult<?, ?> behaviourResult = logicFunction.apply(behaviour);
            return behaviourResult.replaceVanillaLogic();
        } catch (Throwable throwable) {
            Bukkit.getLogger()
                  .log(Level.WARNING, "An error occurred while running custom behaviour logic " + multiCustomBehaviour.getKey() + " for key " + key + " with value " + behaviour, throwable);
            return false;
        }
    }

    private static <I, R, T extends BehaviourResult<I, ?>, K, V> R evaluate(@NotNull MultiCustomBehaviour<K, V> multiCustomBehaviour, @NotNull K key, @NotNull Function<V, T> logicFunction) {
        return evaluate(multiCustomBehaviour, key, logicFunction, () -> null, new Converter<>() {
            @Override
            public R bukkitToNMS(I bukkitValue) {
                return null;
            }

            @Override
            public I nmsToBukkitValue(R nmsValue) {
                return null;
            }
        });
    }



    /**
     * @param <I> The bukkit value
     * @param <R> The nms value
     */
    public interface Converter<I, R> {
        class DummyConverter<T> implements Converter<T, T> {
            private static final Map<Class<?>, DummyConverter<?>> cache = new HashMap<>();

            public static <T> DummyConverter<T> getInstance(Class<? extends T> type) {
                return (DummyConverter<T>) cache.computeIfAbsent(type, aClass -> new DummyConverter<T>());
            }

            @Override
            public T bukkitToNMS(T bukkitValue) {
                return bukkitValue;
            }

            @Override
            public T nmsToBukkitValue(T nmsValue) {
                return nmsValue;
            }
        }

        class ItemStackInteraction implements Converter<de.verdox.mccreativelab.behaviour.interaction.ItemStackInteraction, InteractionResultHolder<net.minecraft.world.item.ItemStack>>{
            public static final ItemStackInteraction INSTANCE = new ItemStackInteraction();
            @Override
            public InteractionResultHolder<net.minecraft.world.item.ItemStack> bukkitToNMS(de.verdox.mccreativelab.behaviour.interaction.ItemStackInteraction bukkitValue) {
                return new InteractionResultHolder<>(InteractionResult.INSTANCE.bukkitToNMS(bukkitValue.interactionResult()), CraftItemStack.asNMSCopy(bukkitValue.stack()));
            }
            @Override
            public de.verdox.mccreativelab.behaviour.interaction.ItemStackInteraction nmsToBukkitValue(InteractionResultHolder<net.minecraft.world.item.ItemStack> nmsValue) {
                return new de.verdox.mccreativelab.behaviour.interaction.ItemStackInteraction(InteractionResult.INSTANCE.nmsToBukkitValue(nmsValue.getResult()), nmsValue.getObject().getBukkitStack());
            }
        }

        class ItemStack implements Converter<org.bukkit.inventory.ItemStack, net.minecraft.world.item.ItemStack> {
            public static final ItemStack INSTANCE = new ItemStack();

            @Override
            public net.minecraft.world.item.ItemStack bukkitToNMS(org.bukkit.inventory.ItemStack bukkitValue) {
                if(bukkitValue == null)
                    return net.minecraft.world.item.ItemStack.EMPTY.copy();
                if (bukkitValue instanceof CraftItemStack craftItemStack)
                    return craftItemStack.handle;
                return CraftItemStack.asNMSCopy(bukkitValue);
            }

            @Override
            public org.bukkit.inventory.ItemStack nmsToBukkitValue(net.minecraft.world.item.ItemStack nmsValue) {
                if(nmsValue == null)
                    return new org.bukkit.inventory.ItemStack(Material.AIR);
                return nmsValue.getBukkitStack();
            }
        }

        class InteractionResult implements Converter<de.verdox.mccreativelab.InteractionResult, net.minecraft.world.InteractionResult> {
            public static final InteractionResult INSTANCE = new InteractionResult();

            private InteractionResult() {
            }

            @Override
            public net.minecraft.world.InteractionResult bukkitToNMS(de.verdox.mccreativelab.InteractionResult bukkitValue) {
                if (bukkitValue == null)
                    return net.minecraft.world.InteractionResult.PASS;
                return switch (bukkitValue) {
                    case SUCCESS -> net.minecraft.world.InteractionResult.SUCCESS;
                    case SUCCESS_NO_ITEM_USED -> net.minecraft.world.InteractionResult.SUCCESS_NO_ITEM_USED;
                    case CONSUME -> net.minecraft.world.InteractionResult.CONSUME;
                    case CONSUME_PARTIAL -> net.minecraft.world.InteractionResult.CONSUME_PARTIAL;
                    case PASS -> net.minecraft.world.InteractionResult.PASS;
                    case FAIL -> net.minecraft.world.InteractionResult.FAIL;
                };
            }

            @Override
            public de.verdox.mccreativelab.InteractionResult nmsToBukkitValue(net.minecraft.world.InteractionResult nmsValue) {
                return switch (nmsValue) {
                    case SUCCESS -> de.verdox.mccreativelab.InteractionResult.SUCCESS;
                    case SUCCESS_NO_ITEM_USED -> de.verdox.mccreativelab.InteractionResult.SUCCESS_NO_ITEM_USED;
                    case CONSUME -> de.verdox.mccreativelab.InteractionResult.CONSUME;
                    case CONSUME_PARTIAL -> de.verdox.mccreativelab.InteractionResult.CONSUME_PARTIAL;
                    case PASS -> de.verdox.mccreativelab.InteractionResult.PASS;
                    case FAIL -> de.verdox.mccreativelab.InteractionResult.FAIL;
                };
            }
        }
        class ItemInteractionResult implements Converter<de.verdox.mccreativelab.ItemInteractionResult, net.minecraft.world.ItemInteractionResult>{
            public static final ItemInteractionResult INSTANCE = new ItemInteractionResult();

            @Override
            public net.minecraft.world.ItemInteractionResult bukkitToNMS(de.verdox.mccreativelab.ItemInteractionResult bukkitValue) {
                if(bukkitValue == null)
                    return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                return switch (bukkitValue) {
                    case SUCCESS -> net.minecraft.world.ItemInteractionResult.SUCCESS;
                    case CONSUME -> net.minecraft.world.ItemInteractionResult.CONSUME;
                    case CONSUME_PARTIAL -> net.minecraft.world.ItemInteractionResult.CONSUME_PARTIAL;
                    case PASS_TO_DEFAULT_BLOCK_INTERACTION -> net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                    case SKIP_DEFAULT_BLOCK_INTERACTION -> net.minecraft.world.ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                    case FAIL -> net.minecraft.world.ItemInteractionResult.FAIL;
                };
            }

            @Override
            public de.verdox.mccreativelab.ItemInteractionResult nmsToBukkitValue(net.minecraft.world.ItemInteractionResult nmsValue) {
                if(nmsValue == null)
                    return de.verdox.mccreativelab.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                return switch (nmsValue) {
                    case SUCCESS -> de.verdox.mccreativelab.ItemInteractionResult.SUCCESS;
                    case CONSUME -> de.verdox.mccreativelab.ItemInteractionResult.CONSUME;
                    case CONSUME_PARTIAL -> de.verdox.mccreativelab.ItemInteractionResult.CONSUME_PARTIAL;
                    case PASS_TO_DEFAULT_BLOCK_INTERACTION -> de.verdox.mccreativelab.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                    case SKIP_DEFAULT_BLOCK_INTERACTION -> de.verdox.mccreativelab.ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                    case FAIL -> de.verdox.mccreativelab.ItemInteractionResult.FAIL;
                };
            }
        }


        class BlockData implements Converter<org.bukkit.block.data.BlockData, BlockState> {
            public static final BlockData INSTANCE = new BlockData();

            @Override
            public BlockState bukkitToNMS(org.bukkit.block.data.BlockData bukkitValue) {
                return ((CraftBlockData) bukkitValue).getState();
            }

            @Override
            public CraftBlockData nmsToBukkitValue(BlockState nmsValue) {
                return CraftBlockData.createData(nmsValue);
            }
        }

        R bukkitToNMS(I bukkitValue);

        I nmsToBukkitValue(R nmsValue);
    }
}
