package org.bukkit.craftbukkit;

import net.minecraft.world.level.biome.Biome;
import org.bukkit.Precipitation;

public class CraftPrecipitation {

    public static Precipitation toBukkit(Biome.Precipitation precipitation){
        return switch (precipitation){
            case NONE -> Precipitation.NONE;
            case RAIN -> Precipitation.RAIN;
            case SNOW -> Precipitation.SNOW;
        };
    }

    public static Biome.Precipitation toNMS(Precipitation precipitation){
        return switch (precipitation){
            case NONE -> Biome.Precipitation.NONE;
            case RAIN -> Biome.Precipitation.RAIN;
            case SNOW -> Biome.Precipitation.SNOW;
        };
    }

}
