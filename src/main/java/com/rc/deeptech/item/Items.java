package com.rc.deeptech.item;

import com.rc.deeptech.DeepTech;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {
    public static final DeferredRegister <Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, DeepTech.MODID);

    public static final RegistryObject<Item> SCULK_CHUNK =
            ITEMS.register("sculk_chunk", () -> new Item(new Item.Properties()));

    public static void registry(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
