
package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.util.*;

public class Timers extends Module
{
    private final Setting<Boolean> tickMode;
    public final Setting<Integer> disableTicks;
    public final Setting<Integer> noPauseTicks;
    public int ticksPassed;
    public int unPauseTicks;
    public boolean pause;
    private final Setting<Float> tickNormal;
    private final Setting<Boolean> bypass;
    public final Setting<Float> speedvalue;
    public int i;
    public int x;
    private final Timer timer;
    
    public Timers() {
        super("Timer", "Change client running speed.", Module.Category.MOVEMENT, true, false, false);
        this.tickMode = (Setting<Boolean>)this.register(new Setting("TickMode", false));
        this.disableTicks = (Setting<Integer>)this.register(new Setting("Disable Ticks", 30, 1, 100, v -> this.tickMode.getValue()));
        this.noPauseTicks = (Setting<Integer>)this.register(new Setting("UnPause Ticks", 30, 1, 100, v -> this.tickMode.getValue()));
        this.ticksPassed = 0;
        this.unPauseTicks = 0;
        this.pause = false;
        this.tickNormal = (Setting<Float>)this.register(new Setting("Speed", 1.2f, 1.0f, 10.0f));
        this.bypass = (Setting<Boolean>)this.register(new Setting("Bypass", true, v -> !this.tickMode.getValue()));
        this.speedvalue = (Setting<Float>)this.register(new Setting("SpeedValue", 0.0f, 0.0f, 100.0f, v -> !this.tickMode.getValue() && this.bypass.getValue()));
        this.i = 0;
        this.x = 0;
        this.timer = new Timer();
    }
    
    public void onDisable() {
        Timers.mc.timer.tickLength = 50.0f;
    }
    
    public void onEnable() {
        Timers.mc.timer.tickLength = 50.0f;
    }
    
    public void onUpdate() {
        if (this.tickMode.getValue()) {
            ++this.ticksPassed;
            if (!this.pause) {
                Timers.mc.timer.tickLength = 50.0f / this.tickNormal.getValue();
            }
            if (this.pause) {
                ++this.unPauseTicks;
                Timers.mc.timer.tickLength = 50.0f;
            }
            if (this.ticksPassed >= this.disableTicks.getValue()) {
                this.ticksPassed = 0;
                if (this.unPauseTicks <= this.noPauseTicks.getValue()) {
                    this.pause = true;
                }
                else if (this.unPauseTicks >= this.noPauseTicks.getValue()) {
                    this.pause = false;
                    this.unPauseTicks = 0;
                }
            }
        }
        else if (this.bypass.getValue()) {
            if (this.i <= this.speedvalue.getValue()) {
                ++this.i;
                Timers.mc.timer.tickLength = 50.0f / this.tickNormal.getValue();
                this.x = 0;
            }
            else if (this.x <= this.speedvalue.getValue() - this.speedvalue.getValue() / 2.0f / 2.0f) {
                ++this.x;
                Timers.mc.timer.tickLength = 50.0f;
            }
            else {
                this.i = 0;
            }
        }
        else {
            Timers.mc.timer.tickLength = 50.0f / this.tickNormal.getValue();
        }
    }
    
    public void onLogin() {
        this.timer.reset();
    }
}
