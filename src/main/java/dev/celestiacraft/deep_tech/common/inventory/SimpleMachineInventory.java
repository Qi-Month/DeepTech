package dev.celestiacraft.deep_tech.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * 将 ItemStackHandler 包装为 Container，以便用于 RecipeManager
 */
public class SimpleMachineInventory implements Container {
    private final ItemStackHandler handler;

    public SimpleMachineInventory(ItemStackHandler handler) {
        this.handler = handler;
    }
    public Slot getSlot(int index) {
        return new Slot(
                this,
                index,
                0,
                0
        );
    }

    @Override
    public int getContainerSize() {
        return handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getItem(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int slot, int amount) {
        return handler.extractItem(slot, amount, false);
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = handler.getStackInSlot(slot);
        handler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack stack) {
        handler.setStackInSlot(slot, stack);
    }

    @Override
    public void setChanged() {
        // 不需要操作，因为 ItemStackHandler 有独立的 change 通知
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < handler.getSlots(); i++) {
            handler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}