package de.verdox.mccreativelab.behaviour;

import de.verdox.mccreativelab.behaviour.entity.AnimalEntityBehaviour;
import de.verdox.mccreativelab.behaviour.entity.EntityBehaviour;
import de.verdox.mccreativelab.behaviour.entity.LivingEntityBehaviour;
import de.verdox.mccreativelab.behaviour.entity.MobBehaviour;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityBehaviourUtil extends BehaviourUtil {
    private static final EntityBehaviour<?> DEFAULT = new EntityBehaviour<>() {
    };

    public static boolean fireImmune(Entity entity, Supplier<Boolean> vanillaLogic) {
        return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Entity.class, EntityBehaviour.class), entityBehaviour -> entityBehaviour.fireImmune(entity.getBukkitEntity()), vanillaLogic);
    }
    public static void readAdditionalSaveData(Entity entity){
        evaluateCallback(getBehaviour(entity, org.bukkit.entity.Entity.class, EntityBehaviour.class), entityBehaviour -> entityBehaviour.readAdditionalSaveData(entity.getBukkitEntity(), entity.getBukkitEntity().getPersistentDataContainer()), () -> {});
    }

    public static void addAdditionalSaveData(Entity entity){
        evaluateCallback(getBehaviour(entity, org.bukkit.entity.Entity.class, EntityBehaviour.class), entityBehaviour -> entityBehaviour.addAdditionalSaveData(entity.getBukkitEntity(), entity.getBukkitEntity().getPersistentDataContainer()), () -> {});
    }


    public static boolean ignoreExplosion(Entity entity, Level level, double x, double y, double z, boolean createFire, Explosion explosion, Supplier<Boolean> vanillaLogic) {
        return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Entity.class, EntityBehaviour.class), entityBehaviour -> {
                Location explosionLocation = new Location(level.getWorld(), x, y, z);
                float radius = explosion.radius();
                @Nullable org.bukkit.entity.Entity source = explosion.source.getBukkitEntity();
                Map<org.bukkit.entity.Player, Vector> hitPlayers = explosion.getHitPlayers().entrySet().stream().map(playerVec3Entry -> {
                    Vector vector = new Vector(playerVec3Entry.getValue().x(), playerVec3Entry.getValue().y(), playerVec3Entry.getValue().z());
                    org.bukkit.entity.Player player = (org.bukkit.entity.Player) playerVec3Entry.getKey().getBukkitEntity();
                    return Map.entry(player, vector);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                List<Location> hitBlocks = explosion.getToBlow().stream().map(blockPos -> new Location(level.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ())).collect(Collectors.toList());


                return entityBehaviour.ignoreExplosion(entity.getBukkitEntity(), explosionLocation, radius, source, createFire, hitPlayers, hitBlocks);
            }
            , vanillaLogic);
    }

    public static void onTick (Entity entity){
        evaluateCallback(getBehaviour(entity, org.bukkit.entity.Entity.class, EntityBehaviour.class), entityBehaviour -> entityBehaviour.onTick(entity.getBukkitEntity()), () -> {});
    }
    public static boolean canChangeDimensions(Entity entity, Supplier<Boolean> vanillaLogic) {
        return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Entity.class, EntityBehaviour.class), entityBehaviour -> entityBehaviour.canChangeDimensions((org.bukkit.entity.LivingEntity) entity.getBukkitEntity()), vanillaLogic);
    }

    // ### Living Entity Section ###

    public static class Living {
        public static boolean isSensitiveToWater(LivingEntity entity, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class), entityBehaviour -> entityBehaviour.isSensitiveToWater((org.bukkit.entity.LivingEntity) entity.getBukkitEntity()), vanillaLogic);
        }

        public static void onItemPickup(LivingEntity entity, ItemEntity item, Runnable vanillaLogic) {
            evaluateVoid(getBehaviour(entity, org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class), livingEntityBehaviour -> livingEntityBehaviour.onItemPickup((org.bukkit.entity.LivingEntity) entity.getBukkitLivingEntity(), (Item) item.getBukkitEntity()), vanillaLogic);
        }

        public static boolean canDisableShield(LivingEntity entity, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class), entityBehaviour -> entityBehaviour.canDisableShield((org.bukkit.entity.LivingEntity) entity.getBukkitEntity()), vanillaLogic);
        }

        public static float waterDamage(LivingEntity entity, Supplier<Float> vanillaLogic) {
            return evaluate(getBehaviour(entity, org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class), entityBehaviour -> entityBehaviour.waterDamage((org.bukkit.entity.LivingEntity) entity.getBukkitEntity()), vanillaLogic, Converter.DummyConverter.getInstance(Float.class));
        }

        public static boolean canAttackType(LivingEntity entity, net.minecraft.world.entity.EntityType<?> entityType, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class), entityBehaviour -> entityBehaviour.canAttackType((org.bukkit.entity.Mob) entity.getBukkitEntity(), CraftEntityType.minecraftToBukkit(entityType)), vanillaLogic);
        }
    }

    public static class Mob {
        public static boolean canFireProjectileWeapon(net.minecraft.world.entity.Mob entity, ProjectileWeaponItem projectileWeaponItem, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Mob.class, MobBehaviour.class), entityBehaviour -> entityBehaviour.canFireProjectileWeapon((org.bukkit.entity.Mob) entity.getBukkitEntity(), CraftMagicNumbers.getMaterial(projectileWeaponItem)), vanillaLogic);
        }

        public static void ate(net.minecraft.world.entity.Mob entity, Runnable vanillaLogic){
            evaluateVoid(getBehaviour(entity, org.bukkit.entity.Mob.class, MobBehaviour.class), entityBehaviour -> entityBehaviour.ate((org.bukkit.entity.Mob) entity.getBukkitEntity()), vanillaLogic);
        }

        public static InteractionResult mobInteract(net.minecraft.world.entity.Mob entity, Player player, InteractionHand hand, Supplier<InteractionResult> vanillaLogic) {
            return evaluate(getBehaviour(entity, org.bukkit.entity.Mob.class, MobBehaviour.class), entityBehaviour ->
                    entityBehaviour.mobInteract((org.bukkit.entity.Mob) entity.getBukkitEntity(), (CraftPlayer) player.getBukkitEntity(), hand == InteractionHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND),
                vanillaLogic,
                Converter.InteractionResult.INSTANCE
            );
        }

        public static boolean canHoldItem(net.minecraft.world.entity.Mob entity, ItemStack stack, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Mob.class, MobBehaviour.class), entityBehaviour -> entityBehaviour.canHoldItem((org.bukkit.entity.Mob) entity.getBukkitEntity(), stack.asBukkitMirror()), vanillaLogic);
        }

        public static boolean wantsToPickup(net.minecraft.world.entity.Mob entity, ItemStack stack, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Mob.class, MobBehaviour.class), entityBehaviour -> entityBehaviour.wantsToPickUp((org.bukkit.entity.Mob) entity.getBukkitEntity(), stack.asBukkitMirror()), vanillaLogic);
        }

        public static boolean removeWhenFarAway(net.minecraft.world.entity.Mob entity, double distanceSquared, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Mob.class, MobBehaviour.class), entityBehaviour -> entityBehaviour.removeWhenFarAway((org.bukkit.entity.Mob) entity.getBukkitEntity(), distanceSquared), vanillaLogic);
        }

        public static boolean canBeLeashed(net.minecraft.world.entity.Mob entity, Player player, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, org.bukkit.entity.Mob.class, MobBehaviour.class), entityBehaviour -> entityBehaviour.canBeLeashed((org.bukkit.entity.Mob) entity.getBukkitEntity(), (CraftPlayer) player.getBukkitEntity()), vanillaLogic);
        }
    }

    public static class Animal {
        public static boolean canMate(net.minecraft.world.entity.animal.Animal entity, net.minecraft.world.entity.animal.Animal other, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, Animals.class, AnimalEntityBehaviour.class), entityBehaviour -> entityBehaviour.canMate((Animals) entity.getBukkitLivingEntity(), (Animals) other.getBukkitLivingEntity()), vanillaLogic);
        }

        public static void breedCallback(net.minecraft.world.entity.animal.Animal parent1, net.minecraft.world.entity.animal.Animal parent2, net.minecraft.world.entity.AgeableMob child){
            evaluateCallback(getBehaviour(parent1, Animals.class, AnimalEntityBehaviour.class), entityBehaviour -> entityBehaviour.onBreed((Animals) parent1.getBukkitEntity(), (Animals) parent2.getBukkitEntity(), (Ageable) child.getBukkitEntity()), () -> {});
        }

        public static boolean isFood(net.minecraft.world.entity.animal.Animal entity, ItemStack stack, Supplier<Boolean> vanillaLogic) {
            return evaluateBoolean(getBehaviour(entity, Animals.class, AnimalEntityBehaviour.class), entityBehaviour -> entityBehaviour.isFood((Animals) entity.getBukkitLivingEntity(), stack.asBukkitMirror()), vanillaLogic);
        }
    }

    @Nullable
    public static <V extends Entity, E extends org.bukkit.entity.Entity, B extends EntityBehaviour<E>> B getBehaviour(V nmsEntity, Class<? extends E> bukkitType, Class<? extends B> behaviourType){
        if(nmsEntity.behaviour == null)
            return null;
        if (!behaviourType.isAssignableFrom(nmsEntity.behaviour.getClass()))
            return null;
        if(!bukkitType.isAssignableFrom(nmsEntity.getBukkitEntity().getClass()))
            return null;
        return behaviourType.cast(nmsEntity.behaviour);
    }

    private static EntityType getType(Entity entity) {
        return entity.getBukkitEntity().getType();
    }
}
