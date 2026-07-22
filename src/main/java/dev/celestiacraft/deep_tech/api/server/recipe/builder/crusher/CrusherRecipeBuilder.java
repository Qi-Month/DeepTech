package dev.celestiacraft.deep_tech.api.server.recipe.builder.crusher;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CrusherRecipeBuilder implements RecipeBuilder {
	private Ingredient input;
	private ItemStack output = ItemStack.EMPTY;

	private int energyCost = 50;
	private int processingTime = 100;

	private final Advancement.Builder advancement = Advancement.Builder.advancement();

	private CrusherRecipeBuilder() {
	}

	public static CrusherRecipeBuilder builder() {
		return new CrusherRecipeBuilder();
	}

	public CrusherRecipeBuilder input(ItemLike item) {
		input = Ingredient.of(item);
		return this;
	}

	public CrusherRecipeBuilder input(TagKey<Item> tag) {
		input = Ingredient.of(tag);
		return this;
	}

	public CrusherRecipeBuilder input(Ingredient ingredient) {
		input = ingredient;
		return this;
	}

	public CrusherRecipeBuilder output(ItemLike item) {
		output = new ItemStack(item);
		return this;
	}

	public CrusherRecipeBuilder output(ItemLike item, int count) {
		output = new ItemStack(item, count);
		return this;
	}

	public CrusherRecipeBuilder output(ItemStack stack) {
		output = stack.copy();
		return this;
	}

	public CrusherRecipeBuilder energyCost(int energyCost) {
		this.energyCost = energyCost;
		return this;
	}

	public CrusherRecipeBuilder processingTime(int processingTime) {
		this.processingTime = processingTime;
		return this;
	}

	@Override
	public @NotNull CrusherRecipeBuilder unlockedBy(@NotNull String name, @NotNull CriterionTriggerInstance instance) {
		advancement.addCriterion(name, instance);
		return this;
	}

	@Override
	public @NotNull CrusherRecipeBuilder group(@Nullable String group) {
		return this;
	}

	@Override
	public @NotNull Item getResult() {
		return output.getItem();
	}

	@Override
	public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
		if (input == null) {
			throw new IllegalStateException("Missing input for recipe " + id);
		}

		if (output.isEmpty()) {
			throw new IllegalStateException("Missing output for recipe " + id);
		}

		advancement.parent(ResourceLocation.tryParse("recipes/root"))
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(RequirementsStrategy.OR);

		consumer.accept(new CrusherRecipeResult(
				id,
				input,
				output,
				energyCost,
				processingTime,
				advancement,
				id.withPrefix("recipes/")
		));
	}
}