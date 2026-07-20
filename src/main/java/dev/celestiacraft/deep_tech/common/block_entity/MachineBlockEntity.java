package dev.celestiacraft.deep_tech.common.block_entity;

import dev.celestiacraft.libs.api.register.block.BasicBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

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
			}
			return received;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			int extracted = Math.min(maxExtract, energy);
			if (!simulate && extracted > 0) {
				energy -= extracted;
				setChanged();
			}
			return extracted;
		}

		@Override
		public int getEnergyStored() {
			return energy;
		}

		@Override
		public int getMaxEnergyStored() {
			return maxEnergy;
		}

		@Override
		public boolean canExtract() {
			return true;
		}

		@Override
		public boolean canReceive() {
			return true;
		}
	};

	@Getter
	protected final ItemStackHandler inventory = new ItemStackHandler(2) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		// ✅ 外部插入限制：只能插入到输入槽（slot 0）
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (slot != 0) {
				return stack; // 不允许插入到输出槽（slot 1）和废弃槽（slot 2）
			}
			return super.insertItem(slot, stack, simulate);
		}

		// ✅ 外部提取限制：只能从输出槽（slot 1）提取
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot != 1) {
				return ItemStack.EMPTY; // 不允许从输入槽（slot 0）提取
			}
			return super.extractItem(slot, amount, simulate);
		}
	};

	// ========== LazyOptional（为每个方向单独包装） ==========
	private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energyStorage);

	// ✅ 关键：根据方向返回不同的 IItemHandler 包装，控制外部访问行为
	private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> new IItemHandler() {
		@Override
		public int getSlots() {
			return inventory.getSlots();
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return inventory.getStackInSlot(slot);
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			// 外部只能插入到输入槽（slot 0）
			if (slot != 0) {
				return stack;
			}
			return inventory.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			// 外部只能从输出槽（slot 1）提取
			if (slot != 1) {
				return ItemStack.EMPTY;
			}
			return inventory.extractItem(slot, amount, simulate);
		}

		@Override
		public int getSlotLimit(int slot) {
			return inventory.getSlotLimit(slot);
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return slot == 0 && inventory.isItemValid(slot, stack);
		}
	});


	// ========== Capability ==========
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == ForgeCapabilities.ENERGY) {
			return energyCap.cast();
		}
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			// ✅ 返回受控的物品处理器（外部无法访问内部原始 inventory）
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

	// ========== 持久化 ==========
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Inventory", inventory.serializeNBT());
		tag.putInt("Energy", energy);
		tag.putInt("Progress", progress);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		inventory.deserializeNBT(tag.getCompound("Inventory"));
		energy = tag.getInt("Energy");
		progress = tag.getInt("Progress");
	}

	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}
}