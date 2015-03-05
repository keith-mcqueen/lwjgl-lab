package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.ArrayList;
import java.util.List;

public class SlicedModel extends Model3D {
    private TriangleModel originalModel = null;
    private List<Slice3D> slices = new ArrayList<>();

    public SlicedModel(TriangleModel originalModel, Axis axis, int numSlices) {
        super(originalModel.getLocation(), originalModel.getColor());

        this.originalModel = originalModel;

        this.computeSlices(axis, numSlices);
    }

    private void computeSlices(Axis axis, int numSlices) {
        // get the min/max values of the model
        double min = this.originalModel.getMinimum(axis);
        double max = this.originalModel.getMaximum(axis);

        // determine the slice "thickness", or the distance between slices
        double modelHeight = max - min;
        double sliceHeight = modelHeight / (double) numSlices;

        // for each slice location, slice the model
        for (int i = 0; i <= numSlices; i++) {
            double slicePlane = min + (sliceHeight * i);

            this.slices.add(new Slice3D(this.originalModel, axis, slicePlane));
        }
    }

    @Override
    public void render(Renderer renderer) {
//        renderer.setColor(0.0f, 1.0f, 0.0f);
//        this.originalModel.render(renderer);
        this.slices.forEach(s -> s.render(renderer));
    }

    @Override
    public Point3D getPointOfIntersection(Point3D origin, Point3D ray) {
        return null;
    }

    @Override
    public Point3D getNormal(Point3D point) {
        return null;
    }
}
