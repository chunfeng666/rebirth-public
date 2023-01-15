

package dev.madcat.rebirth.util.shader;

@FunctionalInterface
public interface ShaderProducer
{
    FramebufferShader getInstance();
}
