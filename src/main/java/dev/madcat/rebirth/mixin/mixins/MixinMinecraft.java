

package dev.madcat.rebirth.mixin.mixins;

import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.crash.*;
import org.spongepowered.asm.mixin.injection.*;
import org.lwjgl.input.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.gui.*;
import javax.annotation.*;
import org.spongepowered.asm.mixin.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.*;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraft
{
    @Inject(method = { "shutdownMinecraftApplet" }, at = { @At("HEAD") })
    private void stopClient(final CallbackInfo callbackInfo) {
        this.unload();
    }
    
    @Redirect(method = { "run" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(final Minecraft minecraft, final CrashReport crashReport) {
        this.unload();
    }
    
    @Inject(method = { "runTickKeyboard" }, at = { @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 0, shift = At.Shift.BEFORE) })
    private void onKeyboard(final CallbackInfo callbackInfo) {
        final int n;
        final int i = n = ((Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey());
        if (Keyboard.getEventKeyState()) {
            final KeyEvent event = new KeyEvent(i);
            MinecraftForge.EVENT_BUS.post((Event)event);
        }
    }
    
    @Shadow
    public abstract void displayGuiScreen(@Nullable final GuiScreen p0);
    
    @Inject(method = { "displayGuiScreen" }, at = { @At("HEAD") })
    private void displayGuiScreen(final GuiScreen screen, final CallbackInfo ci) {
        final ClickGui ClickGui = (ClickGui)Rebirth.moduleManager.getModuleByDisplayName("ClickGui");
    }
    
    @Inject(method = { "runTick()V" }, at = { @At("RETURN") })
    private void runTick(final CallbackInfo callbackInfo) {
        final ClickGui ClickGui = (ClickGui)Rebirth.moduleManager.getModuleByDisplayName("ClickGui");
    }
    
    private void unload() {
        Rebirth.LOGGER.info("Initiated client shutdown.");
        Rebirth.onUnload();
        Rebirth.LOGGER.info("Finished client shutdown.");
    }
}
