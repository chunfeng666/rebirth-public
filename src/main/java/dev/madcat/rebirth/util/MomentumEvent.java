

package dev.madcat.rebirth.util;

import net.minecraftforge.fml.common.eventhandler.*;

@Cancelable
public class MomentumEvent extends Event
{
    Stage stage;
    
    public MomentumEvent() {
    }
    
    public MomentumEvent(final Stage stage) {
        this.stage = stage;
    }
    
    public Stage getStage() {
        return this.stage;
    }
    
    public void setStage(final Stage stage) {
        this.stage = stage;
        this.setCanceled(false);
    }
    
    public enum Stage
    {
        PRE, 
        POST;
    }
}
