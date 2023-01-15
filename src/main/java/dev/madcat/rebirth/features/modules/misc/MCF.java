

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.command.*;
import net.minecraft.entity.*;

public class MCF extends Module
{
    private boolean clicked;
    
    public MCF() {
        super("MCF", "Middleclick Friends.", Category.MISC, true, false, false);
        this.clicked = false;
    }
    
    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && MCF.mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    private void onClick() {
        final RayTraceResult result = MCF.mc.objectMouseOver;
        final Entity entity;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (Rebirth.friendManager.isFriend(entity.getName())) {
                Rebirth.friendManager.removeFriend(entity.getName());
                Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
            }
            else {
                Rebirth.friendManager.addFriend(entity.getName());
                Command.sendMessage(ChatFormatting.AQUA + entity.getName() + ChatFormatting.AQUA + " has been friended.");
            }
        }
        this.clicked = true;
    }
}
