

package dev.madcat.rebirth.mixin.mixins;

import dev.madcat.rebirth.event.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.util.*;
import net.minecraft.client.settings.*;
import dev.madcat.rebirth.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import dev.madcat.rebirth.features.modules.movement.*;
import org.lwjgl.input.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { MovementInputFromOptions.class }, priority = 10001)
public class MixinMovementInputFromOptions extends MovementInput implements MixinInterface
{
    @Redirect(method = { "updatePlayerMoveState" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(final KeyBinding keyBinding) {
        final int keyCode = keyBinding.getKeyCode();
        if (keyCode <= 0) {
            return keyBinding.isKeyDown();
        }
        if (keyCode >= 256) {
            return keyBinding.isKeyDown();
        }
        if (!Rebirth.moduleManager.isModuleEnabled("InvMove")) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen == null) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            return keyBinding.isKeyDown();
        }
        if (keyCode == 42 && !(boolean)InventoryMove.getInstance().shift.getValue()) {
            return keyBinding.isKeyDown();
        }
        return Keyboard.isKeyDown(keyCode);
    }
}
