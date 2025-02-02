package de.verdox.mccreativelab.behavior;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @param <T> The actual return value of a behaviour that was run.
 * @param <R> The Result Type
 */
public abstract class BehaviourResult<T, R extends BehaviourResult.Type> {
    private final T value;
    private final R result;

    BehaviourResult(@Nullable T value, @NotNull R result) {
        this.value = value;
        this.result = result;
    }

    @Nullable
    public T getValue() {
        return value;
    }

    @NotNull
    public R getResult() {
        return result;
    }

    @NotNull
    public T evaluateReturnValue(@NotNull Supplier<T> vanillaLogic) {
        if (getResult().equals(Type.REPLACE_VANILLA)) {
            return Objects.requireNonNull(getValue());
        } else if (getResult().equals(Type.USE_VANILLA_ONLY)) {
            return vanillaLogic.get();
        } else if (getResult().equals(Type.RUN_VANILLA_RETURN_OWN)) {
            vanillaLogic.get();
            return Objects.requireNonNull(getValue());
        } else if (getResult().equals(Type.RUN_OWN_RETURN_VANILLA)) {
            getValue();
            return vanillaLogic.get();
        }
        throw new IllegalStateException("Unknown type returned");
    }

    public abstract boolean replaceVanillaLogic();

    public static class Callback extends BehaviourResult<java.lang.Void, Type> {
        public static final Callback DEFAULT_INSTANCE = new Callback();

        Callback() {
            super(null, Type.NONE);
        }

        @Override
        public java.lang.@NotNull Void evaluateReturnValue(@NotNull Supplier<java.lang.Void> vanillaLogic) {
            return null;
        }

        @Override
        public boolean replaceVanillaLogic() {
            return false;
        }
    }

    public static class Void extends BehaviourResult<java.lang.Void, Void.Type> {
        public static final Void DEFAULT_INSTANCE = new Void(Type.USE_VANILLA_ONLY);

        public Void(@NotNull Type result) {
            super(null, result);
        }

        public boolean replaceVanillaLogic() {
            return Type.REPLACE_VANILLA.equals(getResult());
        }
    }

    public static class Bool extends BehaviourResult<Boolean, Bool.Type> {
        public static final Bool DEFAULT_INSTANCE = new Bool(false, Type.USE_VANILLA_ONLY);

        public Bool(@Nullable Boolean value, @NotNull Type result) {
            super(value, result);
        }

        @Override
        public @NotNull Boolean evaluateReturnValue(@NotNull Supplier<Boolean> vanillaLogic) {
            if (getResult().equals(BooleanType.AND)) {
                return vanillaLogic.get() && Boolean.TRUE.equals(getValue());
            } else if (getResult().equals(BooleanType.OR)) {
                return vanillaLogic.get() || Boolean.TRUE.equals(getValue());
            } else if (getResult().equals(BooleanType.XOR)) {
                return vanillaLogic.get() ^ Boolean.TRUE.equals(getValue());
            } else if (getResult().equals(BooleanType.REPLACE_VANILLA)) {
                return Boolean.TRUE.equals(getValue());
            } else if (getResult().equals(BooleanType.USE_VANILLA_ONLY)) {
                return vanillaLogic.get();
            }
            return super.evaluateReturnValue(vanillaLogic);
        }

        @Override
        public boolean replaceVanillaLogic() {
            return Type.REPLACE_VANILLA.equals(getResult());
        }

        public static final class BooleanType extends BehaviourResult.Type {
            public static final BooleanType AND = new BooleanType();
            public static final BooleanType OR = new BooleanType();
            public static final BooleanType XOR = new BooleanType();
        }
    }

    public static class Object<T> extends BehaviourResult<T, Object.Type> {
        public static final Object DEFAULT_INSTANCE = new Object(null, Type.USE_VANILLA_ONLY);

        public Object(@Nullable T value, @NotNull Type result) {
            super(value, result);
        }

        @Override
        public boolean replaceVanillaLogic() {
            return Type.REPLACE_VANILLA.equals(getResult());
        }
    }

    public sealed static class Type {
        public static final Type REPLACE_VANILLA = new Type();
        public static final Type USE_VANILLA_ONLY = new Type();
        public static final Type RUN_OWN_RETURN_VANILLA = new Type();
        public static final Type RUN_VANILLA_RETURN_OWN = new Type();
        public static final Type NONE = new Type();
    }
}
