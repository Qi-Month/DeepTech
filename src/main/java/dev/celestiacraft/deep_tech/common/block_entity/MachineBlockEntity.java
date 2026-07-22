package dev.celestiacraft.deep_tech.common.block_entity;

import dev.celestiacraft.libs.api.register.block.BasicBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

@Getter
public abstract class MachineBlockEntity extends BasicBlockEntity {
	public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	protected int progress = 0;
	protected final int maxProgress = 100;
	protected int energy = 0;
	protected final int maxEnergy = 10000;
	protected final int maxReceive = 100;
	protected final int maxExtract = 100;

	protected final IEnergyStorage energyStorage = new IEnergyStorage() {
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			int received = Math.min(maxReceive, maxEnergy - energy);
			if (!simulate && received > 0) {
				energy += received;
				setChanged();
				if (level != null && !level.isClientSide) {
					sync();
				}
			}
			return received;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			int extracted = Math.min(maxExtract, energy);
			if (!simulate && extracted > 0) {
				energy -= extracted;
				setChanged();
				if (level != null && !level.isClientSide) {
					sync();
				}
			}
			return extracted;
		}

		@Override
		public int getEnergyStored() { return energy; }
		@Override
		public int getMaxEnergyStored() { return maxEnergy; }
		@Override
		public boolean canExtract() { return true; }
		@Override
		public boolean canReceive() { return true; }
	};

	@Getter
	protected final ItemStackHandler inventory = new ItemStackHandler(2) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energyStorage);
	private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> new IItemHandler() {
		@Override
		public int getSlots() { return inventory.getSlots(); }
		@Override
		public ItemStack getStackInSlot(int slot) { return inventory.getStackInSlot(slot); }
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (slot != 0) return stack;
			return inventory.insertItem(slot, stack, simulate);
		}
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot != 1) return ItemStack.EMPTY;
			return inventory.extractItem(slot, amount, simulate);
		}
		@Override
		public int getSlotLimit(int slot) { return inventory.getSlotLimit(slot); }
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return slot == 0 && inventory.isItemValid(slot, stack);
		}
	});

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == ForgeCapabilities.ENERGY) {
			return energyCap.cast();
		}
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			return itemCap.cast();
		}
		return super.getCapability(capability, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		energyCap.invalidate();
		itemCap.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Inventory", inventory.serializeNBT());
		tag.putInt("Energy", energy);
		tag.putInt("Progress", progress);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		tag.putInt("Energy", energy);
		tag.putInt("Progress", progress);
		return tag;
	}
	// ========== 网络同步（发送 NBT 数据包） ==========
	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag tag = pkt.getTag();
		if (tag != null) {
			handleUpdateTag(tag);
		}
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		energy = tag.getInt("Energy");
		progress = tag.getInt("Progress");
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		inventory.deserializeNBT(tag.getCompound("Inventory"));
		energy = tag.getInt("Energy");
		progress = tag.getInt("Progress");
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (level != null && !level.isClientSide) {
			sync();
		}
	}

	public int getEnergyStored() {
		return energy;
	}

	public int getMaxEnergyStored() {
		return maxEnergy;
	}

	// ✅ 同步方法：发送完整数据到客户端
	public void sync() {
		if (level != null && !level.isClientSide) {
			setChanged();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}
}