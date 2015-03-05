package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.Map;

import static java.lang.Math.max;

public class DirectionalLightSource extends LightSource {
    private final Point3D direction;

    public DirectionalLightSource(Point3D direction) {
        this(0.5f, 0.5f, 0.5f, direction);
    }

    public DirectionalLightSource(float r, float g, float b, Point3D direction) {
        this(new Color(r, g, b), direction);
    }

    public DirectionalLightSource(Color color, Point3D direction) {
        super(color);
        this.direction = direction.normalize();
    }

    @Override
    public Color getColorFor(Model3D model, Point3D location, Point3D eyeLocation, Iterable<Model3D> obstacles) {
        Color baseColor = model.getColor();
        if (null == baseColor) {
            return NO_CONTRIBUTION;
        }

        Point3D normal = model.getNormal(location);
        if (null == normal) {
            return NO_CONTRIBUTION;
        }

        // check to see if there are any obstacles between me and the location
        Map.Entry<Model3D, Point3D> obstacle = RayTracer.findIntersection(location, this.direction, obstacles, model);
        if (null != obstacle) {
            return NO_CONTRIBUTION;
        }

        return baseColor.times(this.getColor()).times(max(0.0d, normal.dot(this.direction)));
    }
}
