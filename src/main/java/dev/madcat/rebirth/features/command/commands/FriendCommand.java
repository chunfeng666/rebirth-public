
package dev.madcat.rebirth.features.command.commands;

import dev.madcat.rebirth.features.command.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.manager.*;
import com.mojang.realmsclient.gui.*;
import java.util.*;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new String[] { "<add/del/name/clear>", "<name>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            if (Rebirth.friendManager.getFriends().isEmpty()) {
                sendMessage("Friend list empty D:.");
            }
            else {
                String f = "Friends: ";
                for (final FriendManager.Friend friend : Rebirth.friendManager.getFriends()) {
                    try {
                        f = f + friend.getUsername() + ", ";
                    }
                    catch (Exception ex) {}
                }
                sendMessage(f);
            }
            return;
        }
        if (commands.length != 2) {
            if (commands.length >= 2) {
                final String s = commands[0];
                switch (s) {
                    case "add": {
                        Rebirth.friendManager.addFriend(commands[1]);
                        sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                    }
                    case "del": {
                        Rebirth.friendManager.removeFriend(commands[1]);
                        sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                    }
                    default: {
                        sendMessage("Unknown Command, try friend add/del (name)");
                        break;
                    }
                }
            }
            return;
        }
        final String s2 = commands[0];
        switch (s2) {
            case "reset": {
                Rebirth.friendManager.onLoad();
                sendMessage("Friends got reset.");
            }
            default: {
                sendMessage(commands[0] + (Rebirth.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            }
        }
    }
}
