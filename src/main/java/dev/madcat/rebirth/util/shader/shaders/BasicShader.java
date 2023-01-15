

package dev.madcat.rebirth.util.shader.shaders;

import dev.madcat.rebirth.util.shader.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class BasicShader extends FramebufferShader
{
    private static BasicShader INSTANCE;
    private float time;
    private float timeMult;
    private int timeLimit;
    
    private BasicShader(final String fragmentShader) {
        super(fragmentShader);
        this.time = 0.0f;
        this.timeMult = 0.1f;
        this.timeLimit = 10000;
    }
    
    private BasicShader(final String fragmentShader, final float timeMult) {
        super(fragmentShader);
        this.time = 0.0f;
        this.timeMult = 0.1f;
        this.timeLimit = 10000;
        this.timeMult = timeMult;
    }
    
    public static FramebufferShader getInstance(final String fragmentShader) {
        if (BasicShader.INSTANCE == null || !BasicShader.INSTANCE.fragmentShader.equals(fragmentShader)) {
            BasicShader.INSTANCE = new BasicShader(fragmentShader);
        }
        return BasicShader.INSTANCE;
    }
    
    public static FramebufferShader getInstance(final String fragmentShader, final float timeMult) {
        if (BasicShader.INSTANCE == null || !BasicShader.INSTANCE.fragmentShader.equals(fragmentShader)) {
            BasicShader.INSTANCE = new BasicShader(fragmentShader, timeMult);
        }
        return BasicShader.INSTANCE;
    }
    
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
    }
    
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        if (!this.animation) {
            return;
        }
        this.time = ((this.time > this.timeLimit) ? 0.0f : (this.time += this.timeMult * this.animationSpeed));
    }
}
