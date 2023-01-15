
package dev.madcat.rebirth.features.command.commands;

import dev.madcat.rebirth.features.command.*;
import net.minecraft.client.audio.*;
import net.minecraftforge.fml.common.*;
import com.mojang.realmsclient.gui.*;

public class ReloadSoundCommand extends Command
{
    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }
    
    public void execute(final String[] commands) {
        try {
            final SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue((Class)SoundHandler.class, (Object)ReloadSoundCommand.mc.getSoundHandler(), new String[] { "sndManager", "sndManager" });
            sndManager.reloadSoundSystem();
            Command.sendMessage(ChatFormatting.GREEN + "Reloaded Sound System.");
        }
        catch (Exception e) {
            System.out.println(ChatFormatting.RED + "Could not restart sound manager: " + e.toString());
            e.printStackTrace();
            Command.sendMessage(ChatFormatting.RED + "Couldnt Reload Sound System!");
        }
    }
}
