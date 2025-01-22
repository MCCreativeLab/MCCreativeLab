package de.verdox.mccreativelab.ai.builder;

import com.destroystokyo.paper.entity.RangedEntity;
import de.verdox.mccreativelab.ai.behavior.AIBehavior;
import de.verdox.mccreativelab.ai.behavior.ControlledBehavior;
import de.verdox.mccreativelab.ai.behavior.OneShotBehavior;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.*;

public interface BehaviorFactory {

    @NotNull OneShotBehavior<Mob> acquirePOI(@NotNull Predicate<PoiType> poiTypePredicate, @NotNull MemoryKey<Location> poiPosModule, @NotNull MemoryKey<Location> potentialPoiPosModule, boolean onlyRunIfChild, boolean entityEvent);

    @NotNull
    default OneShotBehavior<Mob> acquirePOI(@NotNull Predicate<PoiType> poiTypePredicate, @NotNull MemoryKey<Location> poiPosModule, boolean onlyRunIfChild, boolean entityEvent) {
        return acquirePOI(poiTypePredicate, poiPosModule, poiPosModule, onlyRunIfChild, entityEvent);
    }

    @NotNull AIBehavior<Animals> animalMakeLove(@NotNull EntityType targetType, float speed, int approachDistance);

    @NotNull AIBehavior<Mob> animalPanic(float speed, @NotNull Function<Mob, io.papermc.paper.registry.tag.TagKey<DamageType>> panicPredicate);

    @NotNull OneShotBehavior<Villager> assignProfessionFromJobSite();

    @NotNull OneShotBehavior<Ageable> babyFollowAdult(int minRange, int maxRange, float speed);

    @NotNull OneShotBehavior<Mob> backupIfTooClose(int distance, float forwardMovement);

    @NotNull OneShotBehavior<LivingEntity> becomePassiveIfMemoryPresent(@NotNull MemoryKey<?> requiredMemory, int duration);

    @NotNull AIBehavior<Villager> celebrateVillagersSurviveRaid(int minRuntime, int maxRunTime);

    @NotNull <T> OneShotBehavior<LivingEntity> copyMemoryWithExpiry(@NotNull Predicate<LivingEntity> runPredicate, @NotNull MemoryKey<T> sourceType, @NotNull MemoryKey<T> targetType, int minExpiry, int maxExpiry);

    @NotNull AIBehavior<LivingEntity> countDownCooldownTicks(@NotNull MemoryKey<Integer> moduleType);

    @NotNull AIBehavior<Frog> croak();

    @NotNull AIBehavior<RangedEntity> crossbowAttack();

    @NotNull OneShotBehavior<LivingEntity> dismountOrSkipMounting(int range, @NotNull BiPredicate<LivingEntity, Entity> alternativeRideCondition);

    @NotNull ControlledBehavior<LivingEntity> doNothing(int minRuntime, int maxRunTime);

    @NotNull OneShotBehavior<LivingEntity> eraseMemoryIf(@NotNull Predicate<LivingEntity> condition, @NotNull MemoryKey<?> memory);

    @NotNull AIBehavior<Mob> followTemptation(@NotNull Function<LivingEntity, Float> speed, @NotNull Function<LivingEntity, Double> stopDistanceGetter);

    @NotNull AIBehavior<Villager> giveGiftToHero(int delay);

    @NotNull OneShotBehavior<Villager> goToClosestVillage(float speed, int completionRange);

    @NotNull AIBehavior<Villager> goToPotentialJobSite(float speed);

    @NotNull OneShotBehavior<Mob> goToTargetLocation(@NotNull MemoryKey<Location> posModule, int completionRange, float speed);

    @NotNull OneShotBehavior<LivingEntity> goToWantedItem(@NotNull Predicate<LivingEntity> startCondition, float speed, boolean requiresWalkTarget, int radius);

    @NotNull AIBehavior<Villager> harvestFarmland();

    @NotNull OneShotBehavior<Mob> insideBrownianWalk(float speed);

    @NotNull OneShotBehavior<LivingEntity> interactWith(@NotNull EntityType entityType, int maxDistance, @NotNull Predicate<LivingEntity> interactorPredicate, @NotNull Predicate<Entity> targetPredicate, @NotNull MemoryKey<Entity> memoryKey, float speed, int completionRange);

    @NotNull
    default OneShotBehavior<LivingEntity> interactWith(@NotNull EntityType entityType, int maxDistance, @NotNull MemoryKey<Entity> memoryKey, float speed, int completionRange) {
        return interactWith(entityType, maxDistance, livingEntity -> true, entity -> true, memoryKey, speed, completionRange);
    }

    @NotNull OneShotBehavior<LivingEntity> interactWithDoor();

    @NotNull AIBehavior<Mob> jumpOnBed(float walkSpeed);

    @NotNull OneShotBehavior<LivingEntity> locateHidingPlace(int maxDistance, float walkSpeed, int preferredDistance);

    @NotNull AIBehavior<Mob> longJumpMidJump(int minCooldownRange, int maxCooldownRange, @NotNull Sound.Type soundType);

    @NotNull AIBehavior<Mob> longJumpToPreferredBlock(int minCooldownRange, int maxCooldownRange, int verticalRange, int horizontalRange, float maxRange, @NotNull Sound.Type jumpSound, @NotNull Tag<Material> favoredBlocks, float biasChance, @NotNull BiPredicate<Mob, Location> jumpToPredicate);

    @NotNull AIBehavior<Mob> longJumpToRandomBlock(int minCooldownRange, int maxCooldownRange, int verticalRange, int horizontalRange, float maxRange, @NotNull Sound.Type jumpSound);

    @NotNull AIBehavior<Villager> lookAndFollowTradingPlayerSink(float speed);

    @NotNull AIBehavior<Mob> lookAtTargetSink(int minRunTime, int maxRunTime);

    @NotNull OneShotBehavior<Mob> meleeAttack(int cooldown);

    @NotNull OneShotBehavior<LivingEntity> mount(float speed);

    @NotNull OneShotBehavior<LivingEntity> moveToSkySeeingSpot(float speed);

    @NotNull AIBehavior<Mob> moveToTargetSink(int minRunTime, int maxRunTime);

    @NotNull OneShotBehavior<Mob> playTagWithOtherKids();

    @NotNull OneShotBehavior<Villager> poiCompetitorScan();

    @NotNull AIBehavior<Mob> randomLookAround(int minCooldownRange, int maxCooldownRange, float maxYaw, float minPitch, float maxPitch);

    @NotNull OneShotBehavior<Mob> randomStroll(float speed, boolean strollInsideWater);

    @NotNull OneShotBehavior<Mob> randomStroll(float speed, int horizontalRadius, int verticalRadius);

    @NotNull OneShotBehavior<Mob> randomFlyStroll(float speed);

    @NotNull OneShotBehavior<Mob> randomSwimStroll(float speed);

    @NotNull OneShotBehavior<Villager> reactToBell();

    @NotNull OneShotBehavior<Villager> resetProfession();

    @NotNull OneShotBehavior<Villager> resetRaidStatus();

    @NotNull OneShotBehavior<Villager> ringBell();

    @NotNull OneShotBehavior<Villager> setCloseHomeAsWalkTarget(float speed);

    @NotNull OneShotBehavior<LivingEntity> setEntityLookTarget(@NotNull Predicate<LivingEntity> predicate, float maxDistance);

    @NotNull
    default OneShotBehavior<LivingEntity> setEntityLookTarget(@NotNull EntityType type, float maxDistance) {
        return setEntityLookTarget(livingEntity -> livingEntity.getType() == type, maxDistance);
    }

    @NotNull OneShotBehavior<Villager> setHiddenState(int maxHiddenSeconds, int distance);

    @NotNull OneShotBehavior<LivingEntity> setLookAndInteract(@NotNull EntityType entityType, int maxDistance);

    @NotNull OneShotBehavior<LivingEntity> setRaidStatus();

    @NotNull OneShotBehavior<Mob> setWalkTargetAwayFromPos(@NotNull MemoryKey<Location> memoryKey, float speed, int range, boolean requiresWalkTarget);

    @NotNull OneShotBehavior<Mob> setWalkTargetAwayFromEntity(@NotNull MemoryKey<Entity> memoryKey, float speed, int range, boolean requiresWalkTarget);

    @NotNull OneShotBehavior<Mob> setWalkTargetFromAttackTargetIfTargetOutOfReach(@NotNull Function<LivingEntity, Float> speed);

    @NotNull OneShotBehavior<Mob> setWalkTargetFromBlockMemory(@NotNull MemoryKey<Location> blockMemoryKey, float speed, int completionRange, int maxDistance, int maxRunTime);

    @NotNull OneShotBehavior<Mob> setWalkTargetFromLookTarget(@NotNull Predicate<LivingEntity> predicate, @NotNull Function<LivingEntity, Float> speed, int completionRange);

    @NotNull
    default OneShotBehavior<Mob> setWalkTargetFromLookTarget(float speed, int completionRange) {
        return setWalkTargetFromLookTarget(livingEntity -> true, livingEntity -> speed, completionRange);
    }

    @NotNull AIBehavior<Villager> showTradesToPlayer(int minRunTime, int maxRunTime);

    @NotNull AIBehavior<Villager> sleepInBed();

    @NotNull OneShotBehavior<Villager> socializeAtBell();

    @NotNull OneShotBehavior<Mob> startAttacking(@NotNull Function<Mob, LivingEntity> targetGetter);

    @NotNull OneShotBehavior<Piglin> startCelebratingIfTargetDead(int celebrationDuration, @NotNull BiPredicate<LivingEntity, LivingEntity> predicate);

    @NotNull AIBehavior<Mob> stopAttackingIfTargetInvalid(@NotNull Predicate<LivingEntity> alternativeCondition, @NotNull BiConsumer<Mob, LivingEntity> forgetCallback, boolean shouldForgetIfTargetUnreachable);

    @NotNull OneShotBehavior<Mob> stopBeingAngryIfTargetDead();

    @NotNull OneShotBehavior<Mob> strollAroundPoi(@NotNull MemoryKey<Location> posMemory, float walkSpeed, int maxDistance);

    @NotNull OneShotBehavior<Mob> strollToPoi(@NotNull MemoryKey<Location> posMemory, float walkSpeed, int completionRange, int maxDistance);

    @NotNull OneShotBehavior<Mob> strollToPoiList(@NotNull MemoryKey<List<Location>> posMemory, float walkSpeed, int completionRange, int primaryPositionActivationDistance, @NotNull MemoryKey<Location> primaryPosition);

    @NotNull AIBehavior<Mob> swim(float chance);

    @NotNull AIBehavior<Villager> tradeWithVillager();

    @NotNull OneShotBehavior<Mob> tryFindLand(int range, float speed);

    @NotNull OneShotBehavior<Mob> tryFindLandNearWater(int range, float speed);

    @NotNull OneShotBehavior<Mob> tryFindWater(int range, float speed);

    @NotNull OneShotBehavior<Frog> tryLaySpawnOnWaterNearLand(@NotNull Material spawn);

    @NotNull OneShotBehavior<Mob> updateActivityFromSchedule();

    @NotNull AIBehavior<Villager> useBonemeal();

    @NotNull OneShotBehavior<Villager> validateNearbyPoi(@NotNull Predicate<PoiType> poiTypePredicate, @NotNull MemoryKey<Location> poiMemory);

    @NotNull OneShotBehavior<Villager> villageBoundRandomStroll(float walkSpeed, int horizontalRange, int verticalRange);

    @NotNull
    default OneShotBehavior<Villager> villageBoundRandomStroll(float walkSpeed) {
        return villageBoundRandomStroll(walkSpeed, 10, 7);
    }

    @NotNull OneShotBehavior<Villager> villagerCalmDown();

    @NotNull AIBehavior<Villager> villagerMakeLove();

    @NotNull AIBehavior<Villager> villagerPanicTrigger();

    @NotNull OneShotBehavior<Villager> wakeUp();

    @NotNull AIBehavior<Villager> workAtComposter();

    @NotNull AIBehavior<Villager> workAtPoi();

    @NotNull OneShotBehavior<Villager> yieldJobSite(float speed);

    // Logic
    @NotNull <T extends LivingEntity> ControlledBehavior<T> gateBehaviour(@NotNull Consumer<ActivityBuilder<T>> activityBuilder, @NotNull GateOrderPolicy gateOrderPolicy, @NotNull GateRunningPolicy gateRunningPolicy);

    @NotNull
    default <T extends LivingEntity> ControlledBehavior<T> runOne(@NotNull Consumer<ActivityBuilder<T>> activityBuilder) {
        return gateBehaviour(activityBuilder, GateOrderPolicy.SHUFFLED, GateRunningPolicy.RUN_ONE);
    }

    @NotNull <T extends LivingEntity> OneShotBehavior<T> triggerGate(@NotNull Consumer<WeightedBehaviorsBuilder<T>> weightedTasks, @NotNull GateOrderPolicy order, @NotNull GateRunningPolicy runMode);

    @NotNull
    default <T extends LivingEntity> OneShotBehavior<T> triggerOneShuffled(@NotNull Consumer<WeightedBehaviorsBuilder<T>> weightedTasks) {
        return triggerGate(weightedTasks, GateOrderPolicy.SHUFFLED, GateRunningPolicy.RUN_ONE);
    }

    @NotNull <T extends LivingEntity> OneShotBehavior<T> sequence(@NotNull OneShotBehavior<T> predicateBehavior, @NotNull OneShotBehavior<T> task);

    @NotNull <T extends LivingEntity> OneShotBehavior<T> triggerIf(@NotNull Predicate<T> predicate, @NotNull OneShotBehavior<T> task);

    @NotNull <T extends LivingEntity> OneShotBehavior<T> triggerIf(@NotNull Predicate<T> predicate);

    @NotNull <T extends LivingEntity> OneShotBehavior<T> triggerIf(@NotNull BiPredicate<World, T> predicate);

    enum GateOrderPolicy {
        ORDERED,
        SHUFFLED
    }

    enum GateRunningPolicy {
        RUN_ONE,
        TRY_ALL
    }
}
