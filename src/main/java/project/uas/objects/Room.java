package project.uas.objects;

import engine.Scene;
import engine.graphics.Mesh;
import engine.graphics.Texture;
import engine.graphics.shaders.ShaderProgram;
import engine.graphics.shapes.Plane2D;
import engine.interfaces.Renderable;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Room implements Renderable {

    private Mesh    wallZPos,
                    wallZNeg,
                    wallXPos,
                    wallXNeg,
                    wallYPos,
                    wallYNeg;

    private final float[] TEXCOORD = new float[] {
            0, 0,
            5f, 0,
            5f, 5f,
            0, 5f
    };
    private final int[] INDICES = new int[] {
            0, 2, 3,
            1, 2, 0
    };

    public Room(Vector3f position, float width, float height, float depth) throws Exception {
        Texture texture = Texture.loadFromFile("assets/textures/weathered_planks_diff_2k.jpg");
        ShaderProgram shaderProgram = ShaderProgram.fromPath(
                "assets/shaders/wall/vertex.glsl",
                "assets/shaders/wall/fragment.glsl"
        );

        {
            wallYNeg = new Mesh();
            wallYNeg.setVertices(new float[] {
                    position.x - width, position.y + 0.0f, position.z - depth,
                    position.x + width, position.y + 0.0f, position.z - depth,
                    position.x + width, position.y + 0.0f, position.z + depth,
                    position.x -width, position.y + 0.0f, position.z + depth
            });
            wallYNeg.setNormals(new float[] {
                    0, 1.f, 0,
                    0, 1.f, 0,
                    0, 1.f, 0,
                    0, 1.f, 0
            });
            wallYNeg.setIndices(INDICES);
            wallYNeg.setTexCoords(TEXCOORD);
            wallYNeg.setTexture(texture);
            wallYNeg.setShader(shaderProgram);
        }

        {
            wallYPos = new Mesh();
            wallYPos.setVertices(new float[] {
                    -width, height, -depth,
                    width, height, -depth,
                    width, height, depth,
                    -width, height, depth
            });
            wallYPos.setNormals(new float[] {
                    0, 1, 0,
                    0, 1, 0,
                    0, 1, 0,
                    0, 1, 0
            });
            wallYPos.setIndices(INDICES);
            wallYPos.setTexCoords(TEXCOORD);
            wallYPos.setTexture(texture);
            wallYPos.setShader(shaderProgram);
        }

        {
            wallXNeg = new Mesh();
            wallXNeg.setVertices(new float[] {
                    -width, 0, -depth,
                    -width, 0, depth,
                    -width, height, depth,
                    -width, height, -depth
            });
            wallXNeg.setNormals(new float[] {
                    1, 0, 0,
                    1, 0, 0,
                    1, 0, 0,
                    1, 0, 0
            });
            wallXNeg.setIndices(INDICES);
            wallXNeg.setTexCoords(TEXCOORD);
            wallXNeg.setTexture(texture);
            wallXNeg.setShader(shaderProgram);
        }

        {
            wallXPos = new Mesh();
            wallXPos.setVertices(new float[] {
                    width, 0, -depth,
                    width, 0, depth,
                    width, height, depth,
                    width, height, -depth
            });
            wallXPos.setNormals(new float[] {
                    -1, 0, 0,
                    -1, 0, 0,
                    -1, 0, 0,
                    -1, 0, 0
            });
            wallXPos.setIndices(INDICES);
            wallXPos.setTexCoords(TEXCOORD);
            wallXPos.setTexture(texture);
            wallXPos.setShader(shaderProgram);
        }

        {
            wallZNeg = new Mesh();
            wallZNeg.setVertices(new float[] {
                -width, 0, depth,
                width, 0, depth,
                width, height, depth,
                -width, height ,depth
            });
            wallZNeg.setNormals(new float[] {
                    0, 0, -1,
                    0, 0, -1,
                    0, 0, -1,
                    0, 0, -1
            });
            wallZNeg.setIndices(INDICES);
            wallZNeg.setTexCoords(TEXCOORD);
            wallZNeg.setTexture(texture);
            wallZNeg.setShader(shaderProgram);
        }

        {
            wallZPos = new Mesh();
            wallZPos.setVertices(new float[] {
                    -width, 0, -depth,
                    width, 0, -depth,
                    width, height, -depth,
                    -width, height, -depth
            });
            wallZPos.setNormals(new float[] {
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1
            });
            wallZPos.setIndices(INDICES);
            wallZPos.setTexCoords(TEXCOORD);
            wallZPos.setTexture(texture);
            wallZPos.setShader(shaderProgram);
        }

    }

    @Override
    public void render(Scene scene) {
        wallYNeg.render(scene);
        wallYPos.render(scene);
        wallZNeg.render(scene);
        wallZPos.render(scene);
        wallXNeg.render(scene);
        wallXPos.render(scene);
    }
}
