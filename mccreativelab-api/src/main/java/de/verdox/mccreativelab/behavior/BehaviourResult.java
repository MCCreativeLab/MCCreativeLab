package de.verdox.mccreativelab.behavior;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * @param <T> The actual return value of a behaviour that was run.
 * @param <R> The Result Type
 */
public abstract class BehaviourResult<T, R extends Enum<?>> {
    private final T value;
    private final R result;

    BehaviourResult(@Nullable T value, @NotNull R result) {
        this.value = value;
        this.result = result;
    }

    @Nullable public T getValue() {
        return value;
    }

    @NotNull public R getResult() {
        return result;
    }

    @NotNull
    protected abstract T evaluateReturnValue(@NotNull Supplier<T> vanillaLogic);
    protected abstract boolean replaceVanillaLogic();

    public static class Callback extends BehaviourResult<java.lang.Void, Callback.Type> {
        public static final Callback DEFAULT_INSTANCE = new Callback();

        Callback() {
            super(null, Type.NONE);
        }

        @Override
        protected java.lang.@NotNull Void evaluateReturnValue(@NotNull Supplier<java.lang.Void> vanillaLogic) {
            return null;
        }

        @Override
        protected boolean replaceVanillaLogic() {
            return false;
        }

        public enum Type {
            NONE,
        }
    }

    public static class Void extends BehaviourResult<java.lang.Void, Void.Type> {
        public static final Void DEFAULT_INSTANCE = new Void(Type.USE_VANILLA);

        public Void(@NotNull Type result) {
            super(null, result);
        }

        public boolean replaceVanillaLogic() {
            return Type.REPLACE_VANILLA.equals(getResult());
        }

        @Override
        protected java.lang.@NotNull Void evaluateReturnValue(@NotNull Supplier<java.lang.Void> vanillaLogic) {
            if (!Type.REPLACE_VANILLA.equals(getResult()))
                return vanillaLogic.get();
            else
                return getValue();
        }

        public enum Type {
            REPLACE_VANILLA,
            USE_VANILLA
        }
    }

    public static class Bool extends BehaviourResult<Boolean, Bool.Type> {
        public static final Bool DEFAULT_INSTANCE = new Bool(false, Type.ONLY_VANILLA);

        public Bool(@Nullable Boolean value, @NotNull Type result) {
            super(value, result);
        }

        @Override
        protected @NotNull Boolean evaluateReturnValue(@NotNull Supplier<Boolean> vanillaLogic) {
            return switch (getResult()) {
                case AND -> vanillaLogic.get() && getValue();
                case OR -> vanillaLogic.get() || getValue();
                case XOR -> vanillaLogic.get() ^ getValue();
                case REPLACE_VANILLA -> getValue();
                case ONLY_VANILLA -> vanillaLogic.get();
            };
        }

        @Override
        protected boolean replaceVanillaLogic() {
            return Type.REPLACE_VANILLA.equals(getResult());
        }
        public enum Type {
            AND,
            OR,
            XOR,
            REPLACE_VANILLA,
            ONLY_VANILLA,
        }
    }

    public static class Object<T> extends BehaviourResult<T, Object.Type> {
        public static final Object DEFAULT_INSTANCE = new Object(null, Type.USE_VANILLA);

        public Object(@Nullable T value, @NotNull Type result) {
            super(value, result);
        }

        @Override
        protected @NotNull T evaluateReturnValue(@NotNull Supplier<T> vanillaLogic) {
            return switch (getResult()) {
                case REPLACE_VANILLA -> getValue();
                case USE_VANILLA -> vanillaLogic.get();
            };
        }

        @Override
        protected boolean replaceVanillaLogic() {
            return Type.REPLACE_VANILLA.equals(getResult());
        }

        public enum Type {
            REPLACE_VANILLA,
            USE_VANILLA
        }
    }
}
