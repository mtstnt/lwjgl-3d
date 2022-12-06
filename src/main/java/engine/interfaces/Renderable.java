package engine.interfaces;

import engine.Scene;
import engine.graphics.shaders.ShaderProgram;

public interface Renderable {
    void render(Scene scene);
    void render(Scene scene, ShaderProgram customShader);
}
