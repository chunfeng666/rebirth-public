

package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import dev.madcat.rebirth.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.features.gui.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.*;

public class ClickGui extends Module
{
    private static ClickGui INSTANCE;
    private final Setting<Settings> setting;
    public Setting<String> prefix;
    public Setting<String> clientName;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> hoverAlpha;
    public Setting<Integer> alpha;
    public Setting<Integer> alphaBox;
    public Setting<Boolean> outline;
    public Setting<Boolean> moduleDescription;
    public Setting<Boolean> moduleIcon;
    public Setting<Boolean> moduleIcon2;
    public Setting<Boolean> snowing;
    public Setting<Integer> iconmode;
    public Setting<Integer> moduleWidth;
    public Setting<Integer> moduleDistance;
    public Setting<Boolean> rainbowg;
    public Setting<Boolean> rainbow;
    public Setting<rainbowMode> rainbowModeHud;
    public Setting<rainbowModeArray> rainbowModeA;
    public Setting<Integer> rainbowHue;
    public Setting<Float> rainbowBrightness;
    public Setting<Float> rainbowSaturation;
    public Setting<Boolean> background;
    public Setting<Boolean> blur;
    public Setting<Integer> g_red;
    public Setting<Integer> g_green;
    public Setting<Integer> g_blue;
    public Setting<Integer> g_red1;
    public Setting<Integer> g_green1;
    public Setting<Integer> g_blue1;
    public Setting<Integer> g_alpha;
    public Setting<Integer> g_alpha1;
    
    public ClickGui() {
        super("ClickGui", "Module interface.", Category.CLIENT, true, false, false);
        this.setting = (Setting<Settings>)this.register(new Setting("Settings", Settings.Gui));
        this.prefix = (Setting<String>)this.register(new Setting("Prefix", ".", v -> this.setting.getValue() == Settings.Gui));
        this.clientName = (Setting<String>)this.register(new Setting("ClientName", "Rebirth", v -> this.setting.getValue() == Settings.Gui));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 140, 0, 255, v -> !this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 140, 0, 255, v -> !this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 250, 0, 255, v -> !this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.hoverAlpha = (Setting<Integer>)this.register(new Setting("Alpha", 225, 0, 255, v -> !this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.alpha = (Setting<Integer>)this.register(new Setting("HoverAlpha", 240, 0, 255, v -> !this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.alphaBox = (Setting<Integer>)this.register(new Setting("AlphaBox", 0, 0, 255, v -> this.setting.getValue() == Settings.Color));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gui));
        this.moduleDescription = (Setting<Boolean>)this.register(new Setting("Description", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gui));
        this.moduleIcon = (Setting<Boolean>)this.register(new Setting("Icon", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gui));
        this.moduleIcon2 = (Setting<Boolean>)this.register(new Setting("Icon2", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gui));
        this.snowing = (Setting<Boolean>)this.register(new Setting("Snowing", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gui));
        this.iconmode = (Setting<Integer>)this.register(new Setting("SettingIcon", 0, 0, 5, v -> this.setting.getValue() == Settings.Gui));
        this.moduleWidth = (Setting<Integer>)this.register(new Setting("ModuleWidth", 0, 0, 40, v -> this.setting.getValue() == Settings.Gui));
        this.moduleDistance = (Setting<Integer>)this.register(new Setting("ModuleDistance", 30, 0, 50, v -> this.setting.getValue() == Settings.Gui));
        this.rainbowg = (Setting<Boolean>)this.register(new Setting("Rainbow", Boolean.FALSE, v -> this.setting.getValue() == Settings.Gradient));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", Boolean.FALSE, v -> this.setting.getValue() == Settings.Color));
        this.rainbowModeHud = (Setting<rainbowMode>)this.register(new Setting("HUD", rainbowMode.Static, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.rainbowModeA = (Setting<rainbowModeArray>)this.register(new Setting("ArrayList", rainbowModeArray.Up, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.rainbowHue = (Setting<Integer>)this.register(new Setting("Delay", 600, 0, 600, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.rainbowBrightness = (Setting<Float>)this.register(new Setting("Brightness ", 255.0f, 1.0f, 255.0f, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.rainbowSaturation = (Setting<Float>)this.register(new Setting("Saturation", 255.0f, 1.0f, 255.0f, v -> this.rainbow.getValue() && this.setting.getValue() == Settings.Color));
        this.background = (Setting<Boolean>)this.register(new Setting("BackGround", Boolean.FALSE, v -> this.setting.getValue() == Settings.Gui));
        this.blur = (Setting<Boolean>)this.register(new Setting("Blur", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gui));
        this.g_red = (Setting<Integer>)this.register(new Setting("RedL", 105, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_green = (Setting<Integer>)this.register(new Setting("GreenL", 162, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_blue = (Setting<Integer>)this.register(new Setting("BlueL", 255, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_red1 = (Setting<Integer>)this.register(new Setting("RedR", 143, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_green1 = (Setting<Integer>)this.register(new Setting("GreenR", 140, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_blue1 = (Setting<Integer>)this.register(new Setting("BlueR", 213, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_alpha = (Setting<Integer>)this.register(new Setting("AlphaL", 0, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.g_alpha1 = (Setting<Integer>)this.register(new Setting("AlphaR", 0, 0, 255, v -> this.setting.getValue() == Settings.Gradient));
        this.setInstance();
    }
    
    public static ClickGui getInstance() {
        if (ClickGui.INSTANCE == null) {
            ClickGui.INSTANCE = new ClickGui();
        }
        return ClickGui.INSTANCE;
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (this.background.getValue()) {
            RenderUtil.drawRect(0.0f, 0.0f, 1000.0f, 550.0f, ColorUtil.toRGBA(20, 20, 20, 150));
            RenderUtil.drawGradientRect(0, 200, 1000, 350, ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 0), ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255));
        }
    }
    
    private void setInstance() {
        ClickGui.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                Rebirth.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + Rebirth.commandManager.getPrefix());
            }
            Rebirth.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }
    
    public String getCommandMessage() {
        return TextUtil.coloredString("[", HUD.getInstance().bracketColor.getPlannedValue()) + TextUtil.coloredString(getInstance().clientName.getValueAsString(), HUD.getInstance().commandColor.getPlannedValue()) + TextUtil.coloredString("]", HUD.getInstance().bracketColor.getPlannedValue());
    }
    
    @Override
    public void onEnable() {
        ClickGui.mc.displayGuiScreen((GuiScreen)RebirthGui.getClickGui());
    }
    
    @Override
    public void onLoad() {
        Rebirth.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        Rebirth.commandManager.setPrefix(this.prefix.getValue());
    }
    
    @Override
    public void onUpdate() {
        if (this.blur.getValue()) {
            if (OpenGlHelper.shadersSupported && Util.mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (Util.mc.entityRenderer.getShaderGroup() != null) {
                    Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
                try {
                    Util.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (Util.mc.entityRenderer.getShaderGroup() != null && Util.mc.currentScreen == null) {
                Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
        else if (Util.mc.entityRenderer.getShaderGroup() != null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    
    @Override
    public void onTick() {
        Rebirth.commandManager.setClientMessage(this.getCommandMessage());
        if (!(ClickGui.mc.currentScreen instanceof RebirthGui)) {
            this.disable();
        }
    }
    
    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof RebirthGui) {
            Util.mc.displayGuiScreen((GuiScreen)null);
        }
        if (Util.mc.entityRenderer.getShaderGroup() != null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    
    static {
        ClickGui.INSTANCE = new ClickGui();
    }
    
    public enum rainbowModeArray
    {
        Static, 
        Up;
    }
    
    public enum rainbowMode
    {
        Static, 
        Sideway;
    }
    
    public enum Settings
    {
        Gui, 
        Color, 
        Gradient;
    }
}
