package engine;

import engine.graphics.*;

import engine.graphics.buffers.Vertex;
import engine.graphics.shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class AssimpLoader {

    private AIScene aiScene;

    private List<Vertex> vertices;
    private List<Vector3i> indices;
    private List<Material> materials;
    private String textureDir;

    private List<MeshObject> meshes;

    public AssimpLoader() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
        materials = new ArrayList<>();
        meshes = new ArrayList<>();
    }

    public List<MeshObject> load(String filepath, String textureDir, int flags) throws Exception {
        this.textureDir = textureDir;

        aiScene = Assimp.aiImportFile(filepath, flags);
        if (aiScene == null) {
            throw new Exception("Failed to load model");
        }

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        for (int i = 0; i < numMaterials; i++) {
            if (aiMaterials == null) {
                throw new Exception("Material of index " + i + " is null.");
            }
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, aiScene);
        }

        AINode root = aiScene.mRootNode();

        assert root != null;

        processNode(root, aiScene);

        return this.meshes;
    }

    public static Model loadModel(String filepath, String texturesDir, int flags) throws Exception {
        System.out.println("Loading " + filepath);
        AssimpLoader assimpLoader = new AssimpLoader();
        List<MeshObject> meshObjects = assimpLoader.load(filepath, texturesDir, flags);
        return new Model(meshObjects);
    }

    private void processNode(AINode node, AIScene scene) throws Exception {
        PointerBuffer sceneMeshes = scene.mMeshes();

        int numMeshes = node.mNumMeshes();
        IntBuffer aiMeshes = node.mMeshes();

        assert aiMeshes != null;
        assert sceneMeshes != null;

        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(sceneMeshes.get(aiMeshes.get(i)));
            MeshObject mesh = processMesh(aiMesh);
            mesh.setShaderProgram(
                    ShaderProgram.fromPath(
                            "assets/shaders/model_shadow/vertex.glsl",
                            "assets/shaders/model_shadow/fragment.glsl"
                    )
            );
            meshes.add(mesh);
        }

        int numChildNode = node.mNumChildren();
        PointerBuffer childNodes = node.mChildren();

        assert childNodes != null;

        for (int i = 0; i < numChildNode; i++) {
            AINode childNode = AINode.create(childNodes.get(i));
            processNode(childNode, scene);
        }
    }

    private void processMaterial(AIMaterial aiMaterial, AIScene scene) throws Exception {
        AIColor4D colour = AIColor4D.create();
        AIString path = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);

        String textPath = path.dataString();
        Texture texture = null;
        if (textPath.length() > 0) {
//            TextureCache textCache = TextureCache.getInstance();
            texture = Texture.loadFromFile(textureDir + "/" + textPath);
        }

        Vector4f ambient = Material.DEFAULT_COLOUR;
        int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, colour);
        if (result == 0) {
            ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4f diffuse = Material.DEFAULT_COLOUR;
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, colour);
        if (result == 0) {
            diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4f specular = Material.DEFAULT_COLOUR;
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0, colour);
        if (result == 0) {
            specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        System.out.println("Texture path: " + textureDir + textPath);

        Material material = new Material(ambient, diffuse, specular, texture);
        materials.add(material);
    }

    private MeshObject processMesh(AIMesh aiMesh) {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();

        processVertices(aiMesh);

        MeshObject mesh = new MeshObject();
        mesh.setVertices(Utils.flattenVertex(vertices));
        mesh.setIndices(Utils.flatten3i(indices));

        int materialIndex = aiMesh.mMaterialIndex();
        mesh.setMaterial(materials.get(materialIndex));

        mesh.bind();

        return mesh;
    }


    private void processVertices(AIMesh mesh) {
        AIVector3D.Buffer aiVertices = mesh.mVertices();
        AIVector3D.Buffer aiNormals = mesh.mNormals();
        AIVector3D.Buffer aiTexCoords = mesh.mTextureCoords(0);

        assert aiNormals != null;

        while (aiVertices.remaining() > 0 && aiNormals.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            AIVector3D aiNormal = aiNormals.get();

            assert aiTexCoords != null;
            System.out.println(aiTexCoords.remaining());
            if (aiTexCoords.remaining() == 0) {
                break;
            }
            AIVector3D aiTexCoord = aiTexCoords.get();

            vertices.add(new Vertex(
                    new Vector3f(aiVertex.x(), aiVertex.y(), aiVertex.z()),
                    new Vector3f(aiNormal.x(), aiNormal.y(), aiNormal.z()),
                    new Vector2f(aiTexCoord.x(), aiTexCoord.y())
            ));
        }

        AIFace.Buffer aiIndices = mesh.mFaces();
        while (aiIndices.remaining() > 0) {
            AIFace aiIndex = aiIndices.get();
            IntBuffer ib = aiIndex.mIndices();
            Vector3i index = new Vector3i();
            for (int i = 0; i < aiIndex.mNumIndices(); i++) {
                index.setComponent(i, ib.get(i));
            }
            indices.add(index);
        }

    }

}
