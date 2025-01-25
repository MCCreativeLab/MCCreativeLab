package de.verdox.mccreativelab.ai.builder;

import com.destroystokyo.paper.entity.RangedEntity;
import com.destroystokyo.paper.entity.ai.VanillaGoal;
import de.verdox.mccreativelab.random.VanillaRandomSource;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.sound.Sound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftRegionAccessor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class CraftGoalFactory implements GoalFactory {
    @Override
    public @NotNull VanillaGoal<Mob> avoidEntity(@NotNull Mob mob, @NotNull EntityType entityType, @NotNull Predicate<LivingEntity> extraInclusionPredicate, float distance, double slowSpeed, double fastSpeed, @NotNull Predicate<LivingEntity> inclusionSelector) {
        return asGoal(new AvoidEntityGoal<>(toNms(mob), toNms(mob.getWorld(), entityType), toNms(extraInclusionPredicate), distance, slowSpeed, fastSpeed, toNms(inclusionSelector)));
    }

    @Override
    public @NotNull VanillaGoal<Wolf> beg(@NotNull Wolf wolf, float begDistance) {
        return asGoal(new BegGoal((net.minecraft.world.entity.animal.Wolf) toNms(wolf), begDistance));
    }

    @Override
    public @NotNull VanillaGoal<Mob> breakDoor(@NotNull Mob mob, @NotNull Predicate<Difficulty> difficultySufficientPredicate) {
        return asGoal(new BreakDoorGoal(toNms(mob), toNmsDifficultyPredicate(difficultySufficientPredicate)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> breathAir(@NotNull Mob mob) {
        return asGoal(new BreathAirGoal(toNms(mob)));
    }

    @Override
    public @NotNull VanillaGoal<Animals> breed(@NotNull Animals animal, double speed, @NotNull EntityType breedTarget) {
        return asGoal(new BreedGoal((Animal) toNms(animal), speed, (Class<? extends Animal>) toNms(animal.getWorld(), breedTarget)));
    }

    @Override
    public @NotNull VanillaGoal<Cat> catLieOnBed(@NotNull Cat cat, double speed, int range) {
        return asGoal(new CatLieOnBedGoal((net.minecraft.world.entity.animal.Cat) toNms(cat), speed, range));
    }

    @Override
    public @NotNull VanillaGoal<Cat> catSitOnBlock(@NotNull Cat cat, double speed) {
        return asGoal(new CatSitOnBlockGoal((net.minecraft.world.entity.animal.Cat) toNms(cat), speed));
    }

    @Override
    public @NotNull VanillaGoal<Mob> climbOnTopOfPowderSnow(@NotNull Mob mob, @NotNull World world) {
        return asGoal(new ClimbOnTopOfPowderSnowGoal(toNms(mob), toNms(world)));
    }

    @Override
    public @NotNull VanillaGoal<Dolphin> dolphinJump(@NotNull Dolphin dolphin, int chance) {
        return asGoal(new DolphinJumpGoal((net.minecraft.world.entity.animal.Dolphin) toNms(dolphin), chance));
    }

    @Override
    public @NotNull VanillaGoal<Mob> eatBlock(@NotNull Mob mob, @NotNull Function<VanillaRandomSource, Boolean> chanceToEat, @NotNull Predicate<BlockState> predicate) {
        return asGoal(new EatBlockGoal(toNms(mob), chanceToEat, predicate));
    }

    @Override
    public @NotNull VanillaGoal<Mob> fleeSun(@NotNull Mob mob, double speed) {
        return asGoal(new FleeSunGoal(toNms(mob), speed));
    }

    @Override
    public @NotNull VanillaGoal<Mob> floatOnWater(@NotNull Mob mob) {
        return asGoal(new FloatGoal(toNms(mob)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> followBoat(@NotNull Mob mob) {
        return asGoal(new FollowBoatGoal(toNms(mob)));
    }

    @Override
    public @NotNull VanillaGoal<Fish> followFlockLeader(@NotNull Fish fish) {
        return asGoal(new FollowFlockLeaderGoal((AbstractSchoolingFish) toNms(fish)));
    }

    @Override
    public @NotNull VanillaGoal<Fish> followMob(@NotNull Mob mob, double speed, float minDistance, float maxDistance, @NotNull Predicate<Mob> followPredicate) {
        return asGoal(new FollowMobGoal(toNms(mob), speed, minDistance, maxDistance, toNMSMobPredicate(followPredicate)));
    }

    @Override
    public @NotNull VanillaGoal<Tameable> followOwner(@NotNull Tameable tameable, double speed, float minDistance, float maxDistance) {
        return asGoal(new FollowOwnerGoal((TamableAnimal) toNms(tameable), speed, minDistance, maxDistance));
    }

    @Override
    public @NotNull VanillaGoal<Animals> followParent(@NotNull Animals animal, double speed) {
        return asGoal(new FollowParentGoal((Animal) toNms(animal), speed));
    }

    @Override
    public @NotNull VanillaGoal<Mob> randomStrollInVillage(@NotNull Mob mob, double speed) {
        return asGoal(new GolemRandomStrollInVillageGoal(toNms(mob), speed));
    }

    @Override
    public @NotNull VanillaGoal<Mob> interact(@NotNull Mob mob, @NotNull EntityType entityType, float range, float chance) {
        return asGoal(new InteractGoal(toNms(mob), toNms(mob.getWorld(), entityType), range, chance));
    }

    @Override
    public @NotNull VanillaGoal<Parrot> landOnOwnersShoulders(@NotNull Parrot parrot) {
        return asGoal(new LandOnOwnersShoulderGoal((ShoulderRidingEntity) toNms(parrot)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> leapAtTarget(@NotNull Mob mob, float velocity) {
        return asGoal(new LeapAtTargetGoal(toNms(mob), velocity));
    }

    @Override
    public @NotNull VanillaGoal<Llama> llamaFollowCaravan(@NotNull Llama llama, double speed) {
        return asGoal(new LlamaFollowCaravanGoal((net.minecraft.world.entity.animal.horse.Llama) toNms(llama), speed));
    }

    @Override
    public @NotNull VanillaGoal<Mob> lookAtMob(@NotNull Mob mob, @NotNull EntityType targetType, float range, float change, boolean lookForward) {
        return asGoal(new LookAtPlayerGoal(toNms(mob), toNms(mob.getWorld(), targetType), range, change, lookForward));
    }

    @Override
    public @NotNull VanillaGoal<AbstractVillager> lookAtTradingPlayer(@NotNull AbstractVillager abstractVillager) {
        return asGoal(new LookAtTradingPlayerGoal((net.minecraft.world.entity.npc.AbstractVillager) toNms(abstractVillager)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> meleeAttack(@NotNull Mob mob, double speed, boolean pauseWhenMobIdle) {
        return asGoal(new MeleeAttackGoal(toNms(mob), speed, pauseWhenMobIdle));
    }

    @Override
    public @NotNull VanillaGoal<Mob> moveBackToVillage(@NotNull Mob mob, double speed, boolean canDespawn) {
        return asGoal(new MoveBackToVillageGoal(toNms(mob), speed, canDespawn));
    }

    @Override
    public @NotNull VanillaGoal<Mob> moveThroughVillage(@NotNull Mob mob, double speed, boolean requiresNighttime, int distance, @NotNull BooleanSupplier doorPassingThroughGetter) {
        return asGoal(new MoveThroughVillageGoal(toNms(mob), speed, requiresNighttime, distance, doorPassingThroughGetter));
    }

    @Override
    public @NotNull VanillaGoal<Mob> moveTowardsTarget(@NotNull Mob mob, double speed, float maxDistance) {
        return asGoal(new MoveTowardsTargetGoal(toNms(mob), speed, maxDistance));
    }

    @Override
    public @NotNull VanillaGoal<Mob> ocelotAttack(@NotNull Mob mob) {
        return asGoal(new OcelotAttackGoal(toNms(mob)));
    }

    @Override
    public @NotNull VanillaGoal<IronGolem> offerFlower(@NotNull IronGolem ironGolem) {
        return asGoal(new OfferFlowerGoal((net.minecraft.world.entity.animal.IronGolem) toNms(ironGolem)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> openDoor(@NotNull Mob mob, boolean delayedClose) {
        return asGoal(new OpenDoorGoal(toNms(mob), delayedClose));
    }

    @Override
    public @NotNull VanillaGoal<Mob> panic(@NotNull Mob mob, double speed) {
        return asGoal(new PanicGoal(toNms(mob), speed));
    }

    @Override
    public @NotNull VanillaGoal<Raider> pathFindToRaid(@NotNull Raider raider) {
        return asGoal(new PathfindToRaidGoal<>((net.minecraft.world.entity.raid.Raider) toNms(raider)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> randomLookAround(@NotNull Mob mob) {
        return asGoal(new RandomLookAroundGoal(toNms(mob)));
    }

    @Override
    public @NotNull VanillaGoal<AbstractHorse> randomStand(@NotNull AbstractHorse abstractHorse) {
        return asGoal(new RandomStandGoal((net.minecraft.world.entity.animal.horse.AbstractHorse) toNms(abstractHorse)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> randomSwim(@NotNull Mob mob, double speed, int chance) {
        return asGoal(new RandomSwimmingGoal(toNms(mob), speed, chance));
    }

    @Override
    public @NotNull VanillaGoal<RangedEntity> rangedAttack(@NotNull RangedEntity rangedEntity, double mobSpeed, int minIntervalTicks, int maxIntervalTicks, float maxShootRange) {
        return asGoal(new RangedAttackGoal((RangedAttackMob) toNms(rangedEntity), mobSpeed, minIntervalTicks, maxIntervalTicks, maxShootRange));
    }

    @Override
    public @NotNull VanillaGoal<RangedEntity> rangedBowAttack(@NotNull RangedEntity rangedEntity, double speed, int attackInterval, float range) {
        return asGoal(new RangedBowAttackGoal<>((net.minecraft.world.entity.monster.Monster & RangedAttackMob) toNms(rangedEntity), speed, attackInterval, range));
    }

    @Override
    public @NotNull VanillaGoal<RangedEntity> rangedCrossBowAttack(@NotNull RangedEntity rangedEntity, double speed, float range) {
        return asGoal(new RangedCrossbowAttackGoal<>((net.minecraft.world.entity.monster.Monster & RangedAttackMob & CrossbowAttackMob) toNms(rangedEntity), speed, range));
    }

    @Override
    public @NotNull VanillaGoal<Mob> removeBlock(@NotNull Mob mob, @NotNull Material blockType, double speed, int maxYDifference) {
        return asGoal(new RemoveBlockGoal(CraftMagicNumbers.getBlock(blockType), toNms(mob), speed, maxYDifference));
    }

    @Override
    public @NotNull VanillaGoal<Mob> restrictSun(@NotNull Mob mob) {
        return asGoal(new RestrictSunGoal(toNms(mob)));
    }

    @Override
    public @NotNull VanillaGoal<AbstractHorse> runAroundLikeCrazy(@NotNull AbstractHorse abstractHorse, double speed) {
        return asGoal(new RunAroundLikeCrazyGoal((net.minecraft.world.entity.animal.horse.AbstractHorse) toNms(abstractHorse), speed));
    }

    @Override
    public @NotNull VanillaGoal<Tameable> sitWhenOrderedTo(@NotNull Tameable tameable) {
        return asGoal(new SitWhenOrderedToGoal((TamableAnimal) toNms(tameable)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> strollThroughVillage(@NotNull Mob mob, int searchEngine) {
        return asGoal(new StrollThroughVillageGoal(toNms(mob), searchEngine));
    }

    @Override
    public @NotNull VanillaGoal<Creeper> swellGoal(@NotNull Creeper creeper, double distanceToStartSwell, double distanceToStopSwell) {
        return asGoal(new SwellGoal((net.minecraft.world.entity.monster.Creeper) toNms(creeper), distanceToStartSwell, distanceToStopSwell));
    }

    @Override
    public @NotNull VanillaGoal<Mob> temptGoal(@NotNull Mob mob, double speed, @NotNull RecipeChoice food, boolean canBeScared) {
        return asGoal(new TemptGoal(toNms(mob), speed, CraftRecipe.toIngredient(food, true), canBeScared));
    }

    @Override
    public @NotNull VanillaGoal<AbstractVillager> tradeWithPlayer(@NotNull AbstractVillager abstractVillager) {
        return asGoal(new TradeWithPlayerGoal((net.minecraft.world.entity.npc.AbstractVillager) toNms(abstractVillager)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> tryFindWater(@NotNull Mob mob) {
        return asGoal(new TryFindWaterGoal(toNms(mob)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> useItem(@NotNull Mob mob, @NotNull ItemStack item, Sound.@NotNull Type type, @NotNull Predicate<Mob> mobPredicate) {
        return asGoal(new UseItemGoal<>(toNms(mob), CraftItemStack.asNMSCopy(item), BuiltInRegistries.SOUND_EVENT.get(PaperAdventure.asVanilla(type.key())), toNMSMobPredicate(mobPredicate)));
    }

    @Override
    public @NotNull VanillaGoal<Mob> waterAvoidRandomFly(@NotNull Mob mob, double speed) {
        return asGoal(new WaterAvoidingRandomFlyingGoal(toNms(mob), speed));
    }

    @Override
    public @NotNull VanillaGoal<Mob> waterAvoidRandomStroll(@NotNull Mob mob, double speed, float probability) {
        return asGoal(new WaterAvoidingRandomStrollGoal(toNms(mob), speed, probability));
    }

    @Override
    public @NotNull VanillaGoal<Zombie> zombieAttackGoal(@NotNull Zombie zombie, double speed, boolean pauseWhenMobIdle) {
        return asGoal(new ZombieAttackGoal((net.minecraft.world.entity.monster.Zombie) toNms(zombie), speed, pauseWhenMobIdle));
    }

    private Class<? extends net.minecraft.world.entity.LivingEntity> toNms(World world, EntityType entityType) {
        if(entityType.equals(EntityType.PLAYER))
            return Player.class;

        CraftRegionAccessor craftRegionAccessor = (CraftRegionAccessor) world;
        return (Class<? extends PathfinderMob>) ((CraftEntity) craftRegionAccessor.createEntity(world.getSpawnLocation(), entityType.getEntityClass()))
            .getHandle().getClass();
    }

    private Predicate<net.minecraft.world.entity.LivingEntity> toNms(Predicate<LivingEntity> predicate) {
        return livingEntity -> predicate.test(livingEntity.getBukkitLivingEntity());
    }

    private PathfinderMob toNms(Mob mob) {
        return (PathfinderMob) ((CraftMob) mob).getHandle();
    }

    private ServerLevel toNms(World world) {
        return ((CraftWorld) world).getHandle();
    }

    private Predicate<net.minecraft.world.Difficulty> toNmsDifficultyPredicate(Predicate<Difficulty> predicate) {
        return difficulty -> {
            Difficulty bukkitDifficulty = switch (difficulty) {
                case PEACEFUL -> Difficulty.PEACEFUL;
                case EASY -> Difficulty.EASY;
                case NORMAL -> Difficulty.NORMAL;
                case HARD -> Difficulty.HARD;
            };
            return predicate.test(bukkitDifficulty);
        };
    }

    private Predicate<net.minecraft.world.entity.Mob> toNMSMobPredicate(Predicate<Mob> predicate){
        return mob -> predicate.test((Mob)mob.getBukkitLivingEntity());
    }

    private <T extends Mob> VanillaGoal<T> asGoal(Goal goal) {
        return (VanillaGoal<T>) goal.asPaperVanillaGoal();
    }
}
