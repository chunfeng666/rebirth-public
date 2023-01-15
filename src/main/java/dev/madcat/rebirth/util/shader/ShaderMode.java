
package dev.madcat.rebirth.util.shader;

import dev.madcat.rebirth.util.shader.shaders.*;

public enum ShaderMode
{
    AQUA("Aqua", AquaShader::getInstance), 
    AQUAGLOW("AquaGlow", AquaGlShader::getInstance), 
    FLOW("Flow", FlowShader::getInstance), 
    FLOWBLUR("zocker", () -> BasicShader.getInstance("flowglow_z.frag", 5.0E-4f)), 
    FLOWGLOW("FlowGLow", FlowGlShader::getInstance), 
    GHOST("Ghost", GlowShader::getInstance), 
    SMOKE("Smoke", SmokeShader::getInstance), 
    RED("Red", RedShader::getInstance), 
    HOLYFUCK("holyfuck", HolyFuckShader::getInstance), 
    GANG("gang", GangGlShader::getInstance), 
    BLUEFLAMES("BlueFlames", () -> BasicShader.getInstance("blueflames.frag", 0.01f)), 
    GAMER("Gang", () -> BasicShader.getInstance("gamer.frag", 0.03f)), 
    CODEX("Codex", () -> BasicShader.getInstance("codex.frag")), 
    GALAXY("Galaxy", () -> BasicShader.getInstance("galaxy33.frag", 0.001f)), 
    DDEV("Ddev", () -> BasicShader.getInstance("ddev.frag")), 
    CRAZY("crazy", () -> BasicShader.getInstance("crazy.frag", 0.01f)), 
    SNOW("snow", () -> BasicShader.getInstance("snow.frag", 0.01f)), 
    TECHNO("techno", () -> BasicShader.getInstance("techno.frag", 0.01f)), 
    GOLDEN("golden", () -> BasicShader.getInstance("golden.frag", 0.01f)), 
    HOTSHIT("hotshit", () -> BasicShader.getInstance("hotshit.frag", 0.005f)), 
    GUISHADER("guishader", () -> BasicShader.getInstance("clickguishader.frag", 0.02f)), 
    HIDEF("hidef", () -> BasicShader.getInstance("hidef.frag", 0.05f)), 
    HOMIE("homie", () -> BasicShader.getInstance("homie.frag", 0.001f)), 
    KFC("kfc", () -> BasicShader.getInstance("kfc.frag", 0.01f)), 
    OHMYLORD("ohmylord", () -> BasicShader.getInstance("ohmylord.frag", 0.01f)), 
    SHELDON("sheldon", () -> BasicShader.getInstance("sheldon.frag", 0.001f)), 
    SMOKY("smoky", () -> BasicShader.getInstance("smoky.frag", 0.001f)), 
    STROXINJAT("stroxinjat", () -> BasicShader.getInstance("stroxinjat.frag")), 
    WEIRD("weird", () -> BasicShader.getInstance("weird.frag", 0.01f)), 
    YIPPIEOWNS("yippieOwns", () -> BasicShader.getInstance("yippieOwns.frag"));
    
    private final String name;
    private final ShaderProducer shaderProducer;
    
    private ShaderMode(final String name, final ShaderProducer shaderProducer) {
        this.name = name;
        this.shaderProducer = shaderProducer;
    }
    
    public String getName() {
        return this.name;
    }
    
    public FramebufferShader getShader() {
        return this.shaderProducer.getInstance();
    }
}
