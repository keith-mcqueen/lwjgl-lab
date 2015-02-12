package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.Comparator;

public enum Axis implements Comparator<Point3D> {
    X {
        @Override
        public double valueOf(Point3D point) {
            return point.x;
        }

        @Override
        public void setValue(Point3D point, double value) {
            point.x = value;
        }

        @Override
        public Axis[] getComplements() {
            return new Axis[]{Y, Z};
        }
    },
    Y {
        @Override
        public double valueOf(Point3D point) {
            return point.y;
        }

        @Override
        public void setValue(Point3D point, double value) {
            point.y = value;
        }

        @Override
        public Axis[] getComplements() {
            return new Axis[]{X, Z};
        }
    },
    Z {
        @Override
        public double valueOf(Point3D point) {
            return point.z;
        }

        @Override
        public void setValue(Point3D point, double value) {
            point.z = value;
        }

        @Override
        public Axis[] getComplements() {
            return new Axis[]{X, Y};
        }
    };

    public abstract double valueOf(Point3D point);

    public abstract void setValue(Point3D point, double value);

    public abstract Axis[] getComplements();

    @Override
    public int compare(Point3D o1, Point3D o2) {
        return Double.valueOf(this.valueOf(o1)).compareTo(Double.valueOf(this.valueOf(o2)));
    }

}
