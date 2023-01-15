

package dev.madcat.rebirth.features.command.commands;

import dev.madcat.rebirth.features.command.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.*;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", new String[] { "<char>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.GREEN + "Current prefix is " + Rebirth.commandManager.getPrefix());
            return;
        }
        Rebirth.commandManager.setPrefix(commands[0]);
        Command.sendMessage("Prefix changed to " + ChatFormatting.GRAY + commands[0]);
    }
}
