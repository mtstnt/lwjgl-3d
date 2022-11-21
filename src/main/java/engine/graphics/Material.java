package engine.graphics;

import engine.graphics.shaders.ShaderProgram;
import org.joml.Vector4f;

public class Material {

    private Vector4f ambient;
    private Vector4f diffuse;
    private Vector4f specular;
    private Texture texture;

    public static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);

    public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, Texture texture) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.texture = texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void bindToShader(ShaderProgram shaderProgram) {
        texture.bindToActiveTexture();
        shaderProgram.setUniformInt("u_textureSampler", 0);
    }
}
