package de.verdox.mccreativelab.ai.behavior;

import net.minecraft.world.entity.ai.behavior.OneShot;
import org.bukkit.entity.LivingEntity;

public class CraftOneShotBehavior<E extends LivingEntity> extends CraftControlledBehavior<E, OneShot<?>> implements OneShotBehavior<E> {
    public CraftOneShotBehavior(OneShot<?> handle) {
        super(handle);
    }
}
