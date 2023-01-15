
package dev.madcat.rebirth.features.command.commands;

import dev.madcat.rebirth.features.command.*;
import dev.madcat.rebirth.*;
import com.mojang.realmsclient.gui.*;
import java.util.*;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help");
    }
    
    public void execute(final String[] commands) {
        sendMessage("Commands: ");
        for (final Command command : Rebirth.commandManager.getCommands()) {
            sendMessage(ChatFormatting.GRAY + Rebirth.commandManager.getPrefix() + command.getName());
        }
    }
}
