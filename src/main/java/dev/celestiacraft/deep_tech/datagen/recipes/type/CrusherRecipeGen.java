package dev.celestiacraft.deep_tech.datagen.recipes.type;

import dev.celestiacraft.deep_tech.api.server.recipe.builder.crusher.CrusherRecipeBuilder;
import dev.celestiacraft.deep_tech.datagen.recipes.DTRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CrusherRecipeGen extends DTRecipeProvider {
	public CrusherRecipeGen(PackOutput output) {
		super(output);
	}

	public static void register(Consumer<FinishedRecipe> consumer) {
		CrusherRecipeBuilder.builder()
				.input(Tags.Items.COBBLESTONE)
				.output(Blocks.GRAVEL)
				.energyCost(200)
				.processingTime(150)
				.save(consumer, save("crusher/gravel"));
	}
}