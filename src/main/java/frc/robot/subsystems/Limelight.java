package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kConfig;
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
    private GenericEntry customAngleEntry;

    private ShuffleboardTab  trainingTab;

    private final NetworkTable limeTable;

    private final boolean debug = false;

   
    public Limelight() {
        NetworkTableInstance.getDefault().startServer();
        NetworkTableInstance.getDefault().setServerTeam(kConfig.teamNumber);

        limeTable            = NetworkTableInstance.getDefault().getTable("limelight");

        if (debug || kConfig.masterDebug) {
            limeTab              = Shuffleboard.getTab("limelight");
            if (kConfig.training) {
                trainingTab      = Shuffleboard.getTab("Training");
                distanceEntry    = trainingTab.add("Distance to target", 0).getEntry();
            } else {
                distanceEntry    = limeTab.add("Distance to target", 0).getEntry();
            }
            angleEntry           = limeTab.add("Angle to target", 0).getEntry();
            customAngleEntry     = limeTab.add("Custom Angle", 0).getEntry();
        } else if (kConfig.training) {
            trainingTab      = Shuffleboard.getTab("Training");
            distanceEntry    = trainingTab.add("Distance to target", 0).getEntry();
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        if (debug || kConfig.masterDebug) {
            if (isVisable()) {
                distanceEntry.setDouble(getDistanceToTarget());
                angleEntry.setDouble(getXAngle());
                customAngleEntry.setDouble(getAngleToTarget());
            }
        } else if (kConfig.training) {
            distanceEntry.setDouble(getDistanceToTarget());
        }
    }

    @Override
    public void simulationPeriodic() {}

    /**
     * Sets the limelight LED state
     * @param mode the mode you want to set it to
     */
    public void setLedMode(LedMode mode) {
        limeTable.getEntry("ledMode").setNumber(mode.value);
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
        return limeTable.getEntry("ledMode").getInteger(-1) == 3;
    }

    /**
     * Gets custom data
     * @param data The string of the data you want to grab
     * @param defaultValue default value if that value could not be found
     * @return limelights data value
     */
    public Number getData(String data, Number defaultValue) {
        return limeTable.getEntry(data).getNumber(defaultValue);
    }

    /**
     * Gets custom data
     * @param data The string of data you want to grab
     * @return limelights data value
     */
    public Number getData(String data) {
        return getData(data, 0);
    }
    
    /**
     * Set custom data
     * @param data The string of the data you want to set
     * @param value the value you want to set it to
     */
    public void setData(String data, Number value) {
        limeTable.getEntry(data).setNumber(value);
    }

    /**
     * @deprecated tx returns angle to target
     * @return angle to target
     */
    public double getAngleToTarget() {
        double vpw = 2 * Math.tan(kLimelight.HORIZONTAL_FOV / 2);
        double nx = (double) getData("tx0");

        double x = vpw / 2 * nx;

        double ax = Math.atan2(1, x);

        return ax;
    }

    /**
     * Gets the distance to the target
     * @return distance to the target
     */
    public double getDistanceToTarget() {
        double angleGoal = (kLimelight.mountAngle + getYAngle()) * (Math.PI / 180);
        double distanceToTarget = (kLimelight.targetHeight - kLimelight.heightOffFloor) / Math.tan(angleGoal);

        return distanceToTarget;
    }
    
}
