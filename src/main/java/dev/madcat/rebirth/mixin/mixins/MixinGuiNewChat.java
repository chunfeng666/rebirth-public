

package dev.madcat.rebirth.mixin.mixins;

import java.util.*;
import org.spongepowered.asm.mixin.*;
import dev.madcat.rebirth.features.modules.client.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;

@Mixin({ GuiNewChat.class })
public class MixinGuiNewChat extends Gui
{
    @Shadow
    @Final
    public List<ChatLine> drawnChatLines;
    private ChatLine chatLine;
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.drawRect(left, top, right, bottom, (Chat.getInstance().isOn() && (boolean)Chat.getInstance().clean.getValue()) ? 0 : color);
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadow(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
        return 0;
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false))
    public int drawnChatLinesSize(final List<ChatLine> list) {
        return (Chat.getInstance().isOn() && (boolean)Chat.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2, remap = false))
    public int chatLinesSize(final List<ChatLine> list) {
        return (Chat.getInstance().isOn() && (boolean)Chat.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
}
