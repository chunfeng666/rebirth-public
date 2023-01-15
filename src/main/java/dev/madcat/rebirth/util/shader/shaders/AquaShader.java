

package dev.madcat.rebirth.util.shader.shaders;

import dev.madcat.rebirth.util.shader.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class AquaShader extends FramebufferShader
{
    private static AquaShader INSTANCE;
    public float time;
    
    private AquaShader() {
        super("aqua.frag");
        this.time = 0.0f;
    }
    
    public static FramebufferShader getInstance() {
        if (AquaShader.INSTANCE == null) {
            AquaShader.INSTANCE = new AquaShader();
        }
        return AquaShader.INSTANCE;
    }
    
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }
    
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        if (!this.animation) {
            return;
        }
        this.time = ((this.time > 100.0f) ? 0.0f : ((float)(this.time + 0.01 * this.animationSpeed)));
    }
}
