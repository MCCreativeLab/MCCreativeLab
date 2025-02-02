package de.verdox.mccreativelab.behaviour;

import de.verdox.mccreativelab.MultiCustomBehaviour;
import de.verdox.mccreativelab.behavior.BehaviourResult;
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

public interface BehaviourUtil {
    public static final BehaviourResult.Object<Float> FLOAT_DEFAULT = new BehaviourResult.Object<>(0f, BehaviourResult.Object.Type.USE_VANILLA_ONLY);

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

    public static <V> float evaluateFloat(@Nullable V behaviour, @NotNull Function<V, BehaviourResult.Object<Float>> logicFunction, Supplier<Float> vanillaLogic){
        return evaluate(behaviour, logicFunction, vanillaLogic, Converter.DummyConverter.getInstance(Float.class));
    }

    public static <V> double evaluateDouble(@Nullable V behaviour, @NotNull Function<V, BehaviourResult.Object<Double>> logicFunction, Supplier<Double> vanillaLogic){
        return evaluate(behaviour, logicFunction, vanillaLogic, Converter.DummyConverter.getInstance(Double.class));
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
