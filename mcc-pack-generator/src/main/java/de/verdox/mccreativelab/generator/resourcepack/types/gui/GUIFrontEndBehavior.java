package de.verdox.mccreativelab.generator.resourcepack.types.gui;

import de.verdox.mccreativelab.generator.resourcepack.types.gui.element.active.ActiveGUIElement;
import de.verdox.mccreativelab.platform.GeneratorPlatformHelper;
import de.verdox.mccreativelab.wrapper.entity.types.MCCPlayer;
import de.verdox.mccreativelab.wrapper.event.MCCCancellable;
import de.verdox.mccreativelab.wrapper.inventory.MCCContainer;
import de.verdox.mccreativelab.wrapper.inventory.MCCContainerCloseReason;
import de.verdox.mccreativelab.wrapper.inventory.types.container.MCCPlayerInventory;
import de.verdox.mccreativelab.wrapper.item.MCCItemStack;
import de.verdox.mccreativelab.wrapper.platform.MCCPlatform;
import de.verdox.mccreativelab.wrapper.platform.MCCTask;
import de.verdox.mccreativelab.wrapper.typed.MCCDataComponentTypes;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class GUIFrontEndBehavior {
    private static final long SHIFT_COOLDOWN_MILLIS = 20;
    private final ActiveGUI activeGUI;
    private final Set<Audience> viewers;
    private long lastShift = System.currentTimeMillis();
    private FrontEndRenderer frontEndRenderer;
    private MCCTask updateTask;
    private boolean setup;
    private final Set<UUID> inventoryUpdateWhitelist = new HashSet<>();

    public GUIFrontEndBehavior(ActiveGUI activeGUI) {
        this.activeGUI = activeGUI;
        viewers = this.activeGUI.getViewersNoCopy();
    }

    public ActiveGUI getActiveGUI() {
        return activeGUI;
    }

    public void updateFrontEnd() {
        getFrontEndRenderer().offer(() -> {
            try {
                if (viewers.isEmpty()) {
                    return;
                }
                if (activeGUI.getComponentRendered().whileOpen != null) {
                    activeGUI.getComponentRendered().whileOpen.accept(activeGUI);
                }
                activeGUI.forEachElementBehavior((activeGUIRenderedRenderedElementBehavior, rendered, audience) -> activeGUIRenderedRenderedElementBehavior.whileOpen(activeGUI, rendered, audience), false);
                activeGUI.forEachGUIElementBehavior((guiElementBehavior, activeGUIElement) -> guiElementBehavior.whileOpen(activeGUI, activeGUIElement));

                Component lastRendering = activeGUI.getLastRendered();
                Component newRendering = activeGUI.render();
                if (newRendering.equals(lastRendering) && setup) {
                    return;
                }

                Set<Audience> newViewers = MCCPlatform.getInstance().getTaskManager().syncAndWait(mccTask -> {
                    Iterator<Audience> iterator = viewers.iterator();
                    Set<Audience> viewersToAdd = new HashSet<>();

                    while (iterator.hasNext()) {
                        UUID uuid = GeneratorPlatformHelper.INSTANCE.get().getUUIDOfAudience(iterator.next());
                        if (uuid == null) {
                            continue;
                        }
                        MCCPlayer player = MCCPlatform.getInstance().getOnlinePlayer(uuid);
                        if (player == null) {
                            continue;
                        }
                        if (!activeGUI.equals(ActiveGUI.PlayerGUIData.getCurrentActiveGUI(player))) {
                            iterator.remove();
                            continue;
                        }

                        var itemAtCursor = player.getCursorProperty().get().copy();
                        openUpdatedInventory(player, itemAtCursor, newRendering);
                        viewersToAdd.add(player);
                    }
                    return viewersToAdd;
                });

                viewers.addAll(newViewers);
            } finally {
                setup = true;
            }
        });
    }

    public void clickBehavior(GUIClickAction clickAction) {
        try {
            MCCPlayer player = clickAction.getEntityClicking();
            if (!activeGUI.getViewers().contains(player)) {
                clickAction.setCancelled(true);
                return;
            }

            int rawSlot = clickAction.getClickedSlot();
            if (activeGUI.getIndexToClickableItemMapping().containsKey(rawSlot)) {
                runClickableItemLogic(clickAction, rawSlot, player);
                return;
            }


            ActiveGUIElement<?> activeGUIElement = activeGUI.getGuiElementsBySlot().getOrDefault(rawSlot, null);


            if (activeGUI.getComponentRendered().isSlotBlocked(rawSlot) && clickAction.isUpperInventoryClicked()) {
                clickAction.setCancelled(true);
                if (activeGUIElement != null) {
                    activeGUIElement.onClick(clickAction, rawSlot % 9, rawSlot / 9);
                }
            }
            // Prevent inventory clicks if is using player slots
            if (activeGUI.getComponentRendered().isUsePlayerSlots() && !clickAction.isUpperInventoryClicked()) {
                clickAction.setCancelled(true);
            }

            if (clickAction.getClickType().isShiftClick() && !clickAction.isUpperInventoryClicked()) {
                if (performShiftClickLogic(clickAction, player)) {
                    return;
                }
            }

            if (activeGUI.getComponentRendered().getClickConsumer() != null) {
                activeGUI.getComponentRendered().getClickConsumer().accept(clickAction, activeGUI);
            }
        } finally {
            activeGUI.forceUpdate();
        }
    }

    private boolean performShiftClickLogic(GUIClickAction clickAction, MCCPlayer player) {
        if (activeGUI.getComponentRendered().isUsePlayerSlots() || System.currentTimeMillis() - lastShift < SHIFT_COOLDOWN_MILLIS) {
            clickAction.setCancelled(true);
            return true;
        }
        lastShift = System.currentTimeMillis();


        shiftItemToInventory(player.getInventory(), activeGUI.getVanillaInventory(), clickAction.getClickedSlot(), activeGUI.getComponentRendered().getBlockedSlots());
        clickAction.setCancelled(true);
        return false;
    }

    private void runClickableItemLogic(GUIClickAction clickAction, int rawSlot, MCCPlayer player) {
        clickAction.setCancelled(true);

        ClickableItem clickableItem = activeGUI.getIndexToClickableItemMapping().get(rawSlot);
        clickableItem.getOnClick().accept(clickAction, activeGUI);

        if (clickableItem.getBuilder().clearGUIStackAndClose) {
            PlayerGUIStack.load(player).clear();

            player.closeCurrentInventory(new MCCContainerCloseReason("close_active_gui"));
        } else if (clickableItem.getBuilder().popGUIStack) {
            PlayerGUIStack.load(player).popAndOpenLast(player, activeGUI);
        }

        if (activeGUI.getComponentRendered().getClickConsumer() != null) {
            activeGUI.getComponentRendered().getClickConsumer().accept(clickAction, activeGUI);
        }
    }

    public void onDrag(MCCPlayer clicker, List<Integer> involvedSlots, MCCCancellable dragAction) {
        if (!activeGUI.getViewers().contains(clicker)) {
            return;
        }
        var rawSlotUsed = involvedSlots.stream().anyMatch(activeGUI.getComponentRendered()::isSlotBlocked);
        if (rawSlotUsed) {
            dragAction.setCancelled(true);
        }
    }

    /**
     * Is called when the gui is closed. Returns true if the close was successful.
     * @param closingPlayer the player viewing the gui
     * @param closeReason the reason it is closed
     * @return true if it was closed successfully
     */
    public boolean onClose(MCCPlayer closingPlayer, MCCContainerCloseReason closeReason) {
        if (!activeGUI.getViewers().contains(closingPlayer)) {
            return true;
        }

        // A player is in this update whitelist if we reopen the updated inventory to the player
        // Since this forced InventoryCloseEvent should not be
        synchronized (inventoryUpdateWhitelist) {
            if (inventoryUpdateWhitelist.contains(closingPlayer.getUUID())) {
                return false;
            }
        }
        removePlayerFromGUI(closingPlayer, closeReason);
        return true;
    }

    public void removePlayerFromGUI(MCCPlayer player, MCCContainerCloseReason reason) {
        viewers.remove(player);
        if (activeGUI.equals(ActiveGUI.PlayerGUIData.getCurrentActiveGUI(player))) {
            ActiveGUI.PlayerGUIData.trackCurrentActiveGUI(player, null);
        }
        player.syncInventory();
    }

    public void openToPlayer(MCCPlayer player) {
        ActiveGUI currentActiveGUI = ActiveGUI.PlayerGUIData.getCurrentActiveGUI(player);
        if (currentActiveGUI != null) {
            if (getActiveGUI().equals(currentActiveGUI)) {
                //ActiveGUI.LOGGER.info(activeGUI.getComponentRendered().key() + ": Could not open to player");
                return;
            }

            //ActiveGUI.LOGGER.info(currentActiveGUI.getComponentRendered().key() + ": Remove player from gui");
            removePlayerFromGUI(player, MCCContainerCloseReason.OPEN_NEW);
        }
        //ActiveGUI.LOGGER.info(activeGUI.getComponentRendered().key() + ": Add player to gui");
        addPlayerToGUI(player);
    }

    private void addPlayerToGUI(MCCPlayer player) {
        if (!viewers.contains(player)) {
            ActiveGUI.PlayerGUIData.trackCurrentActiveGUI(player, activeGUI);
            viewers.add(player);
        }
        startFrontEnd();
        if (activeGUI.getComponentRendered().onOpen != null) {
            activeGUI.getComponentRendered().onOpen.accept(activeGUI);
        }
    }

    private void startFrontEnd() {
        if (updateTask != null && updateTask.isRunning())
            return;
        onFrontendRenderStart();

        frontEndRenderer = new FrontEndRenderer(activeGUI);
        frontEndRenderer.start();

        updateTask = MCCPlatform.getInstance().getTaskManager().runTimerAsync(mccTask -> {
            if (viewers.isEmpty()) {
                updateTask.cancel();
                onFrontendClose();
                frontEndRenderer.stopRenderer();
                return;
            }
            if (activeGUI.getComponentRendered().updateInterval > 0) {
                activeGUI.forceUpdate();
            }

        }, 0, activeGUI.getComponentRendered().updateInterval > 0 ? activeGUI.getComponentRendered().updateInterval * 50L : 1, TimeUnit.MILLISECONDS);

        if (activeGUI.getComponentRendered().updateInterval < 0) {
            MCCPlatform.getInstance().getTaskManager().runAsync(mccTask -> activeGUI.forceUpdate());
        }
    }

    public abstract void onFrontendClose();

    public abstract void onFrontendRenderStart();

    public FrontEndRenderer getFrontEndRenderer() {
        return frontEndRenderer;
    }

    private void shiftItemToInventory(MCCPlayerInventory sourceInventory, MCCContainer targetInventory, int sourceSlot, Set<Integer> blockedSlots) {
        int targetSlot = 0;

        MCCItemStack itemStack = sourceInventory.getItem(sourceSlot);

        // Überprüfe, ob das Item im Quell-Slot vorhanden ist
        if (itemStack == null) {
            return; // Beende die Funktion, wenn kein Item im Quell-Slot ist
        }

        // Überprüfe, ob alle Slots blockiert sind
        boolean allSlotsBlocked = true;
        for (int i = 0; i < targetInventory.getSize(); i++) {
            if (!blockedSlots.contains(i)) {
                allSlotsBlocked = false;
                break;
            }
        }

        if (allSlotsBlocked)
            return; // Beende die Funktion, wenn alle Slots blockiert sind

        // Iteriere über alle Slots im Ziel-Inventar
        for (int i = 0; i < targetInventory.getSize(); i++) {
            // Überprüfe, ob der aktuelle Slot blockiert ist
            if (blockedSlots.contains(i))
                continue; // Überspringe den aktuellen Slot und gehe zum nächsten

            MCCItemStack currentSlotItem = targetInventory.getItem(i);

            // Überprüfe, ob der aktuelle Slot leer ist
            if (currentSlotItem == null) {
                targetInventory.setItem(i, itemStack);
                sourceInventory.setItem(sourceSlot, null); // Entferne das Item aus dem Quell-Inventar
                return; // Das Item wurde verschoben, beende die Funktion
            }

            // Überprüfe, ob das Item im aktuellen Slot vom gleichen Typ und stapelbar ist
            if (currentSlotItem.isSimilar(itemStack) && currentSlotItem.getAmount() < currentSlotItem.components().get(MCCDataComponentTypes.MAX_STACK_SIZE.get())) {
                int spaceLeft = currentSlotItem.components().get(MCCDataComponentTypes.MAX_STACK_SIZE.get()) - currentSlotItem.getAmount();
                int amountToMove = Math.min(spaceLeft, itemStack.getAmount());

                currentSlotItem.setAmount(currentSlotItem.getAmount() + amountToMove);
                itemStack.setAmount(itemStack.getAmount() - amountToMove);

                if (itemStack.getAmount() == 0) {
                    sourceInventory.setItem(sourceSlot, null); // Entferne das Item aus dem Quell-Inventar
                    return; // Das gesamte Item wurde verschoben, beende die Funktion
                }
            }

            targetSlot++;
        }

        // Falls alle Slots blockiert waren oder kein passender Slot gefunden wurde,
        // wird das Item nicht verschoben
    }

    private void openUpdatedInventory(MCCPlayer player, MCCItemStack itemAtCursor, Component rendering) {
        synchronized (inventoryUpdateWhitelist) {
            inventoryUpdateWhitelist.add(player.getUUID());
            try {
                var menu = activeGUI.getMenuCreatorInstance().createMenuForPlayer(player, rendering);
                if (itemAtCursor != null) {
                    if (menu != null && !itemAtCursor.getType().isEmpty() && !activeGUI.getComponentRendered().isUsePlayerSlots()) {
                        player.getInventory().removeItem(itemAtCursor);
                        player.getCursorProperty().set(itemAtCursor);
                    }
                }
            } finally {
                inventoryUpdateWhitelist.remove(player.getUUID());
            }
        }
    }
}
