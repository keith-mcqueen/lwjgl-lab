package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.lang.Math.min;
import static java.lang.Math.max;

public abstract class Model3D {
    private final List<Point3D> vertices = new ArrayList<>();
    private final Color color;
    private final Point3D location;
    private double specularExponent = 64.0;
    private Color specularColor = new Color(1.0);
    private double reflectivity;
    private double transmissivity;
    private double indexOfRefraction;

    protected Model3D(Point3D location, Color color) {
        this.location = location;
        this.color = color;
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

    public Point3D getLocation() {
        return this.location;
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

    public double getReflectivity() {
        return this.reflectivity;
    }

    public Model3D setReflectivity(double reflectivity) {
        this.reflectivity = min(max(0.0, reflectivity), 1.0);

        return this;
    }

    public double getTransmissivity() {
        return this.transmissivity;
    }

    public Model3D setTransmissivity(double transmissivity, double indexOfRefraction) {
        this.transmissivity = min(max(0.0, transmissivity), 1.0);
        this.indexOfRefraction = indexOfRefraction;

        return this;
    }

    public double getIndexOfRefraction() {
        return this.indexOfRefraction;
    }

    public double getRefractionRatio(Point3D location, Point3D direction) {
        return 0.0;
    }

    public Model3D setSpecularExponent(double exponent) {
        this.specularExponent = exponent;

        return this;
    }

    public Model3D setSpecularColor(Color specularColor) {
        this.specularColor = specularColor;

        return this;
    }
}
