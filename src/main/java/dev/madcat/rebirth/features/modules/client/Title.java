
package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;

public class Title extends Module
{
    private static Title INSTANCE;
    public Setting<String> titleName;
    
    public Title() {
        super("ClientTitle", "Change client title.", Category.CLIENT, true, false, false);
        this.titleName = (Setting<String>)this.register(new Setting("TitleName", "Rebirth story", "client title."));
        this.setInstance();
    }
    
    public static Title getInstance() {
        if (Title.INSTANCE == null) {
            Title.INSTANCE = new Title();
        }
        return Title.INSTANCE;
    }
    
    private void setInstance() {
        Title.INSTANCE = this;
    }
    
    static {
        Title.INSTANCE = new Title();
    }
}
