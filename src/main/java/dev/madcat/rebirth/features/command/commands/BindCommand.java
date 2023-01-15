
package dev.madcat.rebirth.features.command.commands;

import dev.madcat.rebirth.features.command.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.manager.*;
import com.mojang.realmsclient.gui.*;
import org.lwjgl.input.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.features.modules.*;

public class BindCommand extends Command
{
    public BindCommand() {
        super("bind", new String[] { "<module>", "<bind>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            sendMessage("Please specify a module.");
            return;
        }
        final String rkey = commands[1];
        final String moduleName = commands[0];
        final ModuleManager moduleManager = Rebirth.moduleManager;
        final Module module = ModuleManager.getModuleByName(moduleName);
        if (module == null) {
            sendMessage("Unknown module '" + module + "'!");
            return;
        }
        if (rkey == null) {
            sendMessage(module.getName() + " is bound to " + ChatFormatting.GRAY + module.getBind().toString());
            return;
        }
        int key = Keyboard.getKeyIndex(rkey.toUpperCase());
        if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }
        if (key == 0) {
            sendMessage("Unknown key '" + rkey + "'!");
            return;
        }
        module.bind.setValue(new Bind(key));
        sendMessage("Bind for " + ChatFormatting.GREEN + module.getName() + ChatFormatting.WHITE + " set to " + ChatFormatting.GRAY + rkey.toUpperCase());
    }
}
