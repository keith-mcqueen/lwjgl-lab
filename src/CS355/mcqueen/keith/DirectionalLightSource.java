package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
import static java.lang.Math.max;

public class DirectionalLightSource extends LightSource {
    private final Point3D direction;

    public DirectionalLightSource(float r, float g, float b, Point3D direction) {
        this(new Color(r, g, b), direction);
    }

    public DirectionalLightSource(Color color, Point3D direction) {
        super(color);
        this.direction = direction.normalize();
    }

    @Override
    public Color getColorFor(Point3D location, Point3D normal, Color reflectiveColor) {
        return reflectiveColor.times(this.getColor()).times(max(0.0d, normal.dot(this.direction)));
    }
}
