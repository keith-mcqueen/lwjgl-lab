package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ObjFileModel extends Model3D {
    private List<Point3D> normals = new ArrayList<>();
    private List<SimpleModelFace> faces = new ArrayList<>();

    public ObjFileModel(String objFile) {
        this.parseObjFile(objFile);
    }

    private void parseObjFile(String objFile) {
        try {
            Files.lines(Paths.get(objFile)).forEach(this::handleObjLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleObjLine(String line) {
        // split the line into tokens
        String[] tokens = line.split(" ");

        // handle the line based on the first token
        switch (tokens[0]) {
            case "v":
                this.handleVertexLine(line, tokens);
                break;

            case "vn":
                this.handleNormalLine(line, tokens);
                break;

            case "f":
                this.handleFaceLine(line, tokens);
                break;
        }
    }

    private void handleVertexLine(String line, String... tokens) {
        if (tokens.length < 4) {
            throw new IllegalArgumentException("At least 4 tokens expected for vertex line: " + line);
        }

        this.addVertex(Double.valueOf(tokens[1]), Double.valueOf(tokens[2]), Double.valueOf(tokens[3]));
    }

    private void handleNormalLine(String line, String... tokens) {
        if (tokens.length < 4) {
            throw new IllegalArgumentException("At least 4 tokens expected for vertex-normal line: " + line);
        }

        this.normals.add(new Point3D(Double.valueOf(tokens[1]), Double.valueOf(tokens[2]), Double.valueOf(tokens[3])));
    }

    private void handleFaceLine(String line, String... tokens) {
        if (tokens.length < 4) {
            throw new IllegalArgumentException("At least 4 tokens expected for face line: " + line);
        }

        SimpleModelFace face = new SimpleModelFace();
        for (int i = 1; i < tokens.length; i++) {
            // split the token into subtokens
            String[] subtokens = tokens[i].split("/");

            // get the vertex (represented by the first subtoken)
            int vertexIndex = Integer.valueOf(subtokens[0]);
            face.addVertex(this.getVertex(vertexIndex - 1));

            // if a vertex normal is represented (the 3rd subtoken), then get that
//            if (subtokens.length >= 3) {
//                int normalIndex = Integer.valueOf(subtokens[2]);
//
//            }
        }

        this.faces.add(face);
    }

    @Override
    public void renderAsWireframe() {
        this.faces.forEach(SimpleModelFace::renderAsWireframe);
    }

    @Override
    public void renderAsSolid() {
        this.faces.forEach(SimpleModelFace::renderAsSolid);
    }
}
