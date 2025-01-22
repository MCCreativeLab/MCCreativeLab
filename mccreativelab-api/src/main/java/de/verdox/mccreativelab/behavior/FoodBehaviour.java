package de.verdox.mccreativelab.behavior;

import de.verdox.mccreativelab.CustomBehaviour;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface FoodBehaviour extends Behaviour {
    /**
     * If you wish to change the amount of health gained, use the {@link org.bukkit.event.entity.EntityRegainHealthEvent}
     * If you wish to change the amount of damage taken by starvation, use the {@link org.bukkit.event.entity.EntityDamageEvent}
     * This interface is solely used to manipulate the vanilla variables that are not yet changeable by the API.
     * Implement your own version of the FoodBehaviour Interface and make FOOD_BEHAVIOUR use your implementation.
     */
    CustomBehaviour<FoodBehaviour> FOOD_BEHAVIOUR = new CustomBehaviour<>(FoodBehaviour.class, new FoodBehaviour() {},"MCCLab - FoodBehaviour");
    /**
     * Gets the regeneration rate per tick when a player is saturated.
     * Defaults to 10 in vanilla.
     * @param player The player
     * @return the regeneration rate per tick
     */
    default int getSaturatedRegenRate(@NotNull Player player){return 10;}
    /**
     * Gets the regeneration rate per tick when a player is unsaturated.
     * Defaults to 80 in vanilla.
     * @param player The player
     * @return the regeneration rate per tick
     */
    default int getUnsaturatedRegenRate(@NotNull Player player){return 80;}
    /**
     * Gets the starvation rate per tick when a player is starving.
     * Defaults to 80 in vanilla.
     * @param player The player
     * @return the regeneration rate per tick
     */
    default int getStarvationRate(@NotNull Player player){return 80;}
    /**
     * Gets the food level when a player starts starving
     * Defaults to 0 in vanilla.
     * @param player The player
     * @return the starvation food limit
     */
    default int getStarvationFoodLimit(@NotNull Player player){return 0;}
    /**
     * Gets the food level a player needs at minimum to start regenerating health
     * Defaults to 18 in vanilla.
     * @param player The player
     * @return the food level
     */
    default int getMinimumFoodToRegenerate(@NotNull Player player){return 18;}
    /**
     * Gets the exhaustion over time rate in ticks
     * This is a custom feature. If the method returns an integer less or equal then -1 the feature won't be used.
     * @param player The player
     * @return the food level
     */
    default int getExhaustionOverTimeRateInTicks(@NotNull Player player){return 0;}
    /**
     * Gets the exhaustion amount over time
     * * This is a custom feature. If the method returns an integer less or equal then 0 the feature won't be used.
     * @param player The player
     * @return the food level
     */
    default int getExhaustionOverTimeAmount(@NotNull Player player){return 0;}
}
