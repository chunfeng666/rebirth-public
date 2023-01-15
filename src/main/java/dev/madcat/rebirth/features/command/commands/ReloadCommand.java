

package dev.madcat.rebirth.features.command.commands;

import dev.madcat.rebirth.features.command.*;
import dev.madcat.rebirth.*;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    public void execute(final String[] commands) {
        Rebirth.reload();
    }
}
