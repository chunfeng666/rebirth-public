

package dev.madcat.rebirth.features.command.commands;

import dev.madcat.rebirth.features.command.*;
import dev.madcat.rebirth.*;

public class UnloadCommand extends Command
{
    public UnloadCommand() {
        super("unload", new String[0]);
    }
    
    public void execute(final String[] commands) {
        Rebirth.unload(true);
    }
}
