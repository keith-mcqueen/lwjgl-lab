package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import static org.lwjgl.opengl.GL11.*;

public class FlatShader extends Shader {
    public FlatShader(LightSource... lightSources) {
        super(lightSources);
    }

    @Override
    public void shade(Triangle3D triangle, Color baseColor) {
        // get the triangle's centroid
        Point3D centroid = triangle.getCentroid();

        // get the triangle's face normal
        Point3D normal = triangle.getNormal();

        // start with an "empty" (black) color
        Color shade = new Color(0.0f, 0.0f, 0.0f);

        // for each configured light source, add the color for this triangle
        for (LightSource lightSource : this.getLightSources()) {
            shade = shade.add(lightSource.getColorFor(null, centroid, null));
        }

        // set the final color
        glColor3d(shade.red(), shade.green(), shade.blue());

        // now render the triangle
        glBegin(GL_TRIANGLES);
        triangle.getVertices().forEach(v -> glVertex3d(v.x, v.y, v.z));
        glEnd();
    }
}
