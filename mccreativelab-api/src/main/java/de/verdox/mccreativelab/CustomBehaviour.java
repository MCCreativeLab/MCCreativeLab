package de.verdox.mccreativelab;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Used as wrapper class for custom implemented behaviour.
 * This is normally used to change constants in the minecraft gameplay.
 * @param <T>
 */
public class CustomBehaviour<T> {
    private final ProxyInterface<T> proxyInterface;
    private final String key;
    public CustomBehaviour(@NotNull Class<? extends T> type, @NotNull T defaultImplementation, @NotNull String key){
        this.key = key;
        this.proxyInterface = new ProxyInterface<>(type, defaultImplementation);
    }

    @NotNull public String getKey() {
        return key;
    }

    /**
     * Used to set a new behaviour.
     * @param behaviour The new implementation
     */
    public void setBehaviour(@NotNull T behaviour) {
        Objects.requireNonNull(behaviour);
        this.proxyInterface.addImplementation(behaviour);
    }

    @ApiStatus.Internal
    public T getBehaviour() {
        return proxyInterface.getImplementation();
    }

    /**
     * Checks if a custom implementation exists
     * @return true if a custom implementation exists
     */
    public boolean isImplemented(){
        return proxyInterface.isImplemented();
    }
}
