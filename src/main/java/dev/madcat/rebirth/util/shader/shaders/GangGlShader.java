

package dev.madcat.rebirth.util.shader.shaders;

import dev.madcat.rebirth.util.shader.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class GangGlShader extends FramebufferShader
{
    private static GangGlShader INSTANCE;
    public float time;
    
    public GangGlShader() {
        super("gang.frag");
        this.time = 0.0f;
    }
    
    public static FramebufferShader getInstance() {
        if (GangGlShader.INSTANCE == null) {
            GangGlShader.INSTANCE = new GangGlShader();
        }
        return GangGlShader.INSTANCE;
    }
    
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("speed");
        this.setupUniform("shift");
        this.setupUniform("texture");
        this.setupUniform("color");
        this.setupUniform("radius");
        this.setupUniform("quality");
        this.setupUniform("divider");
        this.setupUniform("maxSample");
    }
    
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform2f(this.getUniform("speed"), (float)this.animationSpeed, (float)this.animationSpeed);
        GL20.glUniform1f(this.getUniform("shift"), 1.0f);
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("radius"), Math.min(this.radius, 2.5f));
        GL20.glUniform1f(this.getUniform("quality"), this.quality);
        GL20.glUniform1f(this.getUniform("divider"), this.divider);
        GL20.glUniform1f(this.getUniform("maxSample"), this.maxSample);
        if (!this.animation) {
            return;
        }
        this.time = ((this.time > 100.0f) ? 0.0f : ((float)(this.time + 0.01 * this.animationSpeed)));
    }
}
