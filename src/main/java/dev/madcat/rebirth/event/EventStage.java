
package dev.madcat.rebirth.event;

import net.minecraftforge.fml.common.eventhandler.*;

public class EventStage extends Event
{
    private int stage;
    private boolean cancelled;
    
    public EventStage() {
        this.cancelled = true;
    }
    
    public EventStage(final int stage) {
        this.cancelled = true;
        this.stage = stage;
    }
    
    public int getStage() {
        return this.stage;
    }
    
    public void setStage(final int stage) {
        this.stage = stage;
    }
    
    public final boolean isCancelled() {
        return this.cancelled;
    }
}
