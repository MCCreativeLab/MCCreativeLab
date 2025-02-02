package de.verdox.mccreativelab.behaviour.entity;

import de.verdox.mccreativelab.behavior.entity.EntityBehaviour;
import de.verdox.mccreativelab.behavior.entity.LivingEntityBehaviour;
import de.verdox.mccreativelab.behavior.entity.MobBehaviour;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.jetbrains.annotations.Nullable;

public interface BehaviorEntity extends BehaviourUtil {
    default boolean mcc$fireImmune() {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.fireImmune(asEntity().getBukkitEntity()), () -> asEntity().fireImmune());
    }

    default boolean mcc$canCollideWith(Entity entity) {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canCollideWith(asEntity().getBukkitEntity(), entity.getBukkitEntity()), () -> asEntity().canCollideWith(entity));
    }

    default boolean mcc$couldAcceptPassenger() {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.couldAcceptPassenger(asEntity().getBukkitEntity()), () -> asEntity().couldAcceptPassenger());
    }

    default boolean mcc$isAttackable() {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.isAttackable(asEntity().getBukkitEntity()), () -> asEntity().isAttackable());
    }

    default int mcc$getMaxFallDistance() {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        return BehaviourUtil.evaluateInteger(behaviour, entityBehavior -> entityBehavior.getMaxFallDistance(asEntity().getBukkitEntity()), () -> asEntity().getMaxFallDistance());
    }

    default void mcc$onTick() {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        BehaviourUtil.evaluateCallback(behaviour, entityBehavior -> entityBehavior.onTick(asEntity().getBukkitEntity()), () -> {});
    }

    default void mcc$readAdditionalSaveData(){
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        BehaviourUtil.evaluateCallback(behaviour, entityBehavior -> entityBehavior.readAdditionalSaveData(asEntity().getBukkitEntity(), asEntity().getBukkitEntity().getPersistentDataContainer()), () -> {});
    }

    default void mcc$addAdditionalSaveData(){
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        BehaviourUtil.evaluateCallback(behaviour, entityBehavior -> entityBehavior.addAdditionalSaveData(asEntity().getBukkitEntity(), asEntity().getBukkitEntity().getPersistentDataContainer()), () -> {});
    }

    default void mcc$onBelowWorld() {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        BehaviourUtil.evaluateVoid(behaviour, entityBehavior -> entityBehavior.onBelowWorld(asEntity().getBukkitEntity()), () -> asEntity().onBelowWorld());
    }

    default boolean mcc$canBeLeashed() {
        EntityBehaviour<org.bukkit.entity.Entity> behaviour = getBehaviour(asEntity(), org.bukkit.entity.Entity.class, EntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canBeLeashed(asEntity().getBukkitEntity()), () -> {
            if(asEntity() instanceof Leashable leashable)
                return leashable.canBeLeashed();
            return false;
        });
    }

    @Nullable
    public static <V extends Entity, E extends org.bukkit.entity.Entity, B extends EntityBehaviour<E>> B getBehaviour(V nmsEntity, Class<? extends E> bukkitType, Class<? extends B> behaviourType) {
        if (nmsEntity.behaviour == null)
            return null;
        if (!behaviourType.isAssignableFrom(nmsEntity.behaviour.getClass()))
            return null;
        if (!bukkitType.isAssignableFrom(nmsEntity.getBukkitEntity().getClass()))
            return null;
        return behaviourType.cast(nmsEntity.behaviour);
    }

    default Entity asEntity(){
        return (Entity) this;
    }
}
