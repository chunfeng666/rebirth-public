

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;

public class Chams extends Module
{
    private static Chams INSTANCE;
    
    public Chams() {
        super("Chams", "Player behind rendered wall.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }
    
    public static Chams getInstance() {
        if (Chams.INSTANCE == null) {
            Chams.INSTANCE = new Chams();
        }
        return Chams.INSTANCE;
    }
    
    private void setInstance() {
        Chams.INSTANCE = this;
    }
    
    static {
        Chams.INSTANCE = new Chams();
    }
}
