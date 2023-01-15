

package dev.madcat.rebirth.util.shader;

import java.awt.image.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.*;
import java.io.*;

public class EfficientTexture extends AbstractTexture
{
    private int[] textureData;
    private final int width;
    private final int height;
    
    public EfficientTexture(final BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.textureData, 0, bufferedImage.getWidth());
        this.updateEfficientTexture();
    }
    
    public EfficientTexture(final int textureWidth, final int textureHeight) {
        this.width = textureWidth;
        this.height = textureHeight;
        this.textureData = new int[textureWidth * textureHeight];
        TextureUtil.allocateTexture(this.getGlTextureId(), textureWidth, textureHeight);
    }
    
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
    }
    
    private void updateEfficientTexture() {
        TextureUtil.uploadTexture(this.getGlTextureId(), (int[])this.textureData, this.width, this.height);
        this.textureData = new int[0];
    }
}
