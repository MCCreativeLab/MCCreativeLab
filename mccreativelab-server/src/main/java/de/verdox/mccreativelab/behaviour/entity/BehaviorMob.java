package de.verdox.mccreativelab.behaviour.entity;

import de.verdox.mccreativelab.behavior.entity.MobBehaviour;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public interface BehaviorMob extends BehaviorLivingEntity {

    default boolean mcc$canFireProjectileWeapon(ProjectileWeaponItem weapon) {
        var behaviour = BehaviorEntity.getBehaviour(asMob(), org.bukkit.entity.Mob.class, MobBehaviour.class);
        var craftProjectileWeapon = CraftMagicNumbers.getMaterial(weapon);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canFireProjectileWeapon((org.bukkit.entity.Mob) asMob().getBukkitLivingEntity(), craftProjectileWeapon), () -> asMob().canFireProjectileWeapon(weapon));
    }

    default void mcc$ate() {
        var behaviour = BehaviorEntity.getBehaviour(asMob(), org.bukkit.entity.Mob.class, MobBehaviour.class);
        BehaviourUtil.evaluateVoid(behaviour, entityBehavior -> entityBehavior.ate((org.bukkit.entity.Mob) asMob().getBukkitLivingEntity()), () -> asMob().ate());
    }

    default boolean mcc$canHoldItem(ItemStack stack) {
        var behaviour = BehaviorEntity.getBehaviour(asMob(), org.bukkit.entity.Mob.class, MobBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canHoldItem((org.bukkit.entity.Mob) asMob().getBukkitLivingEntity(), stack.asBukkitMirror()), () -> asMob().canHoldItem(stack));
    }

    default boolean mcc$wantsToPickUp(ServerLevel serverLevel, ItemStack stack) {
        var behaviour = BehaviorEntity.getBehaviour(asMob(), org.bukkit.entity.Mob.class, MobBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.wantsToPickUp((org.bukkit.entity.Mob) asMob().getBukkitLivingEntity(), stack.asBukkitMirror()), () -> asMob().wantsToPickUp(serverLevel, stack));
    }

    default boolean mcc$removeWhenFarAway(double distanceSquared) {
        var behaviour = BehaviorEntity.getBehaviour(asMob(), org.bukkit.entity.Mob.class, MobBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.removeWhenFarAway((org.bukkit.entity.Mob) asMob().getBukkitLivingEntity(), distanceSquared), () -> asMob().removeWhenFarAway(distanceSquared));
    }

    default boolean mcc$shouldDespawnInPeaceful() {
        var behaviour = BehaviorEntity.getBehaviour(asMob(), org.bukkit.entity.Mob.class, MobBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.shouldDespawnInPeaceful((org.bukkit.entity.Mob) asMob().getBukkitLivingEntity()), () -> asMob().shouldDespawnInPeaceful());
    }

    default int mcc$getAmbientSoundInterval() {
        var behaviour = BehaviorEntity.getBehaviour(asMob(), org.bukkit.entity.Mob.class, MobBehaviour.class);
        return BehaviourUtil.evaluateInteger(behaviour, entityBehavior -> entityBehavior.getAmbientSoundInterval((org.bukkit.entity.Mob) asMob().getBukkitLivingEntity()), () -> asMob().getAmbientSoundInterval());
    }

    default Mob asMob(){
        return (Mob) asEntity();
    }
}
