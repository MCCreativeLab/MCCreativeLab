package de.verdox.mccreativelab.ai.builder;

import com.destroystokyo.paper.entity.RangedEntity;
import com.mojang.datafixers.util.Pair;
import de.verdox.mccreativelab.ai.behavior.*;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistries;
import net.kyori.adventure.sound.Sound;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import org.bukkit.*;
import org.bukkit.craftbukkit.CraftPoiType;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryKey;
import org.bukkit.craftbukkit.tag.CraftBlockTag;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;

public class CraftBehaviorFactory implements BehaviorFactory {

    private static GateBehavior.OrderPolicy toNMS(GateOrderPolicy orderPolicy) {
        return switch (orderPolicy) {
            case ORDERED -> GateBehavior.OrderPolicy.ORDERED;
            case SHUFFLED -> GateBehavior.OrderPolicy.SHUFFLED;
        };
    }

    private static GateBehavior.RunningPolicy toNMS(GateRunningPolicy runningPolicy) {
        return switch (runningPolicy) {
            case RUN_ONE -> GateBehavior.RunningPolicy.RUN_ONE;
            case TRY_ALL -> GateBehavior.RunningPolicy.TRY_ALL;
        };
    }

    private static <E extends LivingEntity> AIBehavior<E> asBehavior(Behavior<? extends net.minecraft.world.entity.LivingEntity> behaviour) {
        return new CraftAIBehavior<>(behaviour);
    }

    private static <E extends LivingEntity> OneShotBehavior<E> asOneshot(OneShot<? extends net.minecraft.world.entity.LivingEntity> behaviour) {
        return new CraftOneShotBehavior<>(behaviour);
    }

    private static <E extends LivingEntity> OneShotBehavior<E> asOneshot(BehaviorControl<? extends net.minecraft.world.entity.LivingEntity> behaviour) {
        return new CraftOneShotBehavior<>((OneShot<?>) behaviour);
    }

    private static <E extends LivingEntity> ControlledBehavior<E> asControlledBehaviour(BehaviorControl<? extends net.minecraft.world.entity.LivingEntity> behaviorControl) {
        return new CraftControlledBehavior<>(behaviorControl);
    }

    @Override
    public @NotNull OneShotBehavior<Mob> acquirePOI(@NotNull Predicate<PoiType> poiTypePredicate, @NotNull MemoryKey<Location> poiPosModule, @NotNull MemoryKey<Location> potentialPoiPosModule, boolean onlyRunIfChild, boolean entityEvent) {
        Predicate<Holder<net.minecraft.world.entity.ai.village.poi.PoiType>> poiPredicate = poiTypeHolder -> poiTypePredicate.test(CraftPoiType.minecraftToBukkit(poiTypeHolder.value()));
        MemoryModuleType<GlobalPos> poiPosModuleNMS = CraftMemoryKey.bukkitToMinecraft(poiPosModule);
        MemoryModuleType<GlobalPos> potentialPoiPosModuleNMS = CraftMemoryKey.bukkitToMinecraft(potentialPoiPosModule);

        return new CraftOneShotBehavior<>((OneShot<PathfinderMob>) AcquirePoi.create(poiPredicate, poiPosModuleNMS, potentialPoiPosModuleNMS, onlyRunIfChild, entityEvent ? Optional.of((byte) 14) : Optional.empty()));
    }

    @Override
    public @NotNull AIBehavior<Animals> animalMakeLove(@NotNull EntityType targetType, float speed, int approachDistance) {
        if (!Animals.class.isAssignableFrom(targetType.getEntityClass()))
            throw new IllegalArgumentException("Please provide an Animal EntityType");
        return asBehavior(new AnimalMakeLove((net.minecraft.world.entity.EntityType<? extends Animal>) CraftEntityType.bukkitToMinecraft(targetType), speed, approachDistance));
    }

    @Override
    public @NotNull AIBehavior<Mob> animalPanic(float speed, @NotNull Function<Mob, io.papermc.paper.registry.tag.TagKey<DamageType>> panicPredicate) {
        return asBehavior(new AnimalPanic<>(speed, pathfinderMob -> PaperRegistries.toNms(panicPredicate.apply(((Mob)pathfinderMob.getBukkitLivingEntity())))));
    }

    @Override
    public @NotNull OneShotBehavior<Villager> assignProfessionFromJobSite() {
        return asOneshot(AssignProfessionFromJobSite.create());
    }

    @Override
    public @NotNull OneShotBehavior<Ageable> babyFollowAdult(int minRange, int maxRange, float speed) {
        return asOneshot(BabyFollowAdult.create(UniformInt.of(minRange, maxRange), speed));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> backupIfTooClose(int distance, float forwardMovement) {
        return asOneshot(BackUpIfTooClose.create(distance, forwardMovement));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> becomePassiveIfMemoryPresent(@NotNull MemoryKey<?> requiredMemory, int duration) {
        return asOneshot(BecomePassiveIfMemoryPresent.create(CraftMemoryKey.bukkitToMinecraft(requiredMemory), duration));
    }

    @Override
    public @NotNull AIBehavior<Villager> celebrateVillagersSurviveRaid(int minRuntime, int maxRunTime) {
        return asBehavior(new CelebrateVillagersSurvivedRaid(minRuntime, maxRunTime));
    }

    @Override
    public <T> @NotNull OneShotBehavior<LivingEntity> copyMemoryWithExpiry(@NotNull Predicate<LivingEntity> runPredicate, @NotNull MemoryKey<T> sourceType, @NotNull MemoryKey<T> targetType, int minExpiry, int maxExpiry) {
        return asOneshot(CopyMemoryWithExpiry.create(livingEntity -> runPredicate.test(livingEntity.getBukkitLivingEntity()), CraftMemoryKey.bukkitToMinecraft(sourceType), CraftMemoryKey.bukkitToMinecraft(targetType), UniformInt.of(minExpiry, maxExpiry)));
    }

    @Override
    public @NotNull AIBehavior<LivingEntity> countDownCooldownTicks(@NotNull MemoryKey<Integer> moduleType) {
        return asBehavior(new CountDownCooldownTicks(CraftMemoryKey.bukkitToMinecraft(moduleType)));
    }

    @Override
    public @NotNull AIBehavior<Frog> croak() {
        return asBehavior(new Croak());
    }

    @Override
    public @NotNull AIBehavior<RangedEntity> crossbowAttack() {
        return asBehavior(new CrossbowAttack<>());
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> dismountOrSkipMounting(int range, @NotNull BiPredicate<LivingEntity, Entity> alternativeRideCondition) {
        return asOneshot(DismountOrSkipMounting.create(range, (livingEntity, entity) -> alternativeRideCondition.test(livingEntity.getBukkitLivingEntity(), entity.getBukkitEntity())));
    }

    @Override
    public @NotNull ControlledBehavior<LivingEntity> doNothing(int minRuntime, int maxRunTime) {
        return asControlledBehaviour(new DoNothing(minRuntime, maxRunTime));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> eraseMemoryIf(@NotNull Predicate<LivingEntity> condition, @NotNull MemoryKey<?> memory) {
        return asOneshot(EraseMemoryIf.create(livingEntity -> condition.test(livingEntity.getBukkitLivingEntity()), CraftMemoryKey.bukkitToMinecraft(memory)));
    }

    @Override
    public @NotNull AIBehavior<Mob> followTemptation(@NotNull Function<LivingEntity, Float> speed, @NotNull Function<LivingEntity, Double> stopDistanceGetter) {
        return asBehavior(new FollowTemptation(livingEntity -> speed.apply(livingEntity.getBukkitLivingEntity()), livingEntity -> stopDistanceGetter.apply(livingEntity.getBukkitLivingEntity())));
    }

    @Override
    public <T extends LivingEntity> @NotNull ControlledBehavior<T> gateBehaviour(@NotNull Consumer<ActivityBuilder<T>> activityBuilder, @NotNull GateOrderPolicy gateOrderPolicy, @NotNull GateRunningPolicy gateRunningPolicy) {

        CraftActivityBuilder<LivingEntity> gateActivityBuilder = (CraftActivityBuilder<LivingEntity>) Bukkit.getAIFactory().createActivityBuilder(LivingEntity.class, EntityActivity.CORE);
        activityBuilder.accept((ActivityBuilder<T>) gateActivityBuilder);

        Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState = gateActivityBuilder.requiredMemories.stream().map(memoryModuleTypeMemoryStatusPair -> Map.entry(memoryModuleTypeMemoryStatusPair.getFirst(), memoryModuleTypeMemoryStatusPair.getSecond())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Pair<? extends BehaviorControl<? super net.minecraft.world.entity.LivingEntity>, Integer>> tasks = new LinkedList<>();

        for (Pair<Integer, BehaviorControl<? extends net.minecraft.world.entity.LivingEntity>> behaviour : gateActivityBuilder.craftWeightedBehaviorsBuilder.behaviours) {
            BehaviorControl<? super net.minecraft.world.entity.LivingEntity> behaviorControl = (BehaviorControl<? super net.minecraft.world.entity.LivingEntity>) behaviour.getSecond();

            tasks.add(Pair.of(behaviorControl, behaviour.getFirst()));
        }

        return asControlledBehaviour(new GateBehavior<>(requiredMemoryState, gateActivityBuilder.forgettingMemories, toNMS(gateOrderPolicy), toNMS(gateRunningPolicy), tasks));
    }

    @Override
    public @NotNull AIBehavior<Villager> giveGiftToHero(int delay) {
        return asBehavior(new GiveGiftToHero(delay));
    }

    @Override
    public @NotNull OneShotBehavior<Villager> goToClosestVillage(float speed, int completionRange) {
        return asOneshot(GoToClosestVillage.create(speed, completionRange));
    }

    @Override
    public @NotNull AIBehavior<Villager> goToPotentialJobSite(float speed) {
        return asBehavior(new GoToPotentialJobSite(speed));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> goToTargetLocation(@NotNull MemoryKey<Location> posModule, int completionRange, float speed) {
        return asOneshot(GoToTargetLocation.create(CraftMemoryKey.bukkitToMinecraft(posModule), completionRange, speed));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> goToWantedItem(@NotNull Predicate<LivingEntity> startCondition, float speed, boolean requiresWalkTarget, int radius) {
        return asOneshot(GoToWantedItem.create(livingEntity -> startCondition.test(livingEntity.getBukkitLivingEntity()),speed, requiresWalkTarget, radius));
    }

    @Override
    public @NotNull AIBehavior<Villager> harvestFarmland() {
        return asBehavior(new HarvestFarmland());
    }

    @Override
    public @NotNull OneShotBehavior<Mob> insideBrownianWalk(float speed) {
        return asOneshot(InsideBrownianWalk.create(speed));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> interactWith(@NotNull EntityType entityType, int maxDistance, @NotNull Predicate<LivingEntity> interactorPredicate, @NotNull Predicate<Entity> targetPredicate, @NotNull MemoryKey<Entity> memoryKey, float speed, int completionRange) {
        if (!LivingEntity.class.isAssignableFrom(entityType.getEntityClass()))
            throw new IllegalArgumentException(entityType + " is not a living entity type");

        net.minecraft.world.entity.EntityType<net.minecraft.world.entity.LivingEntity> nmsType = (net.minecraft.world.entity.EntityType<net.minecraft.world.entity.LivingEntity>) CraftEntityType.bukkitToMinecraft(entityType);

        return asOneshot(
            InteractWith.of(nmsType,
                maxDistance, entity -> interactorPredicate.test(entity.getBukkitLivingEntity()),
                entity -> targetPredicate.test(entity.getBukkitEntity()),
                CraftMemoryKey.bukkitToMinecraft(memoryKey),
                speed,
                completionRange));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> interactWithDoor() {
        return asOneshot(InteractWithDoor.create());
    }

    @Override
    public @NotNull AIBehavior<Mob> jumpOnBed(float walkSpeed) {
        return asBehavior(new JumpOnBed(walkSpeed));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> locateHidingPlace(int maxDistance, float walkSpeed, int preferredDistance) {
        return asOneshot(LocateHidingPlace.create(maxDistance, walkSpeed, preferredDistance));
    }

    @Override
    public @NotNull AIBehavior<Mob> longJumpMidJump(int minCooldownRange, int maxCooldownRange, Sound.@NotNull Type soundType) {
        return asBehavior(new LongJumpMidJump(UniformInt.of(minCooldownRange, maxCooldownRange), BuiltInRegistries.SOUND_EVENT.get(PaperAdventure.asVanilla(soundType.key()))));
    }

    @Override
    public @NotNull AIBehavior<Mob> longJumpToPreferredBlock(int minCooldownRange, int maxCooldownRange, int verticalRange, int horizontalRange, float maxRange, Sound.@NotNull Type jumpSound, @NotNull Tag<Material> favoredBlocks, float biasChance, @NotNull BiPredicate<Mob, Location> jumpToPredicate) {

        if (!(favoredBlocks instanceof CraftBlockTag craftBlockTag))
            throw new IllegalArgumentException("Please provide a Block tag");

        ResourceLocation key = CraftNamespacedKey.toMinecraft(craftBlockTag.getKey());
        TagKey<Block> blockTagKey = TagKey.create(Registries.BLOCK, key);

        return asBehavior(new LongJumpToPreferredBlock<>(UniformInt.of(minCooldownRange, maxCooldownRange), verticalRange, horizontalRange, maxRange, mob -> BuiltInRegistries.SOUND_EVENT.get(PaperAdventure.asVanilla(jumpSound.key())), blockTagKey, biasChance, (mob, pos) -> jumpToPredicate.test((Mob) mob.getBukkitLivingEntity(), new Location(mob
            .getBukkitEntity().getWorld(), pos.getX(), pos.getY(), pos.getZ()))));
    }

    @Override
    public @NotNull AIBehavior<Mob> longJumpToRandomBlock(int minCooldownRange, int maxCooldownRange, int verticalRange, int horizontalRange, float maxRange, Sound.@NotNull Type jumpSound) {
        return asBehavior(new LongJumpToRandomPos<>(UniformInt.of(minCooldownRange, maxCooldownRange), verticalRange, horizontalRange, maxRange, mob -> BuiltInRegistries.SOUND_EVENT.get(PaperAdventure.asVanilla(jumpSound.key()))));
    }

    @Override
    public @NotNull AIBehavior<Villager> lookAndFollowTradingPlayerSink(float speed) {
        return asBehavior(new LookAndFollowTradingPlayerSink(speed));
    }

    @Override
    public @NotNull AIBehavior<Mob> lookAtTargetSink(int minRunTime, int maxRunTime) {
        return asBehavior(new LookAtTargetSink(minRunTime, maxRunTime));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> meleeAttack(int cooldown) {
        return asOneshot(MeleeAttack.create(cooldown));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> mount(float speed) {
        return asOneshot(Mount.create(speed));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> moveToSkySeeingSpot(float speed) {
        return asOneshot(MoveToSkySeeingSpot.create(speed));
    }

    @Override
    public @NotNull AIBehavior<Mob> moveToTargetSink(int minRunTime, int maxRunTime) {
        return asBehavior(new MoveToTargetSink(minRunTime, maxRunTime));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> playTagWithOtherKids() {
        return asOneshot(PlayTagWithOtherKids.create());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> poiCompetitorScan() {
        return asOneshot(PoiCompetitorScan.create());
    }

    @Override
    public @NotNull AIBehavior<Mob> randomLookAround(int minCooldownRange, int maxCooldownRange, float maxYaw, float minPitch, float maxPitch) {
        return asBehavior(new RandomLookAround(UniformInt.of(minCooldownRange, maxCooldownRange), maxYaw, minPitch, maxPitch));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> randomStroll(float speed, boolean strollInsideWater) {
        return asOneshot(RandomStroll.stroll(speed, strollInsideWater));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> randomStroll(float speed, int horizontalRadius, int verticalRadius) {
        return asOneshot(RandomStroll.stroll(speed, horizontalRadius, verticalRadius));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> randomFlyStroll(float speed) {
        return asOneshot(RandomStroll.fly(speed));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> randomSwimStroll(float speed) {
        return asOneshot(RandomStroll.swim(speed));
    }

    @Override
    public @NotNull OneShotBehavior<Villager> reactToBell() {
        return asOneshot(ReactToBell.create());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> resetProfession() {
        return asOneshot(ResetProfession.create());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> resetRaidStatus() {
        return asOneshot(ResetRaidStatus.create());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> ringBell() {
        return asOneshot(RingBell.create());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> setCloseHomeAsWalkTarget(float speed) {
        return asOneshot(SetClosestHomeAsWalkTarget.create(speed));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> setEntityLookTarget(@NotNull Predicate<LivingEntity> predicate, float maxDistance) {
        return asOneshot(SetEntityLookTarget.create(livingEntity -> predicate.test(livingEntity.getBukkitLivingEntity()), maxDistance));
    }

    @Override
    public @NotNull OneShotBehavior<Villager> setHiddenState(int maxHiddenSeconds, int distance) {
        return asOneshot(SetHiddenState.create(maxHiddenSeconds, distance));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> setLookAndInteract(@NotNull EntityType entityType, int maxDistance) {
        return asOneshot(SetLookAndInteract.create(CraftEntityType.bukkitToMinecraft(entityType), maxDistance));
    }

    @Override
    public @NotNull OneShotBehavior<LivingEntity> setRaidStatus() {
        return asOneshot(SetRaidStatus.create());
    }

    @Override
    public @NotNull OneShotBehavior<Mob> setWalkTargetAwayFromPos(@NotNull MemoryKey<Location> memoryKey, float speed, int range, boolean requiresWalkTarget) {
        return asOneshot(SetWalkTargetAwayFrom.pos(CraftMemoryKey.bukkitToMinecraft(memoryKey), speed, range, requiresWalkTarget));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> setWalkTargetAwayFromEntity(@NotNull MemoryKey<Entity> memoryKey, float speed, int range, boolean requiresWalkTarget) {
        return asOneshot(SetWalkTargetAwayFrom.entity(CraftMemoryKey.bukkitToMinecraft(memoryKey), speed, range, requiresWalkTarget));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> setWalkTargetFromAttackTargetIfTargetOutOfReach(@NotNull Function<LivingEntity, Float> speed) {
        return asOneshot(SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(livingEntity -> speed.apply(livingEntity.getBukkitLivingEntity())));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> setWalkTargetFromBlockMemory(@NotNull MemoryKey<Location> blockMemoryKey, float speed, int completionRange, int maxDistance, int maxRunTime) {
        return asOneshot(SetWalkTargetFromBlockMemory.create(CraftMemoryKey.bukkitToMinecraft(blockMemoryKey), speed, completionRange, maxDistance, maxRunTime));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> setWalkTargetFromLookTarget(@NotNull Predicate<LivingEntity> predicate, @NotNull Function<LivingEntity, Float> speed, int completionRange) {
        return asOneshot(SetWalkTargetFromLookTarget.create(livingEntity -> predicate.test(livingEntity.getBukkitLivingEntity()), livingEntity -> speed.apply(livingEntity.getBukkitLivingEntity()), completionRange));
    }

    @Override
    public @NotNull AIBehavior<Villager> showTradesToPlayer(int minRunTime, int maxRunTime) {
        return asBehavior(new ShowTradesToPlayer(minRunTime, maxRunTime));
    }

    @Override
    public @NotNull AIBehavior<Villager> sleepInBed() {
        return asBehavior(new SleepInBed());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> socializeAtBell() {
        return asOneshot(SocializeAtBell.create());
    }

    @Override
    public @NotNull OneShotBehavior<Mob> startAttacking(@NotNull Function<Mob, LivingEntity> targetGetter) {
        return asOneshot(StartAttacking.create(mob -> {
            var entity = targetGetter.apply((Mob)mob.getBukkitLivingEntity());
            if(entity == null)
                return Optional.empty();
            else
                return Optional.of(((CraftLivingEntity) entity).getHandle());
        }));
    }

    @Override
    public @NotNull OneShotBehavior<Piglin> startCelebratingIfTargetDead(int celebrationDuration, @NotNull BiPredicate<LivingEntity, LivingEntity> predicate) {
        return asOneshot(StartCelebratingIfTargetDead.create(celebrationDuration, (livingEntity, livingEntity2) -> predicate.test(livingEntity.getBukkitLivingEntity(), livingEntity2.getBukkitLivingEntity())));
    }

    @Override
    public @NotNull AIBehavior<Mob> stopAttackingIfTargetInvalid(@NotNull Predicate<LivingEntity> alternativeCondition, @NotNull BiConsumer<Mob, LivingEntity> forgetCallback, boolean shouldForgetIfTargetUnreachable) {
        return asBehavior((Behavior<? extends net.minecraft.world.entity.LivingEntity>) StopAttackingIfTargetInvalid.create(livingEntity ->
            alternativeCondition.test(livingEntity.getBukkitLivingEntity()), (mob, livingEntity) -> {
            forgetCallback.accept((Mob) mob.getBukkitLivingEntity(), livingEntity.getBukkitLivingEntity());
        }, shouldForgetIfTargetUnreachable));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> stopBeingAngryIfTargetDead() {
        return asOneshot(StopBeingAngryIfTargetDead.create());
    }

    @Override
    public @NotNull OneShotBehavior<Mob> strollAroundPoi(@NotNull MemoryKey<Location> posMemory, float walkSpeed, int maxDistance) {
        return asOneshot(StrollAroundPoi.create(CraftMemoryKey.bukkitToMinecraft(posMemory), walkSpeed, maxDistance));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> strollToPoi(@NotNull MemoryKey<Location> posMemory, float walkSpeed, int completionRange, int maxDistance) {
        return asOneshot(StrollToPoi.create(CraftMemoryKey.bukkitToMinecraft(posMemory), walkSpeed, completionRange, maxDistance));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> strollToPoiList(@NotNull MemoryKey<List<Location>> posMemory, float walkSpeed, int completionRange, int primaryPositionActivationDistance, @NotNull MemoryKey<Location> primaryPosition) {
        return asOneshot(StrollToPoiList.create(CraftMemoryKey.bukkitToMinecraft(posMemory), walkSpeed, completionRange, primaryPositionActivationDistance, CraftMemoryKey.bukkitToMinecraft(primaryPosition)));
    }

    @Override
    public @NotNull AIBehavior<Mob> swim(float chance) {
        return asBehavior(new Swim(chance));
    }

    @Override
    public @NotNull AIBehavior<Villager> tradeWithVillager() {
        return asBehavior(new TradeWithVillager());
    }

    @Override
    public <T extends LivingEntity> @NotNull OneShotBehavior<T> triggerGate(@NotNull Consumer<WeightedBehaviorsBuilder<T>> weightedTasks, @NotNull GateOrderPolicy order, @NotNull GateRunningPolicy runMode) {
        CraftWeightedBehaviorsBuilder<T> craftWeightedBehaviorsBuilder = new CraftWeightedBehaviorsBuilder<>();
        weightedTasks.accept(craftWeightedBehaviorsBuilder);
        List<Pair<? extends Trigger<? super net.minecraft.world.entity.LivingEntity>, Integer>> tasks = new LinkedList<>();

        for (Pair<Integer, BehaviorControl<? extends net.minecraft.world.entity.LivingEntity>> behaviour : craftWeightedBehaviorsBuilder.behaviours) {
            int priority = behaviour.getFirst();
            Trigger<? super net.minecraft.world.entity.LivingEntity> trigger = (Trigger<? super net.minecraft.world.entity.LivingEntity>) behaviour.getSecond();
            tasks.add(Pair.of(trigger, priority));
        }
        return asOneshot(TriggerGate.triggerGate(tasks, toNMS(order), toNMS(runMode)));
    }

    @Override
    public <T extends LivingEntity> @NotNull OneShotBehavior<T> sequence(@NotNull OneShotBehavior<T> predicateBehavior, @NotNull OneShotBehavior<T> task) {
        return asOneshot(BehaviorBuilder.sequence((Trigger<? super net.minecraft.world.entity.LivingEntity>) ((CraftOneShotBehavior<T>) predicateBehavior).getHandle(), (Trigger<? super net.minecraft.world.entity.LivingEntity>) ((CraftOneShotBehavior<T>) task).getHandle()));
    }

    @Override
    public <T extends LivingEntity> @NotNull OneShotBehavior<T> triggerIf(@NotNull Predicate<T> predicate, @NotNull OneShotBehavior<T> task) {
        return asOneshot(BehaviorBuilder.triggerIf(e -> predicate.test((T) e.getBukkitLivingEntity()), ((CraftOneShotBehavior<T>) task).getHandle()));
    }

    @Override
    public <T extends LivingEntity> @NotNull OneShotBehavior<T> triggerIf(@NotNull Predicate<T> predicate) {
        return asOneshot(BehaviorBuilder.triggerIf(e -> predicate.test((T) e.getBukkitLivingEntity())));
    }

    @Override
    public <T extends LivingEntity> @NotNull OneShotBehavior<T> triggerIf(@NotNull BiPredicate<World, T> predicate) {
        return asOneshot(BehaviorBuilder.triggerIf((serverLevel, e) -> predicate.test(serverLevel.getWorld(), (T) e.getBukkitLivingEntity())));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> tryFindLand(int range, float speed) {
        return asOneshot(TryFindLand.create(range, speed));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> tryFindLandNearWater(int range, float speed) {
        return asOneshot(TryFindLandNearWater.create(range, speed));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> tryFindWater(int range, float speed) {
        return asOneshot(TryFindWater.create(range, speed));
    }

    @Override
    public @NotNull OneShotBehavior<Frog> tryLaySpawnOnWaterNearLand(@NotNull Material spawn) {
        return asOneshot(TryLaySpawnOnWaterNearLand.create(CraftMagicNumbers.getBlock(spawn)));
    }

    @Override
    public @NotNull OneShotBehavior<Mob> updateActivityFromSchedule() {
        return asOneshot(UpdateActivityFromSchedule.create());
    }

    @Override
    public @NotNull AIBehavior<Villager> useBonemeal() {
        return asBehavior(new UseBonemeal());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> validateNearbyPoi(@NotNull Predicate<PoiType> poiTypePredicate, @NotNull MemoryKey<Location> poiMemory) {
        return asOneshot(ValidateNearbyPoi.create(poiTypeHolder -> poiTypePredicate.test(CraftPoiType.minecraftToBukkit(poiTypeHolder.value())), CraftMemoryKey.bukkitToMinecraft(poiMemory)));
    }

    @Override
    public @NotNull OneShotBehavior<Villager> villageBoundRandomStroll(float walkSpeed, int horizontalRange, int verticalRange) {
        return asOneshot(VillageBoundRandomStroll.create(walkSpeed, horizontalRange, verticalRange));
    }

    @Override
    public @NotNull OneShotBehavior<Villager> villagerCalmDown() {
        return asOneshot(VillagerCalmDown.create());
    }

    @Override
    public @NotNull AIBehavior<Villager> villagerMakeLove() {
        return asBehavior(new VillagerMakeLove());
    }

    @Override
    public @NotNull AIBehavior<Villager> villagerPanicTrigger() {
        return asBehavior(new VillagerPanicTrigger());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> wakeUp() {
        return asOneshot(WakeUp.create());
    }

    @Override
    public @NotNull AIBehavior<Villager> workAtComposter() {
        return asBehavior(new WorkAtComposter());
    }

    @Override
    public @NotNull AIBehavior<Villager> workAtPoi() {
        return asBehavior(new WorkAtPoi());
    }

    @Override
    public @NotNull OneShotBehavior<Villager> yieldJobSite(float speed) {
        return asOneshot(YieldJobSite.create(speed));
    }
}
