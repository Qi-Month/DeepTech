package dev.celestiacraft.deep_tech.common.register;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.celestiacraft.deep_tech.DeepTech;
import dev.celestiacraft.deep_tech.api.client.ItemModelGen;
import dev.celestiacraft.deep_tech.common.block.MachineCrusher;
import dev.celestiacraft.libs.api.register.block.BasicBlock;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class DTBlocks {
	public static BlockEntry<BasicBlock> MACHINE_FRAME;

	static {
		MACHINE_FRAME = DeepTech.REGISTRATE.block("machine_frame", BasicBlock::new)
				.item()
				.model(ItemModelGen.withModel("block/machine_frame"))
				.build()
				.blockstate((context, provider) -> {
					var models = provider.models();
					var model = models.cube(
							"machine_frame",
							provider.modLoc("block/machine_frame_top"),
							provider.modLoc("block/machine_frame_top"),
							provider.modLoc("block/machine_frame_side"),
							provider.modLoc("block/machine_frame_side"),
							provider.modLoc("block/machine_frame_side"),
							provider.modLoc("block/machine_frame_side")
					);
					provider.simpleBlock(context.get(), model);
				})
				.register();
	}

	public static final BlockEntry<MachineCrusher> MACHINE_CRUSHER;
	static {
		MACHINE_CRUSHER = DeepTech.REGISTRATE.block("machine_crusher", MachineCrusher::new)
				.item()
				.model(ItemModelGen.withModel("block/machine/machine_crusher_north"))
				.build()
				.blockstate((context, provider) -> {
					var models = provider.models();

					// 为每个方向创建模型
					var modelNorth = models.cube(
							"machine_crusher_north",
							provider.modLoc("block/machine/crusher/top_off"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/face_off"),      // 正面（北）
							provider.modLoc("block/machine/crusher/side_off"),      // 右（东）
							provider.modLoc("block/machine/crusher/side_off"),      // 左（西）
							provider.modLoc("block/machine/crusher/side_off")       // 背面（南）
					);
					var modelEast = models.cube(
							"machine_crusher_east",
							provider.modLoc("block/machine/crusher/top_off"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/side_off"),      // 正面（东）
							provider.modLoc("block/machine/crusher/side_off"),      // 右（南）
							provider.modLoc("block/machine/crusher/face_off"),      // 左（北）
							provider.modLoc("block/machine/crusher/side_off")       // 背面（西）
					);
					var modelSouth = models.cube(
							"machine_crusher_south",
							provider.modLoc("block/machine/crusher/top_off"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/side_off"),      // 正面（南）
							provider.modLoc("block/machine/crusher/side_off"),      // 右（西）
							provider.modLoc("block/machine/crusher/side_off"),      // 左（东）
							provider.modLoc("block/machine/crusher/face_off")       // 背面（北）
					);
					var modelWest = models.cube(
							"machine_crusher_west",
							provider.modLoc("block/machine/crusher/top_off"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/side_off"),      // 正面（西）
							provider.modLoc("block/machine/crusher/face_off"),      // 右（北）
							provider.modLoc("block/machine/crusher/side_off"),      // 左（南）
							provider.modLoc("block/machine/crusher/side_off")       // 背面（东）
					);

					// 为激活状态创建模型（使用不同纹理）
					var modelNorthLit = models.cube(
							"machine_crusher_north_lit",
							provider.modLoc("block/machine/crusher/top_on"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/face_on"),  // 激活正面
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/side_on")
					);
					var modelEastLit = models.cube(
							"machine_crusher_east_lit",
							provider.modLoc("block/machine/crusher/top_on"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/face_on"),
							provider.modLoc("block/machine/crusher/side_on")
					);
					var modelSouthLit = models.cube(
							"machine_crusher_south_lit",
							provider.modLoc("block/machine/crusher/top_on"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/face_on")
					);
					var modelWestLit = models.cube(
							"machine_crusher_west_lit",
							provider.modLoc("block/machine/crusher/top_on"),
							provider.modLoc("block/machine/crusher/bottom"),
							provider.modLoc("block/machine/crusher/face_on"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/side_on"),
							provider.modLoc("block/machine/crusher/side_on")
					);
					provider.getVariantBuilder(context.get())
							.forAllStates(state -> {
								Direction facing = state.getValue(MachineCrusher.FACING);
								boolean lit = state.getValue(MachineCrusher.LIT);

								ModelFile model;
								switch (facing) {
									case NORTH -> model = lit ? modelNorthLit : modelNorth;
									case EAST -> model = lit ? modelEastLit : modelEast;
									case SOUTH -> model = lit ? modelSouthLit : modelSouth;
									case WEST -> model = lit ? modelWestLit : modelWest;
									default -> model = modelNorth; // fallback
								}

								return ConfiguredModel.builder()
										.modelFile(model)
										.rotationY((int) facing.toYRot()) // 旋转模型以匹配方向
										.build();
							});
				})
				.register();
	}

	public static void register() {
		DeepTech.registerLog("Blocks");
	}
}




