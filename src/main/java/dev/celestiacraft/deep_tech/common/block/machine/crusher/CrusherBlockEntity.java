package dev.celestiacraft.deep_tech.common.block.machine.crusher;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import dev.celestiacraft.deep_tech.common.block_entity.MachineBlockEntity;
import dev.celestiacraft.deep_tech.common.inventory.SimpleMachineInventory;
import dev.celestiacraft.deep_tech.common.register.DTBlockEntities;
import dev.celestiacraft.deep_tech.common.register.DTRecipes;
import dev.celestiacraft.deep_tech.common.recipe.CrusherRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CrusherBlockEntity
        extends MachineBlockEntity
        implements IUIHolder.BlockEntityUI {

    public CrusherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        this(DTBlockEntities.CRUSHER.get(), pos, state);
    }


    public static CrusherBlockEntity create(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        return new CrusherBlockEntity(type, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrusherBlockEntity entity) {
        if (level.isClientSide) return;

        // 包装为 Container
        SimpleMachineInventory inventoryWrapper = new SimpleMachineInventory(entity.inventory);

        // 查找配方
        RecipeType<CrusherRecipe> recipeType = DTRecipes.CRUSHER_TYPE.get();
        CrusherRecipe recipe = level.getRecipeManager()
                .getRecipeFor(recipeType, inventoryWrapper, level)
                .orElse(null);

        // 如果没有配方，重置状态
        if (recipe == null) {
            if (state.getValue(CrusherBlock.ACTIVE)) {
                level.setBlock(pos, state.setValue(CrusherBlock.ACTIVE, false), 3);
            }
            if (entity.progress > 0) {
                entity.progress = 0;
                entity.setChanged();
            }
            return;
        }

        // 获取配方数据
        ItemStack output = recipe.getOutput();
        int energyCost = recipe.getEnergyCost();
        int processingTime = recipe.getProcessingTime();

        ItemStack currentOutput = entity.inventory.getStackInSlot(1);
        boolean canOutput = currentOutput.isEmpty() ||
                (ItemStack.isSameItemSameTags(currentOutput, output) &&
                        currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize());

        boolean hasEnergy = entity.energy >= energyCost;

        // 更新激活状态
        boolean isWorking = canOutput && hasEnergy;
        if (state.getValue(CrusherBlock.ACTIVE) != isWorking) {
            level.setBlock(pos, state.setValue(CrusherBlock.ACTIVE, isWorking), 3);
        }

        // 执行加工
        if (isWorking) {
            entity.energy -= energyCost;
            entity.setChanged();
            entity.progress++;
            entity.setChanged();

            if(entity.progress >= recipe.getProcessingTime()) {
                entity.inventory.getStackInSlot(0).shrink(1);

                if (currentOutput.isEmpty()) {
                    entity.inventory.setStackInSlot(1, output.copy());
                } else {
                    currentOutput.grow(output.getCount());
                }

                entity.progress = 0;
                entity.setChanged();
            }
        }
    }
    @Override
    public ModularUI createUI(Player player) {

        return new ModularUI(
                176,
                166,
                this,
                player
        ).widget(createUIWidget(player));

    }


    private WidgetGroup createUIWidget(Player player) {

        WidgetGroup group =
                new WidgetGroup(
                        0,
                        0,
                        176,
                        166
                );


        group.setBackground(
                new ResourceTexture(
                        "deep_tech:textures/gui/crusher.png"
                )
        );


        LabelWidget title =
                new LabelWidget(
                        8,
                        8,
                        Component.translatable(
                                "block.deep_tech.machine_crusher"
                        )
                );

        group.addWidget(title);



        SimpleMachineInventory container =
                new SimpleMachineInventory(inventory);



        SlotWidget input =
                new SlotWidget();


        input.setContainerSlot(
                container,
                0
        );


        input.setSelfPosition(
                new Position(
                        35,
                        60
                )
        );


        input.setBackground(
                SlotWidget.ITEM_SLOT_TEXTURE
        );


        group.addWidget(input);



        SlotWidget output =
                new SlotWidget();


        output.setContainerSlot(
                container,
                1
        );


        output.setSelfPosition(
                new Position(
                        125,
                        60
                )
        );


        output.setBackground(
                SlotWidget.ITEM_SLOT_TEXTURE
        );


        group.addWidget(output);



        addPlayerInventory(
                group,
                player
        );


        return group;
    }

//    private IItemTransfer getItemTransfer() {
//
//        return new IItemTransfer() {
//
//            @Override
//            public int getSlots() {
//                return inventory.getSlots();
//            }
//
//
//            @Override
//            public ItemStack getStackInSlot(int slot) {
//                return inventory.getStackInSlot(slot);
//            }
//
//
//            @Override
//            public ItemStack insertItem(
//                    int slot,
//                    ItemStack stack,
//                    boolean simulate,
//                    boolean notifyChanges
//            ) {
//                return inventory.insertItem(
//                        slot,
//                        stack,
//                        simulate
//                );
//            }
//
//
//            @Override
//            public ItemStack extractItem(
//                    int slot,
//                    int amount,
//                    boolean simulate,
//                    boolean notifyChanges
//            ) {
//                return inventory.extractItem(
//                        slot,
//                        amount,
//                        simulate
//                );
//            }
//
//
//            @Override
//            public int getSlotLimit(int slot) {
//                return inventory.getSlotLimit(slot);
//            }
//
//
//            @Override
//            public boolean isItemValid(
//                    int slot,
//                    ItemStack stack
//            ) {
//                return inventory.isItemValid(
//                        slot,
//                        stack
//                );
//            }
//
//
//            @Override
//            public Object createSnapshot() {
//                return new Object();
//            }
//
//
//            @Override
//            public void restoreFromSnapshot(Object snapshot) {
//
//            }
//        };
//    }
private void addPlayerInventory(
        WidgetGroup group,
        Player player
) {

        Container inventory =
                player.getInventory();


        // 主背包 3×9

        for (int row = 0; row < 3; row++) {

            for (int col = 0; col < 9; col++) {

                SlotWidget slot =
                        new SlotWidget();


                slot.setContainerSlot(
                        inventory,
                        col + row * 9 + 9
                );



                slot.setSelfPosition(
                        new Position(
                                35 + col * 18,
                                90 + row * 18
                        )
                );


                group.addWidget(slot);
            }
        }



        // 快捷栏 9格

        for (int col = 0; col < 9; col++) {

            SlotWidget slot =
                    new SlotWidget();


            slot.setContainerSlot(
                    inventory,
                    col
            );


            slot.setSelfPosition(
                    new Position(
                            35 + col * 18,
                            148
                    )
            );


            group.addWidget(slot);
        }
    }
}