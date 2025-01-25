package de.verdox.mccreativelab.container;

import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.network.chat.Component;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomContainerMenu extends AbstractContainerMenu {

    public static org.bukkit.inventory.Inventory openToPlayer(@NotNull CustomInventory customInventory, @NotNull org.bukkit.entity.Player player, @NotNull net.kyori.adventure.text.Component title) {
        Objects.requireNonNull(customInventory, "customInventory cannot be null");
        Objects.requireNonNull(player, "player cannot be null");
        Objects.requireNonNull(title, "title cannot be null");

        Container container = new SimpleContainer(9 * 6);

        ((CraftPlayer) player).getHandle().openMenu(new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return PaperAdventure.asVanilla(title);
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory, @NotNull Player player) {
                return new CustomContainerMenu(customInventory, syncId, playerInventory, container);
            }
        });
        return new CraftInventory(container);
    }


    private CraftInventoryView bukkitEntity = null;

    public CraftInventoryView getBukkitView() {
        if (this.bukkitEntity != null) {
            return this.bukkitEntity;
        }


        CraftInventory inventory;
        if (this.container instanceof Inventory) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer((Inventory) this.container);
        } else if (this.container instanceof CompoundContainer) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((CompoundContainer) this.container);
        } else {
            inventory = new CraftInventory(this.container);
        }

        this.bukkitEntity = new CraftInventoryView(this.playerInventory.player.getBukkitEntity(), inventory, this);
        return this.bukkitEntity;
    }


    private final CustomInventory customInventory;
    private final Inventory playerInventory;
    private final Container container;

    protected CustomContainerMenu(CustomInventory customInventory, int syncId, Inventory playerInventory, Container inventory) {
        super(MenuType.GENERIC_9x6, syncId);
        this.customInventory = customInventory;
        this.playerInventory = playerInventory;
        this.container = inventory;
        inventory.startOpen(playerInventory.player);

        CustomInventory.Slot[] slots = customInventory.getSlots();

        for (int rowCount = 0; rowCount < 6; ++rowCount) {
            for (int slotCount = 0; slotCount < 9; ++slotCount) {
                int index = slotCount + rowCount * 9;
                Slot slot;
                if (index < slots.length)
                    slot = new WrappedSlot(inventory, index, slots[index]);
                else
                    slot = new Slot(inventory, index, 8 + slotCount * 18, 18 + rowCount * 18);
                this.addSlot(slot);
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + l * 9 + 9, 0, 0));
            }
        }

        for (int index = 0; index < 9; ++index) {
            this.addSlot(new Slot(playerInventory, index, 0, 0));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return CraftItemStack.asNMSCopy(customInventory.quickMoveStack((org.bukkit.entity.Player) player.getBukkitEntity(), slot));
    }

    @Override
    public boolean stillValid(Player player) {
        if (!this.checkReachable) return true;
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public Container getContainer() {
        return container;
    }

    public static class WrappedSlot extends Slot {
        private final CustomInventory.Slot apiSlot;

        public WrappedSlot(Container inventory, int index, CustomInventory.Slot apiSlot) {
            // Indices are only used client side
            super(inventory, index, 0, 0);
            this.apiSlot = apiSlot;
        }

        @Override
        protected void onQuickCraft(ItemStack stack, int amount) {
            apiSlot.onQuickCraft(stack.asBukkitMirror(), amount);
        }

        @Override
        protected void onSwapCraft(int amount) {
            apiSlot.onSwapCraft(amount);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            apiSlot.onTake((org.bukkit.entity.Player) player.getBukkitEntity(), stack.asBukkitMirror());
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return apiSlot.mayPlace(stack.getBukkitStack());
        }

        @Override
        public int getMaxStackSize() {
            return apiSlot.getMaxStackSize();
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return apiSlot.getMaxStackSize(stack.asBukkitMirror());
        }

        @Override
        public boolean isActive() {
            return apiSlot.isActive();
        }

        @Override
        public boolean mayPickup(Player player) {
            return apiSlot.mayPickUp((org.bukkit.entity.Player) player.getBukkitEntity());
        }
    }
}
