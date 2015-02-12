package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Slice3D extends TriangleModel {
    private final TriangleModel originalModel;
    private final Axis axis;
    private final double slicePlane;

    public Slice3D(TriangleModel originalModel, Axis axis, double slicePlane) {
        this.originalModel = originalModel;
        this.axis = axis;
        this.slicePlane = slicePlane;

        this.sliceModel();
    }

    private void sliceModel() {
        // process the triangles from the original model
        this.originalModel.getTriangles().forEach(this::processTriangle);
    }

    private void processTriangle(Triangle3D triangle) {
        // if the triangle intersects the plane...
        if (this.checkTriangle(triangle)) {
            // then slice the triangle
            this.sliceTriangle(triangle);
        }
    }

    private boolean checkTriangle(Triangle3D triangle) {
        double min = triangle.getMinimum(this.axis);
        double max = triangle.getMaximum(this.axis);

        return min <= this.slicePlane && this.slicePlane <= max;
    }

    private void sliceTriangle(Triangle3D triangle) {
        // get the vertices from the triangle
        List<Point3D> vertices = new ArrayList<>(triangle.getVertices());

        // sort the vertices along the given axis
        vertices.sort(this.axis);

        int[] left = null;
        int[] right = null;
        // determine whether the slice plane is above or below the midpoint of the triangle
        if (this.slicePlane < this.axis.valueOf(vertices.get(1))) {
            // bottom half
            left = new int[] {0, 1};
            right = new int[] {0, 2};
        } else {
            // top half
            left = new int[] {0, 2};
            right = new int[] {1, 2};
        }

        // interpolate the left and right points of the slice
        Point3D leftPoint = this.interpolate(vertices.get(left[0]), vertices.get(left[1]));
        Point3D rightPoint = this.interpolate(vertices.get(right[0]), vertices.get(right[1]));

        // create a pseudo-triangle from these points and save it
        this.addTriangle(new Triangle3D(leftPoint, rightPoint, rightPoint));
    }

    private Point3D interpolate(Point3D pointA, Point3D pointB) {
        /*
        def interpolate_endpoint(a, b, scanline):
        width = b[Y] - a[Y]
        if width == 0:
        return a

        initial_x = Fraction(a[X])
        delta_x = Fraction(b[X] - a[X])
        initial_r = Fraction(a[R])
        delta_r = Fraction(b[R] - a[R])
        initial_g = Fraction(a[G])
        delta_g = Fraction(b[G] - a[G])
        initial_b = Fraction(a[B])
        delta_b = Fraction(b[B] - a[B])

        distance = scanline - a[Y]
        x = initial_x + Fraction(distance, width) * delta_x
        r = initial_r + Fraction(distance, width) * delta_r
        g = initial_g + Fraction(distance, width) * delta_g
        b = initial_b + Fraction(distance, width) * delta_b

        return x, scanline, r, g, b
        */

        double width = this.axis.valueOf(pointB) - this.axis.valueOf(pointA);
        if (Double.compare(width, 0.0) == 0) {
            return pointA;
        }

        Point3D interpolated = new Point3D(0.0, 0.0, 0.0);

        // first set the known value of the point
        this.axis.setValue(interpolated, this.slicePlane);

        // interpolate the values for the other axes
        for (Axis a : this.axis.getComplements()) {
            double initial = a.valueOf(pointA);
            double delta = a.valueOf(pointB) - a.valueOf(pointA);
            double distance = this.slicePlane - this.axis.valueOf(pointA);

            double value = initial + ((distance / width) * delta);

            a.setValue(interpolated, value);
        }

        return interpolated;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setColor(1.0f, 0.0f, 0.0f);
        super.render(renderer);
    }
}
