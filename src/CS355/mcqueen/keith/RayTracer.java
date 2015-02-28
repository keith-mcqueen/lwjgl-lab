package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import static CS355.mcqueen.keith.Color.randomColor;
import static CS355.LWJGL.Point3D.randomPoint;

public class RayTracer {
    private final Scene scene;
    private final Camera camera;

    private final double top, bottom, left, right, width_per_pixel, height_per_pixel;
    private final int rows, cols;
    private final double depth;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;

    public RayTracer(Camera camera, Scene scene) {
        this.camera = camera;
        this.scene = scene;

        this.rows = this.scene.getHeight();
        this.cols = this.scene.getWidth();
        this.depth = camera.getFocalLength();

        // compute the top, bottom, left and right end-points
        double half_theta = Math.toRadians(camera.getFieldOfViewInDegrees()) / 2.0d;
        this.top  = this.right = Math.tan(half_theta) * this.depth;
        this.bottom = this.left = -this.top;

        // compute the width- and height-per-pixel
        this.width_per_pixel = (this.right - this.left) / this.cols;
        this.height_per_pixel = (this.top - this.bottom) / this.rows;
    }

    public Color trace(int row, int col) {
        // compute the location of the pixel on the screen in camera space
        double u = this.left + (this.width_per_pixel * (col + 0.5));
        double v = this.bottom + (this.height_per_pixel * (row + 0.5));
        double w = -this.depth;

        Point3D s = new Point3D(u, v, w);
        Point3D ray = s.subtract(this.camera).normalize();

        // check to see if any of the models in the scene are intersected with the ray
        Point3D nearestPOI = null;
        double distance = Double.MAX_VALUE;
        Model3D modelObject = null;
        for (Model3D obj : this.scene) {
            Point3D point = obj.getPointOfIntersection(this.camera, ray);
            if (null != point) {
                if (null == nearestPOI) {
                    nearestPOI = point;
                    distance = point.subtract(this.camera).lengthSquared();
                    modelObject = obj;
                    continue;
                }

                double d2 = point.subtract(this.camera).lengthSquared();
                if (d2 < distance) {
                    nearestPOI = point;
                    distance = d2;
                    modelObject = obj;
                }
            }
        }

        // if there were no intersections, then just return the scene's background color
        if (null == nearestPOI) {
            return this.scene.getBackgroundColor();
        }

        // compute the final color (starting with black)
        Color finalColor = new Color(0.0, 0.0, 0.0);
        for (LightSource light : this.scene.getLights()) {
            finalColor = finalColor.add(light.getColorFor(modelObject, nearestPOI, this.camera));
        }

        return finalColor;
    }

    public static void main(String[] args) {
        // set up the camera (at the origin, looking down the Z axis)
        Camera camera = new Camera(2.0d);

        // set up the scene
        Scene scene = new Scene(SCREEN_WIDTH, SCREEN_HEIGHT, new Color(0.2));

        // add some spheres at random locations
//        scene.addModel(randomSphere());
//        scene.addModel(randomSphere());
//        scene.addModel(randomSphere());
//        scene.addModel(randomSphere());
//        scene.addModel(randomSphere());
//        scene.addModel(randomSphere());
//        scene.addModel(randomSphere());
//        scene.addModel(randomSphere());

        // add some regularly placed spheres
//        scene.addModel(new SphereModel(new Point3D(25.0, 0.0d, -100.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(25.0, 0.0d, -200.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(25.0, 0.0d, -300.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(25.0, 0.0d, -400.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(25.0, 0.0d, -500.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(-25.0, 0.0d, -100.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(-25.0, 0.0d, -200.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(-25.0, 0.0d, -300.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(-25.0, 0.0d, -400.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(-25.0, 0.0d, -500.0d), 10.0d, randomColor()));

        // add some light sources
//        scene.addLight(new PointLightSource(new Point3D(0.0, 50.0, -150.0)));
//        scene.addLight(new AmbientLightSource(0.25));
//        scene.addLight(new AmbientLightSource(0.25));

        // single sphere with light directly above the camera
        scene.addModel(new SphereModel(new Point3D(0.0, 0.0, -50.0), 10.0, randomColor()));
        scene.addLight(new PointLightSource(new Point3D(0.0, 100.0, 0.0)));
        scene.addLight(new AmbientLightSource(0.25));

        // create the ray tracer
        RayTracer tracer = new RayTracer(camera, scene);

        // prepare the image buffer
        BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();

        // render the scene to an image
        for (int row = 0; row < SCREEN_HEIGHT; row++) {
            System.out.println("Processing row " + row);
            for (int col = 0; col < SCREEN_WIDTH; col++) {
                // trace a ray through the current pixel (to get the color for that pixel)
                Color p = tracer.trace(row, col);

                // write the pixel color to the image
                raster.setPixel(
                        col,
                        SCREEN_HEIGHT - 1 - row,
                        new double[]{ p.red() * 255, p.green() * 255, p.blue() * 255 });
            }
        }

        // write the image to a file
        try {
            ImageIO.write(image, "PNG", new File(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
