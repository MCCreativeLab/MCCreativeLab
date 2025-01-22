package de.verdox.mccreativelab.behavior;

import org.jetbrains.annotations.NotNull;

public interface Behaviour {
    @NotNull
    default BehaviourResult.Void voidResult(@NotNull BehaviourResult.Void.Type type) {
        return new BehaviourResult.Void(type);
    }

    @NotNull
    default BehaviourResult.Void voidResult() {
        return voidResult(BehaviourResult.Void.Type.REPLACE_VANILLA);
    }

    @NotNull
    default BehaviourResult.Callback done() {
        return new BehaviourResult.Callback();
    }

    @NotNull
    default BehaviourResult.Bool bool(boolean value, @NotNull BehaviourResult.Bool.Type type) {
        return new BehaviourResult.Bool(value, type);
    }

    @NotNull
    default BehaviourResult.Bool bool(boolean value) {
        return bool(value, BehaviourResult.Bool.Type.REPLACE_VANILLA);
    }

    @NotNull
    default <T> BehaviourResult.Object<T> result(@NotNull T result, @NotNull BehaviourResult.Object.Type type){
        return new BehaviourResult.Object<>(result, type);
    }

    @NotNull
    default <T> BehaviourResult.Object<T> result(@NotNull T result){
        return result(result, BehaviourResult.Object.Type.REPLACE_VANILLA);
    }
}
