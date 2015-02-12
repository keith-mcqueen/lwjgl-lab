package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StlFileModel extends AbstractFileModel {
    private List<Point3D> currentVertices = new ArrayList<>();

    public StlFileModel(String stlFile) {
        super(stlFile);

        this.parseFile(stlFile);
    }

    @Override
    protected boolean isValidFile(String file) {
        Path filePath = Paths.get(file);
        String name = filePath.toFile().getName();

        return name.endsWith(".stl");
    }

    @Override
    protected void handleLine(String line) {
        // split the line into tokens
        String[] tokens = line.trim().split(" ");

        // handle the lines based on the first token
        switch (tokens[0]) {
            case "vertex":
                this.handleVertexLine(line, tokens);
                break;

            case "endloop":
                this.addTriangle(new Triangle3D(this.currentVertices.get(0), this.currentVertices.get(1), this.currentVertices.get(2)));
                this.currentVertices.clear();
                break;
        }
    }

    private void handleVertexLine(String line, String... tokens) {
        if (tokens.length < 4) {
            throw new IllegalArgumentException("At least 4 tokens expected for vertex line: " + line);
        }

        // add the vertex to the super class
        Point3D vertex = this.addVertex(Double.valueOf(tokens[1]), Double.valueOf(tokens[2]), Double.valueOf(tokens[3]));

        // also add the vertex to the list of "current" vertices
        this.currentVertices.add(vertex);
    }
}
