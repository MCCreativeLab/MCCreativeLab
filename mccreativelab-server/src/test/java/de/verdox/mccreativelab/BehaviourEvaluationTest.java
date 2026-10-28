package de.verdox.mccreativelab;

import de.verdox.mccreativelab.behavior.BehaviourResult;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@VanillaFeature
class BehaviourEvaluationTest {
    private static final BehaviourUtil.Converter<String, Integer> BROKEN_CONVERSION = new BehaviourUtil.Converter<>() {
        public Integer bukkitToNMS(String value) { return Integer.valueOf(value); }
        public String nmsToBukkitValue(Integer value) { throw new IllegalArgumentException("Deliberate test failure"); }
    };

    @Test
    void conversionFailureDoesNotRunVanillaTwice() {
        AtomicInteger calls = new AtomicInteger();
        int result = BehaviourUtil.evaluate(new Object(),
                ignored -> new BehaviourResult.Object<String>(null, BehaviourResult.Type.USE_VANILLA_ONLY),
                () -> { calls.incrementAndGet(); return 42; }, BROKEN_CONVERSION);
        assertEquals(42, result);
        assertEquals(1, calls.get());
    }

    @Test
    void nativeFailureIsPropagatedWithoutRetryingSideEffects() {
        AtomicInteger calls = new AtomicInteger();
        assertThrows(IllegalStateException.class, () -> BehaviourUtil.evaluate(new Object(),
                ignored -> new BehaviourResult.Object<String>(null, BehaviourResult.Type.USE_VANILLA_ONLY),
                () -> { calls.incrementAndGet(); throw new IllegalStateException("Native failure"); }, BROKEN_CONVERSION));
        assertEquals(1, calls.get());
    }
}
