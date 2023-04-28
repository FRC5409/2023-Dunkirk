package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kLimelight;

public class Limelight extends SubsystemBase {

    public enum LedMode {
        kModePipeline(0), kModeOff(1), kModeBlink(2), kModeOn(3);

        LedMode(double value) {
            this.value = value;
        }

        public final double value;
    }

    private ShuffleboardTab limeTab;
    private GenericEntry distanceEntry;
    private GenericEntry angleEntry;

    private final NetworkTable limeTable;

    private final boolean debug = true;

   
    public Limelight() {
        NetworkTableInstance.getDefault().startServer();
        NetworkTableInstance.getDefault().setServerTeam(5409);

        limeTable = NetworkTableInstance.getDefault().getTable("limelight");

        if (debug) {
            limeTab = Shuffleboard.getTab("limelight");
            distanceEntry = limeTab.add("Distance to target: ", 0).getEntry();
            angleEntry = limeTab.add("Angle to target", 0).getEntry();
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        if (debug) {
            if (isVisable()) {
                distanceEntry.setDouble(getDistanceToTarget());
                angleEntry.setDouble(getAngleToTarget());
            }
        }
    }

    @Override
    public void simulationPeriodic() {}

    /**
     * Sets the limelight LED state
     * @param mode the mode you want to set it to
     */
    public void setLedMode(LedMode mode) {
        limeTable.getEntry("LEDMode").setNumber(mode.value);
    }

    /**
     * Gets the X angle to the target
     * @return angle in degrees
     */
    public double getXAngle() {
        return limeTable.getEntry("tx").getDouble(-1);
    }

    /**
     * Gets the Y angle to the target
     * @return angle in degrees
     */
    public double getYAngle() {
        return limeTable.getEntry("ty").getDouble(-1);
    }

    /**
     * Is a target visable
     * @return true or false
     */
    public boolean isVisable() {
        return limeTable.getEntry("tv").getDouble(0) == 1;
    }

    /**
     * Is the limelight on
     * @return true or false
     */
    public boolean isOn() {
        return limeTable.getEntry("LEDMode").getInteger(-1) == 3;
    }

    /**
     * @deprecated tx returns angle to target
     * @return angle to target
     */
    public double getAngleToTarget() {
        double vpw = 2 * Math.tan(kLimelight.HORIZONTAL_FOV / 2);
        double nx = getXAngle();

        double x = vpw / 2 * nx;

        double ax = Math.atan2(1, x);

        return ax;
    }

    public double getDistanceToTarget() {
        double angleGoal = (kLimelight.mountAngle + getYAngle()) * (Math.PI / 180);//getting the angle to the goal in radians (tan requires radians to work)
        double distanceToTarget = (kLimelight.targetHeight - kLimelight.heightOffFloor) / Math.tan(angleGoal);//getting distance to target

        return distanceToTarget;
    }
    
}
