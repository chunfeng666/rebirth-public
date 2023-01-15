

package dev.madcat.rebirth.manager;

import dev.madcat.rebirth.features.*;

public class TimerManager extends Feature
{
    private float timer;
    
    public TimerManager() {
        this.timer = 1.0f;
    }
    
    public void unload() {
        this.timer = 1.0f;
        TimerManager.mc.timer.tickLength = 50.0f;
    }
    
    public void update() {
        TimerManager.mc.timer.tickLength = 50.0f / ((this.timer <= 0.0f) ? 0.1f : this.timer);
    }
    
    public void setTimer(final float timer) {
        if (timer > 0.0f) {
            this.timer = timer;
        }
    }
    
    public float getTimer() {
        return this.timer;
    }
    
    public void reset() {
        this.timer = 1.0f;
    }
}
