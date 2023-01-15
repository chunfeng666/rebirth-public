

package dev.madcat.rebirth.manager;

import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.features.gui.font.*;
import java.awt.*;

public class MenuFont implements Globals
{
    private final CustomFont menuFont;
    
    public MenuFont() {
        this.menuFont = new CustomFont(new Font("Tahoma", 1, 21), true, false);
    }
    
    public void drawStringWithShadow(final String string, final float x, final float y, final int colour) {
        this.drawString(string, x, y, colour, true);
    }
    
    public float drawString(final String string, final float x, final float y, final int colour, final boolean shadow) {
        if (shadow) {
            return this.menuFont.drawStringWithShadow(string, (double)x, (double)y, colour);
        }
        return this.menuFont.drawString(string, x, y, colour);
    }
}
