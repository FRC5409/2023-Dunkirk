package frc.robot.util;

/**
 * InterpolatedData is for interpolating data (duh),
 * also has finding the closest point in an array
 * 
 * @author Alexander Szura
 */
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
     * @param x1 first point X
     * @param y1 first point Y
     * @param x2 second point X
     * @param y2 second point Y
     * @param x x position in between those 2 points
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
