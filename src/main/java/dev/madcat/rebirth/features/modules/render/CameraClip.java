

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;

public class CameraClip extends Module
{
    public Setting<Double> distance;
    
    public CameraClip() {
        super("CameraClip", "CameraClip", Module.Category.RENDER, false, false, false);
        this.distance = (Setting<Double>)this.register(new Setting("Distance", 4.0, (-10.0), 20.0));
    }
}
