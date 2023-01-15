
package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.util.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import java.awt.*;

public class WorldModify extends Module
{
    public Setting<Boolean> color;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    public Setting<Boolean> timeChanger;
    private Setting<Integer> time;
    public Setting<Boolean> sky;
    public Setting<Boolean> rainbow;
    private Setting<Integer> red2;
    private Setting<Integer> green2;
    private Setting<Integer> blue2;
    int registered;
    
    public WorldModify() {
        super("WorldModify", "Change the world something.", Module.Category.RENDER, true, false, false);
        this.color = (Setting<Boolean>)this.register(new Setting("ColorChanger", false));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255, v -> this.color.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255, v -> this.color.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 255, 0, 255, v -> this.color.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 35, 0, 255, v -> this.color.getValue()));
        this.timeChanger = (Setting<Boolean>)this.register(new Setting("TimeChanger", false));
        this.time = (Setting<Integer>)this.register(new Setting("Time", 0, 0, 24000, v -> this.timeChanger.getValue()));
        this.sky = (Setting<Boolean>)this.register(new Setting("FogColor", false));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", true, v -> this.sky.getValue()));
        this.red2 = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255, v -> this.sky.getValue()));
        this.green2 = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255, v -> this.sky.getValue()));
        this.blue2 = (Setting<Integer>)this.register(new Setting("Blue", 255, 0, 255, v -> this.sky.getValue()));
        this.registered = 0;
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (this.color.getValue()) {
            RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
        }
    }
    
    @SubscribeEvent
    public void init(final WorldEvent event) {
        if (this.timeChanger.getValue()) {
            event.getWorld().setWorldTime((long)this.time.getValue());
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate && this.timeChanger.getValue()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        if (this.sky.getValue()) {
            event.setRed(this.red2.getValue() / 255.0f);
            event.setGreen(this.green2.getValue() / 255.0f);
            event.setBlue(this.blue2.getValue() / 255.0f);
        }
    }
    
    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if (this.sky.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        this.registered = 0;
    }
    
    public void onUpdate() {
        if (this.registered == 0) {
            MinecraftForge.EVENT_BUS.register((Object)this);
            this.registered = 1;
        }
        if (this.rainbow.getValue()) {
            this.doRainbow();
        }
    }
    
    public void doRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.red2.setValue(color_rgb_o >> 16 & 0xFF);
        this.green2.setValue(color_rgb_o >> 8 & 0xFF);
        this.blue2.setValue(color_rgb_o & 0xFF);
    }
}
