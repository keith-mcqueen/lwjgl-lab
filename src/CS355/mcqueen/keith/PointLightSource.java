package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import static java.lang.Math.max;
import static org.lwjgl.opengl.GL11.*;

public class PointLightSource extends LightSource {
    private final Point3D location;

    public PointLightSource(Point3D location) {
        this(0.5f, 0.5f, 0.5f, location);
    }

    public PointLightSource(float r, float g, float b, Point3D location) {
        this(new Color(r, g, b), location);
    }

    public PointLightSource(Color color, Point3D location) {
        super(color);
        this.location = location;
    }

    @Override
    public Color getColorFor(Point3D location, Point3D normal, Color reflectiveColor) {
        // render the ray from this light source to the given point
//        this.renderRay(location);

        Point3D direction = this.location.subtract(location);
        double distanceSquared = direction.length();
        return reflectiveColor.times(this.getColor()).times(max(0.0d, normal.dot(direction)) / distanceSquared);
    }

    private void renderRay(Point3D location) {
        Color color = this.getColor();
        glColor3d(color.red(), color.green(), color.blue());
        glBegin(GL_LINES);
        glVertex3d(this.location.x, this.location.y, this.location.z);
        glVertex3d(location.x, location.y, location.z);
        glEnd();
    }
}
