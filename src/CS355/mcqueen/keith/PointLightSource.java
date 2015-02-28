package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import static java.lang.Math.max;
import static java.lang.Math.pow;
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
    public Color getColorFor(Model3D model, Point3D location, Point3D eyeLocation) {
        Color c_r = model.getColor();
        if (null == c_r) {
            return new Color(0.0);
        }

        Point3D n = model.getNormal(location);
        if (null == n) {
            return new Color(0.0);
        }

        Point3D l = this.getLocation().subtract(location).normalize();
        double d_squared = l.length();

        Color diffuseColor = this.computeDiffuseColor(c_r, n, l, d_squared);

        double p = model.getSpecularExponent();
        if (null != eyeLocation && p > 0.0) {
            Point3D e = eyeLocation.subtract(location).normalize();
            Color specularColor = this.computeSpecularColor(model.getSpecularColor(), n, l, d_squared, e, p);
            return diffuseColor.add(specularColor);
        }

        return diffuseColor;
    }

    private Color computeSpecularColor(Color c_p, Point3D n, Point3D l, double d_squared, Point3D e, double p) {
        // compute the reflection direction
        Point3D r = n.times(2.0).times(n.dot(l)).subtract(l);

        Color c_l = this.getColor();

        return c_p.times(c_l.times(pow(max(0.0, e.dot(r)), p) / d_squared));
    }

    public Color computeDiffuseColor(Color c_r, Point3D n, Point3D l, double d_squared) {
        Color c_l = this.getColor();

        return c_r.times(c_l).times(max(0.0, n.dot(l)) / d_squared);
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
