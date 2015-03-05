package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.pow;
import static org.lwjgl.opengl.GL11.*;

public class PointLightSource extends LightSource {
    private Point3D location;

    public PointLightSource(Point3D location) {
        this(location, 0.5f, 0.5f, 0.5f);
    }

    public PointLightSource(Point3D location, float r, float g, float b) {
        this(location, new Color(r, g, b));
    }

    public PointLightSource(Point3D location, Color color) {
        super(color);
        this.location = location;
    }

    @Override
    public Color getColorFor(Model3D model, Point3D location, Point3D eyeLocation, Iterable<Model3D> obstacles) {
        Color c_r = model.getColor();
        if (null == c_r) {
            return NO_CONTRIBUTION;
        }

        Point3D n = model.getNormal(location);
        if (null == n) {
            return NO_CONTRIBUTION;
        }

        // check to see if there are any obstacles between me and the location
        Point3D l = this.getLocation().subtract(location).normalize();
        Color transmittedColor = NO_CONTRIBUTION;
        Map.Entry<Model3D, Point3D> obstacle = RayTracer.findIntersection(location, l, obstacles, model);
        if (null != obstacle) {
            Model3D occlusion = obstacle.getKey();
            if (occlusion.getTransmissivity() <= 0.0) {
                return NO_CONTRIBUTION;
            }

            transmittedColor = transmittedColor.add(this.getColor().times(occlusion.getColor().times(occlusion.getTransmissivity())));
            transmittedColor = transmittedColor.times(this.getColorFor(occlusion, obstacle.getValue(), eyeLocation, obstacles));
        }

        double d_squared = l.length();
        Color diffuseColor = this.computeDiffuseColor(c_r, n, l, d_squared);

        double p = model.getSpecularExponent();
        Color highlightColor = NO_CONTRIBUTION;
        if (null != eyeLocation && p > 0.0) {
            Point3D e = eyeLocation.subtract(location).normalize();
            highlightColor = this.computeSpecularColor(model.getSpecularColor(), n, l, d_squared, e, p);
        }

        return transmittedColor.add(diffuseColor).add(highlightColor);
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
