

package dev.madcat.rebirth.util.shader.shaders;

import dev.madcat.rebirth.util.shader.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class HolyFuckShader extends FramebufferShader
{
    private static HolyFuckShader INSTANCE;
    protected float time;
    
    private HolyFuckShader() {
        super("holyfuck.frag");
        this.time = 0.0f;
    }
    
    public static HolyFuckShader getInstance() {
        if (HolyFuckShader.INSTANCE == null) {
            HolyFuckShader.INSTANCE = new HolyFuckShader();
        }
        return HolyFuckShader.INSTANCE;
    }
    
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
        this.setupUniform("speed");
        this.setupUniform("shift");
        this.setupUniform("color");
        this.setupUniform("radius");
        this.setupUniform("quality");
        this.setupUniform("divider");
        this.setupUniform("texelSize");
        this.setupUniform("maxSample");
    }
    
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform1f(this.getUniform("quality"), this.quality);
        GL20.glUniform1f(this.getUniform("divider"), this.divider);
        GL20.glUniform2f(this.getUniform("speed"), (float)this.animationSpeed, (float)this.animationSpeed);
        GL20.glUniform1f(this.getUniform("shift"), 1.0f);
        GL20.glUniform1f(this.getUniform("maxSample"), this.maxSample);
        if (!this.animation) {
            return;
        }
        this.time = ((this.time > 100.0f) ? 0.0f : (this.time += 0.01f * this.animationSpeed));
    }
}
