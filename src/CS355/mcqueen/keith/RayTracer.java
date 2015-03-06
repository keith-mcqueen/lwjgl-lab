package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

public class RayTracer {
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final double ANTI_ALIASING_RAYS = 15.0;
    private static final int MAX_BOUNCES = 5;
    private static final double[][] CORNERS = new double[][] {{0.0, 0.0}, {1.0, 0.0}, {1.0, 1.0}, {0.0, 1.0}};

    private final Scene scene;
    private final Camera camera;

    private final double top, bottom, left, right, width_per_pixel, height_per_pixel;
    private final int rows, cols;
    private final double depth;

    public static final int SCREEN_WIDTH = 200;
    public static final int SCREEN_HEIGHT = SCREEN_WIDTH;

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
        // use a Point3D for the color so we can add up the contributions and take the average later
        Point3D compositeColor = new Point3D(0.0);

        // first check the corners of the pixel, if we hit something, then go on and do the
        Color cornerColor = null;
        for (double[] corner : CORNERS) {
            cornerColor = this.traceSubpixel(row, col, corner[0], corner[1], 1);
            if (null != cornerColor) {
                break;
            }
        }

        // we didn't get any colors for the pixel corners, then just return the scene's background color
        if (null == cornerColor) {
            return this.scene.getBackgroundColor();
        }

        // compute the composited color for this pixel with random sampling within the pixel
        for (int i = 0; i < ANTI_ALIASING_RAYS; i++) {
            Color subpixel = this.traceSubpixel(row, col, RANDOM.nextDouble(), RANDOM.nextDouble(), MAX_BOUNCES);
            compositeColor = compositeColor.add(subpixel != null ? subpixel : this.scene.getBackgroundColor());
        }

        return new Color(compositeColor.divide(ANTI_ALIASING_RAYS));
    }

    private Color traceSubpixel(int row, int col, double subCol, double subRow, int maxBounces) {
        // compute the location of the pixel on the screen in camera space
        double u = this.left + (this.width_per_pixel * (col + subCol));
        double v = this.bottom + (this.height_per_pixel * (row + subRow));
        double w = -this.depth;

        Point3D s = new Point3D(u, v, w);
        Point3D origin = this.camera;
        Point3D ray = s.subtract(origin).normalize();

        return this.traceRayFromOrigin(origin, ray, maxBounces, null);
    }

    private Color traceRayFromOrigin(Point3D origin, Point3D ray, int maxBounces, Model3D excluded) {
        if (maxBounces <= 0) {
            return LightSource.NO_CONTRIBUTION;
        }

        // check to see if any of the models in the scene are intersected with the ray
        Map.Entry<Model3D, Point3D> intersection = RayTracer.findIntersection(origin, ray, this.scene, excluded);

        // if there were no intersections, then just return the scene's background color
        if (null == intersection) {
            return null;
        }

        // compute the final color (starting with black)
        Model3D model = intersection.getKey();
        Point3D location = intersection.getValue();

        // compute the shading
        Color localColor = LightSource.NO_CONTRIBUTION;
        for (LightSource light : this.scene.getLights()) {
            // add the contribution for the current light
            localColor = localColor.add(light.getColorFor(model, location, origin, this.scene));
        }

        // compute reflection(s)
        double reflectivity = model.getReflectivity();
        Color reflectionColor = LightSource.NO_CONTRIBUTION;
        if (reflectivity > 0.0) {
            // compute the reflection ray (from http://www.flipcode.com/archives/reflection_transmission.pdf)
            Point3D n = model.getNormal(location);
            double n_dot_i = n.dot(ray);
            Point3D reflectedRay = ray.subtract(n.times(2 * n_dot_i));

            // now trace the reflected ray from the point of intersection
            Color reflectedColor = this.traceRayFromOrigin(location, reflectedRay, maxBounces - 1, model);
            if (null == reflectedColor) {
                reflectedColor = this.scene.getBackgroundColor();
            }
            reflectionColor = reflectionColor.add(reflectedColor.times(reflectivity));
        }

        // compute refraction(s)
        double transmissivity = model.getTransmissivity();
        Color refractionColor = LightSource.NO_CONTRIBUTION;
        if (transmissivity > 0.0) {
            // compute the refraction ray (from http://www.flipcode.com/archives/reflection_transmission.pdf)
            double n1_over_n2 = model.getRefractionRatio(location, ray);
            Point3D n = model.getNormal(location);
            double cos = n.dot(ray);
            double sin_squared = n1_over_n2 * n1_over_n2 * (1.0 - cos * cos);
            if (sin_squared <= 1.0) {
                Point3D refractedRay = ray.times(n1_over_n2).subtract(n.times((n1_over_n2 * cos) + Math.sqrt(1.0 - sin_squared)));

                Color refractedColor = this.traceRayFromOrigin(location, refractedRay, maxBounces - 1, model);
                if (null == refractedColor) {
                    refractedColor = this.scene.getBackgroundColor();
                }
                refractionColor = reflectionColor.add(refractedColor.times(transmissivity));
            }
        }

        return localColor.times(1.0 - reflectivity - transmissivity).add(reflectionColor).add(refractionColor);
    }

    static Map.Entry<Model3D, Point3D> findIntersection(Point3D origin, Point3D ray, Iterable<Model3D> models, Model3D excluded) {
        Point3D nearestPOI = null;
        double distance = Double.MAX_VALUE;
        Model3D modelObject = null;
        for (Model3D obj : models) {
            if (null != excluded && excluded.equals(obj)) {
                continue;
            }

            Point3D point = obj.getPointOfIntersection(origin, ray);
            if (null != point) {
                if (null == nearestPOI) {
                    nearestPOI = point;
                    distance = point.subtract(origin).lengthSquared();
                    modelObject = obj;
                    continue;
                }

                double d2 = point.subtract(origin).lengthSquared();
                if (d2 < distance) {
                    nearestPOI = point;
                    distance = d2;
                    modelObject = obj;
                }
            }
        }

        return null == nearestPOI ? null : new AbstractMap.SimpleEntry<>(modelObject, nearestPOI);
    }

    public static void main(String[] args) {
        // set up the camera (at the origin, looking down the Z axis)
//        Camera camera = new Camera(2.0d);
        Camera camera = new Camera(0.0, 0.0, 0.0, 2.0d);

        // set up the scene
        Scene scene = new Scene(SCREEN_WIDTH, SCREEN_HEIGHT, new Color(0.4));

        // add some regularly placed spheres
        scene.addModel(new SphereModel(new Point3D(25.0, -10.0d, -100.0d), 10.0d, Color.randomColor()).setReflectivity(0.4));
        scene.addModel(new SphereModel(new Point3D(25.0, -10.0d, -200.0d), 10.0d, Color.randomColor()).setReflectivity(0.4));
        scene.addModel(new SphereModel(new Point3D(25.0, -10.0d, -300.0d), 10.0d, Color.randomColor()).setReflectivity(0.4));
//        scene.addModel(new SphereModel(new Point3D(25.0, -20.0d, -400.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(25.0, -20.0d, -500.0d), 10.0d, randomColor()));

        scene.addModel(new SphereModel(new Point3D(-25.0, -10.0d, -100.0d), 10.0d, Color.randomColor()).setTransmissivity(0.50, 1.5).setReflectivity(0.10));
        scene.addModel(new SphereModel(new Point3D(-25.0, -10.0d, -200.0d), 10.0d, Color.randomColor()).setTransmissivity(0.50, 1.5).setReflectivity(0.10));
        scene.addModel(new SphereModel(new Point3D(-25.0, -10.0d, -300.0d), 10.0d, Color.randomColor()).setTransmissivity(0.50, 1.5).setReflectivity(0.10));
//        scene.addModel(new SphereModel(new Point3D(-25.0, -20.0d, -400.0d), 10.0d, randomColor()));
//        scene.addModel(new SphereModel(new Point3D(-25.0, -20.0d, -500.0d), 10.0d, randomColor()));

        // single sphere with light directly above the camera
//        scene.addModel(new SphereModel(new Point3D(0.0, -10.0, -150.0), 10.0, Color.randomColor()).setTransmissivity(0.25, 1.5));
//        scene.addModel(new SphereModel(new Point3D(0.0, -10.0, -100.0), 10.0, new Color(1.0)).setTransmissivity(0.75, 1.5).setReflectivity(0.10));

        // add a flat/planar surface underneath
        scene.addModel(new CircularPlaneModel(new Point3D(0.0, -20.0, -150.0), 250.0, new Point3D(0.0, 1.0, 0.0), new Color(0.9))/*.setReflectivity(0.25)*/);

        // add an OBJ model from a file
//        scene.addModel(new ObjFileModel("/Users/keith/School/CS-455/Labs/obj-files-455/flower.obj", new Point3D(0.0, 0.0, -150.0), new Color(1.0, 1.0, 0.0)));
//        scene.addModel(new ObjFileModel("/Users/keith/School/CS-455/Labs/obj-files-455/blocky-cylinder.obj", new Point3D(0.0, -20.0, -100.0), new Color(1.0, 1.0, 0.0)));
//        scene.addModel(new ObjFileModel("/Users/keith/School/CS-455/Labs/obj-files-455/box.obj", new Point3D(0.0, 20.0, -100.0), new Color(1.0, 1.0, 0.0)));

        // add some light sources
//        scene.addLight(new AreaLightSource(new Point3D(0.0, 50.0, -150.0), 20.0, new Point3D(0.0, -1.0, 0.0), new Color(0.5)));
        scene.addLight(new AreaLightSource(new Point3D(50.0, 50.0, -150.0), 20.0, new Point3D(0.0, -1.0, 0.0), new Color(0.5)));
        scene.addLight(new AmbientLightSource(new Color(0.6)));

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
