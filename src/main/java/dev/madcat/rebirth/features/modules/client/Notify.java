
package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import dev.madcat.rebirth.util.*;

public class Notify extends Module
{
    boolean notify;
    private final Setting<Integer> wy;
    private final Setting<Integer> tick;
    private final Setting<Integer> alpha;
    int delay;
    int delay2;
    
    public Notify() {
        super("Notify", "Notify.", Category.CLIENT, true, false, false);
        this.wy = (Setting<Integer>)this.register(new Setting("Y", 18, 25, 500));
        this.tick = (Setting<Integer>)this.register(new Setting("Tick", 60, 0, 120));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 160, 0, 255));
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Module.test == null) {
            return;
        }
        final String string = Module.test;
        final int y = this.renderer.scaledHeight - this.wy.getValue();
        final int x = this.renderer.scaledWidth - 10 - this.renderer.getStringWidth(string);
        if (Module.test2) {
            this.notify = true;
            this.delay = 0;
            Module.test2 = false;
            this.delay2 = this.tick.getValue();
        }
        if (this.notify) {
            if (this.alpha.getValue() >= 0) {
                RenderUtil.drawRectangleCorrectly(x, y, 10 + this.renderer.getStringWidth(string), 15, ColorUtil.toRGBA(20, 20, 20, this.alpha.getValue()));
            }
            final int color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            this.renderer.drawString(string, (float)(5 + x), (float)(4 + y), color, true);
            RenderUtil.drawRectangleCorrectly(x, y + 14, (10 + this.renderer.getStringWidth(string)) * this.delay2 / this.tick.getValue(), 1, color);
        }
    }
    
    @Override
    public void onTick() {
        if (this.delay >= this.tick.getValue() - 1) {
            this.notify = false;
            this.delay = 0;
        }
        if (this.notify) {
            ++this.delay;
            --this.delay2;
        }
    }
}
