package io.papermc.paper.event.data;

import com.google.gson.JsonObject;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerAdvancementDataEventTest {
    @Test
    void loginLoadEventsExposeTheStaticHandlerListForBothConstructors() {
        var uuid = UUID.randomUUID();
        var synchronous = new PlayerAdvancementDataLoadEvent(uuid);
        var login = new PlayerAdvancementDataLoadEvent(true, uuid);
        assertNotNull(login.getHandlers());
        assertSame(PlayerAdvancementDataLoadEvent.getHandlerList(), login.getHandlers());
        assertSame(synchronous.getHandlers(), login.getHandlers());
        assertTrue(login.isAsynchronous());
        assertFalse(synchronous.isAsynchronous());
        assertEquals(uuid, login.getPlayerUUID());
        var replacement = new JsonObject();
        login.setSerializedAdvancements(replacement);
        assertSame(replacement, login.getSerializedAdvancements());
    }

    @Test
    void saveEventsExposeTheirOwnHandlersAndRetainCancellation() {
        var uuid = UUID.randomUUID();
        var data = new JsonObject();
        var save = new PlayerAdvancementDataSaveEvent(uuid, data);
        var async = new PlayerAdvancementDataSaveEvent(true, uuid, data);
        assertNotNull(save.getHandlers());
        assertSame(PlayerAdvancementDataSaveEvent.getHandlerList(), save.getHandlers());
        assertSame(save.getHandlers(), async.getHandlers());
        assertNotSame(PlayerAdvancementDataLoadEvent.getHandlerList(), save.getHandlers());
        assertSame(data, save.getSerializedAdvancements());
        assertFalse(save.isCancelled());
        save.setCancelled(true);
        assertTrue(save.isCancelled());
        assertTrue(async.isAsynchronous());
    }
}
