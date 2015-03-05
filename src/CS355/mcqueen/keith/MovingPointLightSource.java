package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.Random;

public class MovingPointLightSource extends PointLightSource {
    private static final double MIN_VAL = -100.0;
    private static final double MAX_VAL = 100.0;
    private static final double MAX_DELTA = 5.0;
    private static final int TIME_STEP = 50;

    private final Point3D directions = new Point3D(1, 1, 1);
    private final Random random = new Random(System.currentTimeMillis());
    private long lastUpdate = System.currentTimeMillis();

    public MovingPointLightSource(Point3D location) {
        super(location);
    }

    public MovingPointLightSource(float r, float g, float b, Point3D location) {
        super(location, r, g, b);
    }

    public MovingPointLightSource(Color color, Point3D location) {
        super(location, color);
    }

    @Override
    public Point3D getLocation() {
        Point3D currentLocation = super.getLocation();
        long timeDiff = System.currentTimeMillis() - this.lastUpdate;
        if (timeDiff < TIME_STEP) {
            return currentLocation;
        }

        Point3D deltaLocation = new Point3D(
                this.random.nextDouble(),
                this.random.nextDouble(),
                this.random.nextDouble()).times(MAX_DELTA).times(this.directions);

        Point3D newLocation = currentLocation.add(deltaLocation);

        // make sure that the new location stays within the bounds
        if (newLocation.x < MIN_VAL) {
            newLocation.x = MIN_VAL;
            directions.x *= - 1;
        }
        if (newLocation.y < MIN_VAL) {
            newLocation.y = MIN_VAL;
            directions.y *= - 1;
        }
        if (newLocation.z < MIN_VAL) {
            newLocation.z = MIN_VAL;
            directions.z *= - 1;
        }
        if (newLocation.x > MAX_VAL) {
            newLocation.x = MAX_VAL;
            directions.x *= - 1;
        }
        if (newLocation.y > MAX_VAL) {
            newLocation.y = MAX_VAL;
            directions.y *= - 1;
        }
        if (newLocation.z > MAX_VAL) {
            newLocation.z = MAX_VAL;
            directions.z *= - 1;
        }

        // save the new location for next time
        this.setLocation(newLocation);
        this.lastUpdate = System.currentTimeMillis();

        return newLocation;
    }
}
