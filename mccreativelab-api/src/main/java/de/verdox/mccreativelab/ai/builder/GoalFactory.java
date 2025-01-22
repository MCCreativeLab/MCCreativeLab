package de.verdox.mccreativelab.ai.builder;

import com.destroystokyo.paper.entity.RangedEntity;
import com.destroystokyo.paper.entity.ai.VanillaGoal;
import de.verdox.mccreativelab.random.VanillaRandomSource;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public interface GoalFactory {
    @NotNull VanillaGoal<@NotNull Mob> avoidEntity(@NotNull Mob mob, @NotNull EntityType entityType, @NotNull Predicate<LivingEntity> extraInclusionPredicate, float distance, double slowSpeed, double fastSpeed, @NotNull Predicate<LivingEntity> inclusionSelector);

    @NotNull VanillaGoal<@NotNull Wolf> beg(@NotNull Wolf wolf, float begDistance);

    @NotNull VanillaGoal<@NotNull Mob> breakDoor(@NotNull Mob mob, @NotNull Predicate<Difficulty> difficultySufficientPredicate);

    @NotNull VanillaGoal<@NotNull Mob> breathAir(@NotNull Mob mob);

    @NotNull VanillaGoal<@NotNull Animals> breed(@NotNull Animals animal, double speed, @NotNull EntityType breedTarget);

    @NotNull VanillaGoal<@NotNull Cat> catLieOnBed(@NotNull Cat cat, double speed, int range);

    @NotNull VanillaGoal<@NotNull Cat> catSitOnBlock(@NotNull Cat cat, double speed);

    @NotNull VanillaGoal<@NotNull Mob> climbOnTopOfPowderSnow(@NotNull Mob mob, @NotNull World world);

    @NotNull VanillaGoal<@NotNull Dolphin> dolphinJump(@NotNull Dolphin dolphin, int chance);

    @NotNull VanillaGoal<@NotNull Mob> eatBlock(@NotNull Mob mob, @NotNull Function<VanillaRandomSource, Boolean> chanceToEat, @NotNull Predicate<BlockState> predicate);

    @NotNull VanillaGoal<@NotNull Mob> fleeSun(@NotNull Mob mob, double speed);

    @NotNull VanillaGoal<@NotNull Mob> floatOnWater(@NotNull Mob mob);

    @NotNull VanillaGoal<@NotNull Mob> followBoat(@NotNull Mob mob);

    @NotNull VanillaGoal<@NotNull Fish> followFlockLeader(@NotNull Fish fish);

    @NotNull VanillaGoal<@NotNull Fish> followMob(@NotNull Mob mob, double speed, float minDistance, float maxDistance, @NotNull Predicate<Mob> followPredicate);

    @NotNull VanillaGoal<@NotNull Tameable> followOwner(@NotNull Tameable tameable, double speed, float minDistance, float maxDistance);

    @NotNull VanillaGoal<@NotNull Animals> followParent(@NotNull Animals animal, double speed);

    @NotNull VanillaGoal<@NotNull Mob> randomStrollInVillage(@NotNull Mob mob, double speed);

    @NotNull VanillaGoal<@NotNull Mob> interact(@NotNull Mob mob, @NotNull EntityType entityType, float range, float chance);

    @NotNull VanillaGoal<@NotNull Parrot> landOnOwnersShoulders(@NotNull Parrot parrot);

    @NotNull VanillaGoal<@NotNull Mob> leapAtTarget(@NotNull Mob mob, float velocity);

    @NotNull VanillaGoal<@NotNull Llama> llamaFollowCaravan(@NotNull Llama llama, double speed);

    @NotNull VanillaGoal<@NotNull Mob> lookAtMob(@NotNull Mob mob, @NotNull EntityType targetType, float range, float change, boolean lookForward);

    @NotNull VanillaGoal<@NotNull AbstractVillager> lookAtTradingPlayer(@NotNull AbstractVillager abstractVillager);

    @NotNull VanillaGoal<@NotNull Mob> meleeAttack(@NotNull Mob mob, double speed, boolean pauseWhenMobIdle);

    @NotNull VanillaGoal<@NotNull Mob> moveBackToVillage(@NotNull Mob mob, double speed, boolean canDespawn);

    @NotNull VanillaGoal<@NotNull Mob> moveThroughVillage(@NotNull Mob mob, double speed, boolean requiresNighttime, int distance, @NotNull BooleanSupplier doorPassingThroughGetter);

    @NotNull VanillaGoal<@NotNull Mob> moveTowardsTarget(@NotNull Mob mob, double speed, float maxDistance);

    @NotNull VanillaGoal<@NotNull Mob> ocelotAttack(@NotNull Mob mob);

    @NotNull VanillaGoal<@NotNull IronGolem> offerFlower(@NotNull IronGolem ironGolem);

    @NotNull VanillaGoal<@NotNull Mob> openDoor(@NotNull Mob mob, boolean delayedClose);

    @NotNull VanillaGoal<@NotNull Mob> panic(@NotNull Mob mob, double speed);

    @NotNull VanillaGoal<@NotNull Raider> pathFindToRaid(@NotNull Raider raider);

    @NotNull VanillaGoal<@NotNull Mob> randomLookAround(@NotNull Mob mob);

    @NotNull VanillaGoal<@NotNull AbstractHorse> randomStand(@NotNull AbstractHorse abstractHorse);

    @NotNull VanillaGoal<@NotNull Mob> randomSwim(@NotNull Mob mob, double speed, int chance);

    @NotNull VanillaGoal<@NotNull RangedEntity> rangedAttack(@NotNull RangedEntity rangedEntity, double mobSpeed, int minIntervalTicks, int maxIntervalTicks, float maxShootRange);

    @NotNull VanillaGoal<@NotNull RangedEntity> rangedBowAttack(@NotNull RangedEntity rangedEntity, double speed, int attackInterval, float range);

    @NotNull VanillaGoal<@NotNull RangedEntity> rangedCrossBowAttack(@NotNull RangedEntity rangedEntity, double speed, float range);

    @NotNull VanillaGoal<@NotNull Mob> removeBlock(@NotNull Mob mob, @NotNull Material blockType, double speed, int maxYDifference);

    @NotNull VanillaGoal<@NotNull Mob> restrictSun(@NotNull Mob mob);

    @NotNull VanillaGoal<@NotNull AbstractHorse> runAroundLikeCrazy(@NotNull AbstractHorse abstractHorse, double speed);

    @NotNull VanillaGoal<@NotNull Tameable> sitWhenOrderedTo(@NotNull Tameable tameable);

    @NotNull VanillaGoal<@NotNull Mob> strollThroughVillage(@NotNull Mob mob, int searchEngine);

    @NotNull VanillaGoal<@NotNull Creeper> swellGoal(@NotNull Creeper creeper, double distanceToStartSwell, double distanceToStopSwell);

    @NotNull VanillaGoal<@NotNull Mob> temptGoal(@NotNull Mob mob, double speed, @NotNull RecipeChoice food, boolean canBeScared);

    @NotNull VanillaGoal<@NotNull AbstractVillager> tradeWithPlayer(@NotNull AbstractVillager abstractVillager);

    @NotNull VanillaGoal<@NotNull Mob> tryFindWater(@NotNull Mob mob);

    @NotNull VanillaGoal<@NotNull Mob> useItem(@NotNull Mob mob, @NotNull ItemStack item, @NotNull Sound.Type type, @NotNull Predicate<Mob> mobPredicate);

    @NotNull VanillaGoal<@NotNull Mob> waterAvoidRandomFly(@NotNull Mob mob, double speed);

    @NotNull VanillaGoal<@NotNull Mob> waterAvoidRandomStroll(@NotNull Mob mob, double speed, float probability);

    @NotNull VanillaGoal<@NotNull Zombie> zombieAttackGoal(@NotNull Zombie zombie, double speed, boolean pauseWhenMobIdle);
}
