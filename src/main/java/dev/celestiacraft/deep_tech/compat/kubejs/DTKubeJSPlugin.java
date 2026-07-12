package dev.celestiacraft.deep_tech.compat.kubejs;

import dev.celestiacraft.deep_tech.DeepTech;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class DTKubeJSPlugin extends KubeJSPlugin {
	@Override
	public void registerBindings(BindingsEvent event) {
		event.add("DeepTech", DeepTech.class);
	}
}