

package dev.madcat.rebirth.util.shader.shaders;

import dev.madcat.rebirth.util.shader.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class SmokeShader extends FramebufferShader
{
    private static SmokeShader INSTANCE;
    protected float time;
    
    private SmokeShader() {
        super("smoke.frag");
        this.time = 0.0f;
    }
    
    public static SmokeShader getInstance() {
        if (SmokeShader.INSTANCE == null) {
            SmokeShader.INSTANCE = new SmokeShader();
        }
        return SmokeShader.INSTANCE;
    }
    
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
        this.setupUniform("radius");
        this.setupUniform("divider");
        this.setupUniform("maxSample");
        this.setupUniform("texelSize");
    }
    
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), new ScaledResolution(this.mc).getScaledWidth() / 2.0f, new ScaledResolution(this.mc).getScaledHeight() / 2.0f);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform1f(this.getUniform("divider"), this.divider);
        GL20.glUniform1f(this.getUniform("maxSample"), this.maxSample);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / this.mc.displayWidth * (this.radius * this.quality), 1.0f / this.mc.displayHeight * (this.radius * this.quality));
        if (!this.animation) {
            return;
        }
        this.time = ((this.time > 100.0f) ? 0.0f : ((float)(this.time + 0.05 * this.animationSpeed)));
    }
}
