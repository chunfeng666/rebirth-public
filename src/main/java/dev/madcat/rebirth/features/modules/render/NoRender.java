

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;

public class NoRender extends Module
{
    public static NoRender INSTANCE;
    public Setting<Boolean> armor;
    public Setting<Boolean> fire;
    public Setting<Boolean> blind;
    public Setting<Boolean> nausea;
    public Setting<Boolean> fog;
    public Setting<Boolean> noWeather;
    public Setting<Boolean> hurtCam;
    public Setting<Boolean> totemPops;
    public Setting<Boolean> blocks;
    
    public NoRender() {
        super("NoRender", "Prevent some animation.", Module.Category.RENDER, true, false, false);
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", true));
        this.fire = (Setting<Boolean>)this.register(new Setting("Fire", true));
        this.blind = (Setting<Boolean>)this.register(new Setting("Blind", true));
        this.nausea = (Setting<Boolean>)this.register(new Setting("Nausea", true));
        this.fog = (Setting<Boolean>)this.register(new Setting("Fog", true));
        this.noWeather = (Setting<Boolean>)this.register(new Setting("Weather", true, "AntiWeather"));
        this.hurtCam = (Setting<Boolean>)this.register(new Setting("HurtCam", true));
        this.totemPops = (Setting<Boolean>)this.register(new Setting("TotemPop", true, "Removes the Totem overlay."));
        this.blocks = (Setting<Boolean>)this.register(new Setting("Block", true));
        this.setInstance();
    }
    
    public static NoRender getInstance() {
        if (NoRender.INSTANCE != null) {
            return NoRender.INSTANCE;
        }
        return NoRender.INSTANCE = new NoRender();
    }
    
    private void setInstance() {
        NoRender.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (this.blind.getValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (!this.nausea.getValue()) {
            return;
        }
        if (this.noWeather.getValue()) {
            NoRender.mc.world.getWorldInfo().setRaining(false);
        }
        if (!NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            return;
        }
        NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
    }
    
    @SubscribeEvent
    public void NoRenderEventListener(final NoRenderEvent event) {
        if (event.getStage() == 0 && this.armor.getValue()) {
            event.setCanceled(true);
            return;
        }
        if (event.getStage() != 1) {
            return;
        }
        if (!this.hurtCam.getValue()) {
            return;
        }
        event.setCanceled(true);
    }
    
    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if (!this.fog.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void blockOverlayEventListener(final RenderBlockOverlayEvent event) {
        if (!this.fire.getValue()) {
            return;
        }
        if (event.getOverlayType() != RenderBlockOverlayEvent.OverlayType.FIRE) {
            return;
        }
        event.setCanceled(true);
    }
    
    static {
        NoRender.INSTANCE = new NoRender();
    }
}
