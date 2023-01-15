

package dev.madcat.rebirth.util;

import java.awt.*;

public class Rainbow
{
    public static Color getColour() {
        return (Color)Colour.fromHSB(System.currentTimeMillis() % 11520L / 11520.0f, 1.0f, 1.0f);
    }
}
