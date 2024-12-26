package de.verdox.mccreativelab.generator.resourcepack.types.gui;

import com.google.common.base.Preconditions;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.element.active.ActiveGUIElement;
import de.verdox.mccreativelab.generator.resourcepack.types.rendered.ActiveComponentRendered;
import de.verdox.mccreativelab.platform.GeneratorPlatformHelper;
import de.verdox.mccreativelab.wrapper.entity.types.MCCPlayer;
import de.verdox.mccreativelab.wrapper.inventory.MCCContainer;
import de.verdox.mccreativelab.wrapper.inventory.types.menu.creator.SharedMenuCreatorInstance;
import de.verdox.mccreativelab.wrapper.platform.MCCPlatform;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ActiveGUI extends ActiveComponentRendered<ActiveGUI, CustomGUIBuilder> {
    public static final Logger LOGGER = Logger.getLogger(ActiveGUI.class.getSimpleName());

    private final Map<String, ActiveGUIElement<?>> activeGUIElements = new HashMap<>();
    private final Map<Integer, ActiveGUIElement<?>> guiElementsBySlot = new HashMap<>();
    private final SharedMenuCreatorInstance<?, ?> menuCreatorInstance;
    private final MCCContainer container;
    private GUIFrontEndBehavior GUIFrontEndBehavior;
    private final Map<Integer, ClickableItem> indexToClickableItemMapping = new HashMap<>();

    public ActiveGUI(CustomGUIBuilder customGUIBuilder, @Nullable Consumer<ActiveGUI> initialSetup) {
        super(customGUIBuilder);
        customGUIBuilder.checkInstalled();

        customGUIBuilder.guiElements.forEach((s, guiElement) -> {
            var activeElement = guiElement.toActiveElement(this);
            activeGUIElements.put(s, activeElement);
        });

        this.GUIFrontEndBehavior = GeneratorPlatformHelper.INSTANCE.get().createFrondEndBehavior(this);
        this.menuCreatorInstance = (SharedMenuCreatorInstance<?, ?>) MCCPlatform.getInstance().getContainerFactory().create(customGUIBuilder.getType());
        this.container = menuCreatorInstance.getSharedContainer();


        if (initialSetup != null) {
            initialSetup.accept(this);
            forEachElementBehavior((activeGUIRenderedRenderedElementBehavior, rendered, a) -> activeGUIRenderedRenderedElementBehavior.onOpen(this, rendered, a), true);
            forEachGUIElementBehavior((guiElementBehavior, activeGUIElement) -> guiElementBehavior.onOpen(this, activeGUIElement));
        }
    }

    @Deprecated
    public MCCPlayer getPlayer() {
        return (MCCPlayer) viewers.stream().findAny().orElse(null);
    }

    /**
     * We are able to reopen an existing inventory with a new title without creating a new inventory object.
     * Thus, we use this constructor to link a custom gui to an existing inventory
     *
     * @param customGUIBuilder The customGUIBuilder this belongs to
     * @param inventory        The inventory that is linked to this CustomGUI
     * @param initialSetup     The initial setup
     */
    public ActiveGUI(CustomGUIBuilder customGUIBuilder, MCCContainer inventory, @Nullable Consumer<ActiveGUI> initialSetup) {
        super(customGUIBuilder);
        Preconditions.checkArgument(customGUIBuilder.getType().containerSize() != inventory.getSize(), "The provided container has not the required size of the menu type for this active gui! This gui requires containers with size: " + customGUIBuilder.getType().containerSize());
        customGUIBuilder.checkInstalled();

        customGUIBuilder.guiElements.forEach((s, guiElement) -> {
            var activeElement = guiElement.toActiveElement(this);
            activeGUIElements.put(s, activeElement);
        });

        this.menuCreatorInstance = MCCPlatform.getInstance().getContainerFactory().createShared(customGUIBuilder.getType(), inventory);
        this.container = inventory;

        if (initialSetup != null) {
            initialSetup.accept(this);
            forEachElementBehavior((activeGUIRenderedRenderedElementBehavior, rendered, audience) -> activeGUIRenderedRenderedElementBehavior.onOpen(this, rendered, audience), true);
            forEachGUIElementBehavior((guiElementBehavior, activeGUIElement) -> guiElementBehavior.onOpen(this, activeGUIElement));
        }
    }

    public Map<Integer, ActiveGUIElement<?>> getGuiElementsBySlot() {
        return Map.copyOf(guiElementsBySlot);
    }

    public Map<Integer, ClickableItem> getIndexToClickableItemMapping() {
        return Map.copyOf(indexToClickableItemMapping);
    }

    public final void addClickableItem(int index, ClickableItem clickableItem) {
        this.getVanillaInventory().setItem(index, clickableItem.getStack());
        indexToClickableItemMapping.put(index, clickableItem);
    }

    public final void removeClickableItem(int index) {
        if (!indexToClickableItemMapping.containsKey(index))
            return;
        this.getVanillaInventory().setItem(index, null);
        indexToClickableItemMapping.remove(index);
    }

    public void openToPlayer(MCCPlayer player) {
        this.GUIFrontEndBehavior.openToPlayer(player);
    }

    public final void forEachGUIElementBehavior(BiConsumer<GUIElementBehavior<ActiveGUIElement<?>>, ActiveGUIElement<?>> forEach) {
        this.activeGUIElements.forEach((s, activeGUIElement) -> {
            GUIElementBehavior<ActiveGUIElement<?>> guiElementBehavior = (GUIElementBehavior<ActiveGUIElement<?>>) getComponentRendered()
                .getGuiElementBehaviors().getOrDefault(activeGUIElement.getGuiElement(), null);
            if (guiElementBehavior != null)
                forEach.accept(guiElementBehavior, activeGUIElement);
        });
        //forceUpdate();
    }

    public final <H extends ActiveGUIElement<?>> boolean editGUIElement(String id, Class<? extends H> type, Consumer<H> execution) {
        return edit(id, activeGUIElements, type, h -> {
            h.setVisible(true);
            execution.accept(h);
        });
    }

    void trackGUIInStack(MCCPlayer player) {
        PlayerGUIStack.load(player).trackGUI(this);
    }

    public MCCContainer getVanillaInventory() {
        return container;
    }

    public void placeGuiElementInSlot(int slotIndex, @Nullable ActiveGUIElement<?> activeGUIElement) {
        if (activeGUIElement != null && !this.equals(activeGUIElement.getActiveGUI()))
            throw new IllegalArgumentException("Trying to add gui element that does not belong to this gui.");
        if (activeGUIElement != null)
            guiElementsBySlot.put(slotIndex, activeGUIElement);
        else
            guiElementsBySlot.remove(slotIndex);
    }

    public @Nullable ActiveGUIElement<?> getGUIElementAtIndex(int slotIndex) {
        return guiElementsBySlot.getOrDefault(slotIndex, null);
    }

    @Override
    protected void doUpdate() {
        getFrontEndBehavior().updateFrontEnd();
    }

    public GUIFrontEndBehavior getFrontEndBehavior() {
        return GUIFrontEndBehavior;
    }

    public SharedMenuCreatorInstance<?, ?> getMenuCreatorInstance() {
        return menuCreatorInstance;
    }

    public static class PlayerGUIData {
        public static void trackCurrentActiveGUI(MCCPlayer player, @Nullable ActiveGUI activeGUI) {
            if (activeGUI != null) {
                player.getTempData().storeData("active_gui", activeGUI);
            } else {
                player.getTempData().removeData("active_gui");
            }
        }

        @Nullable
        public static ActiveGUI getCurrentActiveGUI(MCCPlayer player) {
            if (!player.getTempData().containsData("active_gui"))
                return null;
            return player.getTempData().getData(ActiveGUI.class, "active_gui");
        }
    }

    synchronized Set<Audience> getViewersNoCopy() {
        return viewers;
    }
}
