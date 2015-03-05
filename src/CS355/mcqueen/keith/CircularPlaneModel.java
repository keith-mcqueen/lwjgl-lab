package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class CircularPlaneModel extends PlaneModel {
    private final double radius;

    public CircularPlaneModel(Point3D location, double radius, Point3D normal, Color color) {
        super(location, normal, color);

        this.radius = radius;
    }

    @Override
    protected boolean contains(Point3D point) {
        // a point lies within a circle if it's distance to the center is less than (or equal to) the radius
        double d = point.subtract(this.getCentroid()).length();

        return d <= this.radius;
    }

    @Override
    public void render(Renderer renderer) {

    }
}
