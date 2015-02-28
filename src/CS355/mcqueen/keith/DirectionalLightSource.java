package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
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
    public Color getColorFor(Model3D model, Point3D location, Point3D eyeLocation) {
        Color baseColor = model.getColor();
        if (null == baseColor) {
            return new Color(0.0);
        }

        Point3D normal = model.getNormal(location);
        if (null == normal) {
            normal = new Point3D(0.0);
        }

        return baseColor.times(this.getColor()).times(max(0.0d, normal.dot(this.direction)));
    }
}
