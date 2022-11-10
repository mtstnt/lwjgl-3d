package project.loader3;

import engine.game.GameObject;
import engine.graphics.Mesh;
import engine.graphics.shaders.ShaderProgram;
import org.joml.Vector3f;

public class Train extends GameObject {

    private static final String MODEL_PATH = "assets/models/toy_train.obj";

    protected Vector3f position;
    protected ShaderProgram shader;

    public Train() throws Exception {
        this.position = new Vector3f().zero();
        this.shader = ShaderProgram.createDefault();

        this.mesh = Mesh.fromObj(MODEL_PATH);
        this.mesh.setShader(shader);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        this.mesh.getTransform().translate(new Vector3f(this.position));
    }
}
