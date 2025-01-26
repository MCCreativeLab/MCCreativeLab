package de.verdox.mccreativelab.data;

import java.util.LinkedList;
import java.util.List;

public class CraftVanillaRegistryManipulator implements VanillaRegistryManipulator {
    public static List<Runnable> CUSTOM_BOOTSTRAPPERS = new LinkedList<>();


    @Override
    public void injectRegistryManipulationCode(Runnable runnable) {
        CUSTOM_BOOTSTRAPPERS.add(runnable);
    }
}
