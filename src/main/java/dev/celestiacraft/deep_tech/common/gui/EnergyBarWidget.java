package dev.celestiacraft.deep_tech.common.gui;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;


public class EnergyBarWidget extends Widget {


    private final Supplier<Integer> energyGetter;

    private final int maxEnergy;


    private final ResourceTexture background =
            new ResourceTexture(
                    "deep_tech:textures/gui/elements/energy_back.png"
            );


    private final ResourceTexture foreground =
            new ResourceTexture(
                    "deep_tech:textures/gui/elements/energy_front.png"
            );


    public EnergyBarWidget(
            int x,
            int y,
            Supplier<Integer> energyGetter,
            int maxEnergy
    ) {

        super(
                x,
                y,
                14,
                42
        );


        this.energyGetter = energyGetter;
        this.maxEnergy = maxEnergy;

    }


    @Override
    public void drawInBackground(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTicks
    ) {

        int energy = energyGetter.get();

        background.draw(
                graphics,
                mouseX,
                mouseY,
                getPosition().x,
                getPosition().y,
                14,
                42
        );


        int height =
                (int)(42F * energy / maxEnergy);


        if(height <= 0)
            return;


        graphics.enableScissor(
                getPosition().x,
                getPosition().y + 42 - height,
                getPosition().x + 14,
                getPosition().y + 42
        );


        foreground.draw(
                graphics,
                mouseX,
                mouseY,
                getPosition().x,
                getPosition().y,
                14,
                42
        );


        graphics.disableScissor();
    }



    @Override
    public void drawInForeground(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTicks
    ){

        super.drawInForeground(
                graphics,
                mouseX,
                mouseY,
                partialTicks
        );


        if(isMouseOverElement(mouseX, mouseY)){

            setHoverTooltips(
                    Component.literal(
                            energyGetter.get()
                                    +
                                    "FE / "
                                    +
                                    maxEnergy
                                    +
                                    "FE"
                    )
            );

        }

    }
}