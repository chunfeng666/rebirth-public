
package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class ViewModel extends Module
{
    private static ViewModel INSTANCE;
    public Setting<Settings> settings;
    public Setting<Boolean> noEatAnimation;
    public Setting<Double> eatX;
    public Setting<Double> eatY;
    public Setting<Boolean> doBob;
    public Setting<Double> mainX;
    public Setting<Double> mainY;
    public Setting<Double> mainZ;
    public Setting<Double> offX;
    public Setting<Double> offY;
    public Setting<Double> offZ;
    
    public ViewModel() {
        super("ViewModel", "Change the position of the arm", Module.Category.RENDER, true, false, false);
        this.settings = (Setting<Settings>)this.register(new Setting("Settings", Settings.TRANSLATE));
        this.noEatAnimation = (Setting<Boolean>)this.register(new Setting("NoEatAnimation", false, v -> this.settings.getValue() == Settings.TWEAKS));
        this.eatX = (Setting<Double>)this.register(new Setting("EatX", 3.5, (-5.0), 15.0, v -> this.settings.getValue() == Settings.TWEAKS && !this.noEatAnimation.getValue()));
        this.eatY = (Setting<Double>)this.register(new Setting("EatY", 2.1, (-5.0), 15.0, v -> this.settings.getValue() == Settings.TWEAKS && !this.noEatAnimation.getValue()));
        this.doBob = (Setting<Boolean>)this.register(new Setting("ItemBob", true, v -> this.settings.getValue() == Settings.TWEAKS));
        this.mainX = (Setting<Double>)this.register(new Setting("MainX", 1.2, (-2.0), 4.0, v -> this.settings.getValue() == Settings.TRANSLATE));
        this.mainY = (Setting<Double>)this.register(new Setting("MainY", (-0.95), (-3.0), 3.0, v -> this.settings.getValue() == Settings.TRANSLATE));
        this.mainZ = (Setting<Double>)this.register(new Setting("MainZ", (-1.45), (-5.0), 5.0, v -> this.settings.getValue() == Settings.TRANSLATE));
        this.offX = (Setting<Double>)this.register(new Setting("OffX", 1.2, (-2.0), 4.0, v -> this.settings.getValue() == Settings.TRANSLATE));
        this.offY = (Setting<Double>)this.register(new Setting("OffY", (-0.95), (-3.0), 3.0, v -> this.settings.getValue() == Settings.TRANSLATE));
        this.offZ = (Setting<Double>)this.register(new Setting("OffZ", (-1.45), (-5.0), 5.0, v -> this.settings.getValue() == Settings.TRANSLATE));
        this.setInstance();
    }
    
    public static ViewModel getInstance() {
        if (ViewModel.INSTANCE == null) {
            ViewModel.INSTANCE = new ViewModel();
        }
        return ViewModel.INSTANCE;
    }
    
    private void setInstance() {
        ViewModel.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onItemRender(final RenderItemEvent event) {
        event.setMainX((double)this.mainX.getValue());
        event.setMainY((double)this.mainY.getValue());
        event.setMainZ((double)this.mainZ.getValue());
        event.setOffX(-this.offX.getValue());
        event.setOffY((double)this.offY.getValue());
        event.setOffZ((double)this.offZ.getValue());
    }
    
    static {
        ViewModel.INSTANCE = new ViewModel();
    }
    
    private enum Settings
    {
        TRANSLATE, 
        TWEAKS;
    }
}
