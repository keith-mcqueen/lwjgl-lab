package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import static java.lang.Math.max;
import static org.lwjgl.opengl.GL11.*;

public class PointLightSource extends LightSource {
    private Point3D location;

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
//        this.renderRay(location);

        Point3D direction = this.getLocation().subtract(location);
        double distanceSquared = direction.length();
        return reflectiveColor.times(this.getColor()).times(max(0.0d, normal.dot(direction)) / distanceSquared);
    }

    public Point3D getLocation() {
        return this.location;
    }

    protected void setLocation(Point3D newLoc) {
        this.location = newLoc;
    }

    private void renderRay(Point3D location) {
        Color color = this.getColor();
        glColor3d(color.red(), color.green(), color.blue());
        glBegin(GL_LINES);
        Point3D myLoc = getLocation();
        glVertex3d(myLoc.x, myLoc.y, myLoc.z);
        glVertex3d(location.x, location.y, location.z);
        glEnd();
    }
}
