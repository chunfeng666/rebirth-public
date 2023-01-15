
package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;

public class NoEntityTrace extends Module
{
    private static NoEntityTrace INSTANCE;
    public boolean noTrace;
    
    public NoEntityTrace() {
        super("NoEntityTrace", "Mine through entities", Category.MISC, false, false, false);
        this.setInstance();
    }
    
    public static NoEntityTrace getINSTANCE() {
        if (NoEntityTrace.INSTANCE == null) {
            NoEntityTrace.INSTANCE = new NoEntityTrace();
        }
        return NoEntityTrace.INSTANCE;
    }
    
    private void setInstance() {
        NoEntityTrace.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        this.noTrace = true;
    }
    
    static {
        NoEntityTrace.INSTANCE = new NoEntityTrace();
    }
}
