

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.command.*;

public class NickHider extends Module
{
    public final Setting<String> NameString;
    private static NickHider instance;
    
    public NickHider() {
        super("NameChanger", "Changes name", Category.MISC, false, false, false);
        this.NameString = (Setting<String>)this.register(new Setting("Name", "New Name Here..."));
        NickHider.instance = this;
    }
    
    @Override
    public void onEnable() {
        Command.sendMessage(ChatFormatting.GRAY + "Success! Name succesfully changed to " + ChatFormatting.GREEN + this.NameString.getValue());
    }
    
    public static NickHider getInstance() {
        if (NickHider.instance == null) {
            NickHider.instance = new NickHider();
        }
        return NickHider.instance;
    }
}
