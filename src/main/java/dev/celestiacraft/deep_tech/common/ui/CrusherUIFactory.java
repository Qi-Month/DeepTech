//package dev.celestiacraft.deep_tech.common.ui;
//
//import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
//import com.lowdragmc.lowdraglib.gui.factory.UIEditorFactory;
//import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//
//import dev.celestiacraft.deep_tech.DeepTech;
//import dev.celestiacraft.deep_tech.common.block_entity.CrusherBlockEntity;
//
//
//public class CrusherUIFactory extends UIFactory<CrusherBlockEntity> {
//
//
//    public static final CrusherUIFactory INSTANCE =
//            new CrusherUIFactory();
//
//
//    public CrusherUIFactory() {
//        super(
//                new ResourceLocation(
//                        "deep_tech",
//                        "projects/ui/machine_crusher.ui"
//                )
//        );
//    }
//
//
//
//    @Override
//    protected ModularUI createUITemplate(
//            CrusherBlockEntity holder,
//            Player player
//    ) {
//
//
//        return holder.createUI(player);
//
//    }
//
//
//
//    @Override
//    protected CrusherBlockEntity readHolderFromSyncData(
//            FriendlyByteBuf buffer
//    ) {
//
//        return null;
//    }
//
//
//
//    @Override
//    protected void writeHolderToSyncData(
//            FriendlyByteBuf buffer,
//            CrusherBlockEntity holder
//    ) {
//
//    }
//
//
//}