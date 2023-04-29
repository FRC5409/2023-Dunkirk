package frc.robot;

public final class InterpolatedData {

    /**
     * Find the closes index in an array
     * @param value the value you give it
     * @param steps how many steps is each point
     * @return index of the closest point - 1
     */
    public static int closestPoint(double value, int steps) {
        return (int) Math.round(value / steps) - 1;
    }

    /**
     * Uses interpolated data to find the new shooter speed
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x
     * @return New interpolated data
     */
    public static double getInterpolatedSpeed(double x1, double y1, double x2, double y2, double x) {//gets the new interpolated speed
        // Y = ( ( X - X1 )( Y2 - Y1) / ( X2 - X1) ) + Y1
        // Y: finding interpolated Y value
        // X: Target X cordinant
        // X1, Y1: first point
        // X2, Y2: second point

        return ((x - x1) * (y2 - y1) / (x2 - x1)) + y1;
    }
}
