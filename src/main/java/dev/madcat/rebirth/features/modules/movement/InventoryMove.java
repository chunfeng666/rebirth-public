

package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;

public class InventoryMove extends Module
{
    private static InventoryMove INSTANCE;
    public Setting<Boolean> shift;
    
    public InventoryMove() {
        super("InvMove", "Allow walking on the interface.", Module.Category.MOVEMENT, true, false, false);
        this.shift = (Setting<Boolean>)this.register(new Setting("Sneak", false));
        this.setInstance();
    }
    
    public static InventoryMove getInstance() {
        if (InventoryMove.INSTANCE == null) {
            InventoryMove.INSTANCE = new InventoryMove();
        }
        return InventoryMove.INSTANCE;
    }
    
    private void setInstance() {
        InventoryMove.INSTANCE = this;
    }
    
    static {
        InventoryMove.INSTANCE = new InventoryMove();
    }
}
