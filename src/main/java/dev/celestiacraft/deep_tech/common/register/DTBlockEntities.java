package dev.celestiacraft.deep_tech.common.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.celestiacraft.deep_tech.DeepTech;
import dev.celestiacraft.deep_tech.common.block_entity.CrusherBlockEntity;

public class DTBlockEntities {
    public static final BlockEntityEntry<CrusherBlockEntity> CRUSHER =
            DeepTech.REGISTRATE
                    .blockEntity("crusher", CrusherBlockEntity::create)  // ✅ 明确类型
                    .validBlocks(DTBlocks.MACHINE_CRUSHER)
                    .register();

    public static void register() {
        DeepTech.registerLog("Block Entities");
    }
}