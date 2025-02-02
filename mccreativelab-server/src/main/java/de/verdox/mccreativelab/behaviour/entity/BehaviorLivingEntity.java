package de.verdox.mccreativelab.behaviour.entity;

import de.verdox.mccreativelab.behavior.entity.LivingEntityBehaviour;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.entity.CraftEntityType;

public interface BehaviorLivingEntity extends BehaviorEntity {
    default boolean mcc$isSensitiveToWater() {
        var behaviour = BehaviorEntity.getBehaviour(asLivingEntity(), org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.isSensitiveToWater(asLivingEntity().getBukkitLivingEntity()), () -> asLivingEntity().isSensitiveToWater());
    }

    default boolean mcc$canAttackType(EntityType<?> entityType) {
        var behaviour = BehaviorEntity.getBehaviour(asLivingEntity(), org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canAttackType(CraftEntityType.minecraftToBukkit(entityType)), () -> asLivingEntity().canAttackType(entityType));
    }

    default boolean mcc$canAttack(LivingEntity entity) {
        var behaviour = BehaviorEntity.getBehaviour(asLivingEntity(), org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canAttack(asLivingEntity().getBukkitLivingEntity(), entity.getBukkitLivingEntity()), () -> asLivingEntity().canAttack(entity));
    }

    default boolean mcc$canBeNameTagged() {
        var behaviour = BehaviorEntity.getBehaviour(asLivingEntity(), org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canBeNameTagged(asLivingEntity().getBukkitLivingEntity()), () -> asLivingEntity().canBeNameTagged());
    }

    default boolean mcc$canPickUpLoot() {
        var behaviour = BehaviorEntity.getBehaviour(asLivingEntity(), org.bukkit.entity.LivingEntity.class, LivingEntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canPickUpLoot(asLivingEntity().getBukkitLivingEntity()), () -> asLivingEntity().canPickUpLoot());
    }

    default LivingEntity asLivingEntity(){
        return (LivingEntity) asEntity();
    }
}
