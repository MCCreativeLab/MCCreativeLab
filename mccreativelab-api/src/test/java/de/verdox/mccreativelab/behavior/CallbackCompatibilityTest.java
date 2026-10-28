package de.verdox.mccreativelab.behavior;

import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CallbackCompatibilityTest {
    @Test
    void callbackRunsVanillaExactlyOnce() {
        AtomicInteger calls = new AtomicInteger();
        assertNull(BehaviourResult.Callback.DEFAULT_INSTANCE.evaluateReturnValue(() -> {
            calls.incrementAndGet();
            return null;
        }));
        assertEquals(1, calls.get());
        assertFalse(BehaviourResult.Callback.DEFAULT_INSTANCE.replaceVanillaLogic());
    }

    @Test
    void positionAwareRandomTickCheckDelegatesToStateCallback() {
        BlockBehaviour behaviour = new BlockBehaviour() {
            @Override
            public BehaviourResult.@NonNull Bool isBlockDataRandomlyTicking(@NonNull BlockData data) {
                return bool(true);
            }
        };
        assertTrue(behaviour.isBlockRandomlyTicking(mock(Block.class), mock(BlockData.class)).evaluateReturnValue(() -> false));
    }

    @Test
    void replacementVoidHasNoRequiredReturnValue() {
        AtomicInteger calls = new AtomicInteger();
        assertNull(new BehaviourResult.Void(BehaviourResult.Type.REPLACE_VANILLA).evaluateReturnValue(() -> {
            calls.incrementAndGet();
            return null;
        }));
        assertEquals(0, calls.get());
        assertNull(BehaviourResult.Void.DEFAULT_INSTANCE.evaluateReturnValue(() -> {
            calls.incrementAndGet();
            return null;
        }));
        assertEquals(1, calls.get());
    }

}
