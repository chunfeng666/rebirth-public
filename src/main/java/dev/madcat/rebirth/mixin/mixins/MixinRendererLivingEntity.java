

package dev.madcat.rebirth.mixin.mixins;

import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import dev.madcat.rebirth.*;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T>
{
    protected MixinRendererLivingEntity() {
        super((RenderManager)null);
    }
    
    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    public void doRenderPre(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (Rebirth.moduleManager.isModuleEnabled("TexturedChams") && entity != null) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }
    
    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    public void doRenderPost(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (Rebirth.moduleManager.isModuleEnabled("TexturedChams") && entity != null) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
}
