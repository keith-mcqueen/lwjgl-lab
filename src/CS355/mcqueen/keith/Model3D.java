package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by keith on 1/24/15.
 */
public abstract class Model3D implements Iterable<Point3D> {
    private List<Point3D> vertices = new ArrayList<>();

    @Override
    public Iterator<Point3D> iterator() {
        return this.vertices.iterator();
    }

    public List<Point3D> getVertices() {
        return Collections.unmodifiableList(this.vertices);
    }

    protected void addVertex(int x, int y, int z) {
        this.addVertex((long) x, (long) y, (long) z);
    }

    protected void addVertex(long x, long y, long z) {
        this.vertices.add(new Point3D(x, y, z));
    }

    protected void addVertex(float x, float y, float z) {
        this.addVertex((double) x, (double) y, (double) z);
    }

    protected void addVertex(double x, double y, double z) {
        this.vertices.add(new Point3D(x, y, z));
    }

    public abstract void renderAsWireframe();

    public abstract void renderAsSolid();
}