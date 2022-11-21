package engine.graphics;

import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Texture {

    private int textureID;
    private String type;

    public Texture() {
        this.textureID = glGenTextures();
    }

    public void bindToActiveTexture() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.textureID);
    }

    public void setup(ByteBuffer buffer, int width, int height, int channels) {
        glBindTexture(GL_TEXTURE_2D, this.textureID);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        int channelEnum = 0;
        if (channels == 3) {
            channelEnum = GL_RGB;
        } else if (channels == 4) {
            channelEnum = GL_RGBA;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, channelEnum, width, height, 0, channelEnum, GL_UNSIGNED_BYTE, buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glGenerateMipmap(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static Texture loadFromFile(String filepath) throws Exception {
        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];

        ByteBuffer buffer = STBImage.stbi_load(filepath, width, height, channels, 0);

        if (buffer == null) {
            throw new Exception("Failed to load image from stbi_load");
        }

        Texture texture = new Texture();
        texture.setup(buffer, width[0], height[0], channels[0]);

        STBImage.stbi_image_free(buffer);

        return texture;
    }

}
