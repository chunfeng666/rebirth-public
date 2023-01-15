

package dev.madcat.rebirth.util.shader.shaders;

import dev.madcat.rebirth.util.shader.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class ItemShader extends FramebufferShader
{
    private static ItemShader INSTANCE;
    protected float time;
    
    private ItemShader() {
        super("item.frag");
        this.time = 0.0f;
    }
    
    public static ItemShader getInstance() {
        if (ItemShader.INSTANCE == null) {
            ItemShader.INSTANCE = new ItemShader();
        }
        return ItemShader.INSTANCE;
    }
    
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("dimensions");
        this.setupUniform("texture");
        this.setupUniform("image");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
        this.setupUniform("blur");
        this.setupUniform("minAlpha");
    }
    
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("dimensions"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform1i(this.getUniform("image"), 0);
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform1f(this.getUniform("divider"), this.divider);
        GL20.glUniform1f(this.getUniform("maxSample"), this.maxSample);
        GL20.glUniform1i(this.getUniform("blur"), 1);
        GL20.glUniform1f(this.getUniform("minAlpha"), 1.0f);
        if (!this.animation) {
            return;
        }
        this.time = ((this.time > 100.0f) ? 0.0f : ((float)(this.time + 0.001 * this.animationSpeed)));
    }
}
