package de.verdox.mccreativelab.generator;

import de.verdox.mccreativelab.event.GUICloseEvent;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.PlayerGUIStack;
import de.verdox.mccreativelab.impl.paper.platform.converter.BukkitAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerGuiStackListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClose(GUICloseEvent e) {
        PlayerGUIStack playerGUIStack = PlayerGUIStack.load(BukkitAdapter.wrap(e.getPlayer()));
        playerGUIStack.onActiveGuiClose(e.getActiveGUI(), PaperGUIFrontEndBehavior.to(e.getReason()));
    }
}
