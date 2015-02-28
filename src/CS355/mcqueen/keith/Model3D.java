package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Model3D {
    private final List<Point3D> vertices = new ArrayList<>();
    private final Color color;
    private final double specularExponent;
    private final Color specularColor;

    protected Model3D(Color color) {
        this(color, 32.0);
    }

    protected Model3D(Color color, double specularExponent) {
        this(color, specularExponent, new Color(1.0));
    }

    protected Model3D(Color color, double specularExponent, Color specularColor) {
        this.color = color;
        this.specularExponent = specularExponent;
        this.specularColor = specularColor;
    }

//    @Override
//    public Iterator<Point3D> iterator() {
//        return this.vertices.iterator();
//    }

    public List<Point3D> getVertices() {
        return Collections.unmodifiableList(this.vertices);
    }

    protected Point3D addVertex(int x, int y, int z) {
        return this.addVertex((long) x, (long) y, (long) z);
    }

    protected Point3D addVertex(long x, long y, long z) {
        return this.addVertex(new Point3D(x, y, z));
    }

    protected Point3D addVertex(float x, float y, float z) {
        return this.addVertex((double) x, (double) y, (double) z);
    }

    protected Point3D addVertex(double x, double y, double z) {
        return this.addVertex(new Point3D(x, y, z));
    }

    protected Point3D addVertex(Point3D vertex) {
        this.vertices.add(vertex);

        return vertex;
    }

    protected Point3D getVertex(int index) {
        return this.vertices.get(index);
    }

    public abstract void render(Renderer renderer);

    public double getMinimum(Axis axis) {
        List<Point3D> copyOfVertices = new ArrayList<>(this.vertices);
        copyOfVertices.sort(axis);

        return axis.valueOf(copyOfVertices.get(0));
    }

    public double getMaximum(Axis axis) {
        List<Point3D> copyOfVertices = new ArrayList<>(this.vertices);
        copyOfVertices.sort(axis.reversed());

        return axis.valueOf(copyOfVertices.get(0));
    }

    public void clearVertices() {
        this.vertices.clear();
    }

    public abstract Point3D getPointOfIntersection(Point3D origin, Point3D ray);

    public Color getColor() {
        return this.color;
    }

    public abstract Point3D getNormal(Point3D point);

    public double getSpecularExponent() {
        return this.specularExponent;
    }

    public Color getSpecularColor() {
        return this.specularColor;
    }
}
