package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kLimelight;

public class Limelight extends SubsystemBase {

    private ShuffleboardTab tab = Shuffleboard.getTab("limelight");
    private GenericEntry distanceEntry = tab.add("Distance to target: ", 0).getEntry();

    NetworkTable limeTable;

    double angleGoal;
    double distanceToTarget;

   
    public Limelight() {
        NetworkTableInstance.getDefault().startServer();
        NetworkTableInstance.getDefault().setServerTeam(5409);
        distanceToTarget = -1;  
        
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        if (isVisable()) {
            angleGoal = (kLimelight.mountAngle + getYOffset()) * (3.14159 / 180);//getting the angle to the goal in radians (tan requires radians to work)
            distanceToTarget = (kLimelight.targetHeight - kLimelight.heightOffFloor) / Math.tan(angleGoal);//getting distance to target
        }

        distanceEntry.setDouble(distanceToTarget);
    }

    @Override
    public void simulationPeriodic() {}

    public void turnOffLimelight() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("LEDMode").setNumber(1);
    }

    public void turnOnLimelight() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("LEDMode").setNumber(3);
    }

    public double getXOffset() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(-1);
    }

    public double getYOffset() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(-1);
    }

    public boolean isVisable() {
        try {
            return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0) == 1;
        } catch (Exception e) { 
            return false;
        }
    
    }
    
}
