package de.verdox.mccreativelab.generator;

import de.verdox.mccreativelab.event.GUICloseEvent;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.ActiveGUI;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.GUIClickAction;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.GUIFrontEndBehavior;
import de.verdox.mccreativelab.impl.paper.platform.converter.BukkitAdapter;
import de.verdox.mccreativelab.wrapper.entity.types.MCCPlayer;
import de.verdox.mccreativelab.wrapper.event.MCCCancellable;
import de.verdox.mccreativelab.wrapper.inventory.MCCContainerCloseReason;
import de.verdox.mccreativelab.wrapper.inventory.MCCInventoryAction;
import de.verdox.mccreativelab.wrapper.inventory.MCCInventoryClickType;
import de.verdox.mccreativelab.wrapper.inventory.MCCInventorySlotType;
import de.verdox.mccreativelab.wrapper.platform.MCCPlatform;
import net.minecraft.world.Container;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperGUIFrontEndBehavior extends GUIFrontEndBehavior implements Listener {
    private final JavaPlugin javaPlugin;

    public PaperGUIFrontEndBehavior(JavaPlugin javaPlugin, ActiveGUI activeGUI) {
        super(activeGUI);
        this.javaPlugin = javaPlugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (!isRightContainer(e.getInventory())) {
            return;
        }
        clickBehavior(to(e));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent e) {
        if (!isRightContainer(e.getInventory())) {
            return;
        }
        onDrag(BukkitAdapter.wrap((Player) e.getWhoClicked()), e.getInventorySlots().stream().toList(), new MCCCancellable() {
            @Override
            public boolean isCancelled() {
                return e.isCancelled();
            }

            @Override
            public void setCancelled(boolean cancelled) {
                e.setCancelled(cancelled);
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent e) {
        if (!isRightContainer(e.getInventory())) {
            return;
        }
        onClose(BukkitAdapter.wrap(e.getPlayer()), to(e.getReason()));
        Bukkit.getPluginManager().callEvent(new GUICloseEvent((Player) e.getPlayer(), getActiveGUI(), e.getReason()));
    }

    @Override
    public void onFrontendClose() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onFrontendRenderStart() {
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    private boolean isRightContainer(Inventory inventory) {
        Container container = ((CraftInventory) inventory).getInventory();
        Container fromGUI = MCCPlatform.getInstance().getConversionService().unwrap(getActiveGUI().getVanillaInventory());

        return container.equals(fromGUI);
    }

    public static MCCContainerCloseReason to(InventoryCloseEvent.Reason reason) {
        return switch (reason) {
            case UNKNOWN -> MCCContainerCloseReason.UNKNOWN;
            case TELEPORT -> MCCContainerCloseReason.TELEPORT;
            case CANT_USE -> MCCContainerCloseReason.CANT_USE;
            case UNLOADED -> MCCContainerCloseReason.UNLOADED;
            case OPEN_NEW -> MCCContainerCloseReason.OPEN_NEW;
            case PLAYER -> MCCContainerCloseReason.CLOSED_BY_VIEWER;
            case DISCONNECT -> MCCContainerCloseReason.DISCONNECT;
            case DEATH -> MCCContainerCloseReason.DEATH;
            case PLUGIN -> MCCContainerCloseReason.CLOSED_BY_SERVER;
        };
    }

    public static GUIClickAction to(InventoryClickEvent e) {
        MCCPlayer player = BukkitAdapter.wrap((Player) e.getWhoClicked());
        boolean isUpperInventoryClicked = e.getView().getTopInventory().equals(e.getClickedInventory());

        return new GUIClickAction(BukkitAdapter.wrap(e.getClickedInventory()), player, isUpperInventoryClicked, e.getSlot(), e.getRawSlot(), BukkitAdapter.wrap(e.getCurrentItem()), BukkitAdapter.wrap(e.getCursor()), to(e.getSlotType()), to(e.getClick()), to(e.getAction())) {
            @Override
            public void setCancelled(boolean cancelled) {
                e.setCancelled(cancelled);
            }

            @Override
            public boolean isCancelled() {
                return e.isCancelled();
            }
        };
    }

    public static MCCInventorySlotType to(InventoryType.SlotType slotType) {
        return MCCInventorySlotType.valueOf(slotType.name());
    }

    public static MCCInventoryClickType to(ClickType clickType) {
        return MCCInventoryClickType.valueOf(clickType.name());
    }

    public static MCCInventoryAction to(InventoryAction inventoryAction) {
        return MCCInventoryAction.valueOf(inventoryAction.name());
    }
}
