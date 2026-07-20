package dev.celestiacraft.deep_tech.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CrusherRecipeSerializer implements RecipeSerializer<CrusherRecipe> {
    @Override
    public CrusherRecipe fromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack output = new ItemStack(
                BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(GsonHelper.getAsString(json, "output_item"))),
                GsonHelper.getAsInt(json, "output_count", 1)
        );
        int energyCost = GsonHelper.getAsInt(json, "energy_cost", 50);
        int processingTime = GsonHelper.getAsInt(json, "processing_time", 100);
        return new CrusherRecipe(id, input, output, energyCost, processingTime);
    }

    @Override
    public CrusherRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int energyCost = buffer.readInt();
        int processingTime = buffer.readInt();

        return new CrusherRecipe(
                id,
                input,
                output,
                energyCost,
                processingTime
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CrusherRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
        buffer.writeInt(recipe.getEnergyCost());
        buffer.writeInt(recipe.getProcessingTime());
    }
}