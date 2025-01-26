package de.verdox.mccreativelab.data;

/**
 * Used as a helper class that injects runnable code after the minecraft registries are bootstrapped.
 * We need this because paper has its own registry manipulation logic before the minecraft registries are bootstrapped.
 * <p>
 * We need this because when we want to add stuff like custom attributes we must ensure, that they are registered after the mc attributes.
 * Else the attribute ids that are sent via packets are doomed.
 */
public interface VanillaRegistryManipulator {
    /**
     * Injects code into the explained code point.
     * @param runnable the code
     */
    void injectRegistryManipulationCode(Runnable runnable);
}
