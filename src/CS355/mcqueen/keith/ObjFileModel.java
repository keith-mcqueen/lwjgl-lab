package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ObjFileModel extends Model3D {
    private List<Point3D> normals = new ArrayList<>();
    private List<Triangle3D> triangles = new ArrayList<>();

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
        if (tokens.length > 5) {
            throw new IllegalArgumentException("More than 5 tokens for a face definition is unsupported: " + line);
        }

        ArrayList<Point3D> faceVertices = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++) {
            // split the token into subtokens
            String[] subtokens = tokens[i].split("/");

            // get the vertex (represented by the first subtoken)
            int vertexIndex = Integer.valueOf(subtokens[0]);
            faceVertices.add(this.getVertex(vertexIndex - 1));

            // if a vertex normal is represented (the 3rd subtoken), then get that
//            if (subtokens.length >= 3) {
//                int normalIndex = Integer.valueOf(subtokens[2]);
//
//            }
        }

        switch (faceVertices.size()) {
            case 3:
                this.triangles.add(new Triangle3D(faceVertices.get(0), faceVertices.get(1), faceVertices.get(2)));
                break;

            case 4:
                // we've got a quadrilateral that needs to be broken up into 2 triangles
                this.triangles.add(new Triangle3D(faceVertices.get(0), faceVertices.get(1), faceVertices.get(3)));
                this.triangles.add(new Triangle3D(faceVertices.get(1), faceVertices.get(2), faceVertices.get(3)));
                break;

            default:
                throw new IllegalArgumentException("Wrong number of vertices in face: " + line);
        }
    }

    @Override
    public void render(Renderer renderer) {
        this.triangles.forEach(t -> renderer.render(t));
    }
}
