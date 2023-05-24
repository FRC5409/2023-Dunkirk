package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.Pigeon2Configuration;
import com.ctre.phoenix.sensors.WPI_Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.kConfig;

public class Gyroscope extends SubsystemBase {

    // Pigeon hardware
    private final WPI_Pigeon2 m_pigeon;

    // Config Pigeon
    private final Pigeon2Configuration pigeon_config;

    // Accerlerometor on the roborio
    private final BuiltInAccelerometer m_accelerometer;

    private double headingSpeed = 0;

    private ShuffleboardTab gyroTab;
    private GenericEntry angleEntry, rateEntry, rotationEntry, yawEntry, pitchEntry, rollEntry, speedEntry;

    private final boolean debug = false;

    /**
     * Wrapper for the Pigeon gyro.
     * 
     * From user guide:
     * https://store.ctr-electronics.com/content/user-manual/Pigeon2%20User's%20Guide.pdf
     * 
     * +X points forward.
     * +Y points to the left.
     * +Z points to the sky.
     * Pitch is about +Y
     * Roll is about +X
     * Yaw is about +Z
     */
    public Gyroscope() {
        m_pigeon = new WPI_Pigeon2(Constants.kGyro.kPigeonCAN);

        // Configure Pigeon
        pigeon_config = new Pigeon2Configuration();

        pigeon_config.MountPosePitch = Constants.kGyro.kMountPosePitch;
        pigeon_config.MountPoseRoll = Constants.kGyro.kMountPoseRoll;
        pigeon_config.MountPoseYaw = Constants.kGyro.kMountPoseYaw;

        m_pigeon.configAllSettings(pigeon_config);

        m_accelerometer = new BuiltInAccelerometer();

        if (debug || kConfig.masterDebug) {

            System.out.println("lets see if this happens twice");

            gyroTab          = Shuffleboard.getTab("Pigeon Gyro Tab");

            angleEntry       = gyroTab.add("Pigeon Angle", 0).getEntry();
            rateEntry        = gyroTab.add("Pigeon Rate", 0).getEntry();
            rotationEntry    = gyroTab.add("Pigeon Rotation", 0).getEntry();
            yawEntry         = gyroTab.add("Pigeon Yaw", 0).getEntry();
            pitchEntry       = gyroTab.add("Pigeon Pitch", 0).getEntry();
            rollEntry        = gyroTab.add("Pigeon Roll", 0).getEntry();
            speedEntry       = gyroTab.add("Pigeon Speed", 0).getEntry();
        }

    }

    // Give values ----------
    public void resetGyro() {
        // https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/interfaces/Gyro.html#reset()
        m_pigeon.reset();
    }

    public double getAngle() {
        // https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/interfaces/Gyro.html#getAngle()
        return m_pigeon.getAngle();
    }

    public double getAbsoluteAngle() {
        return getAngle() % 360 - 180;
    }

    public double getRate() {
        // https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/interfaces/Gyro.html#getRate()
        return m_pigeon.getRate();
    }

    public Rotation2d getRotation2d() {
        // https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/interfaces/Gyro.html#getRotation2d()
        return m_pigeon.getRotation2d();
    }

    public double getYaw() {
        // https://api.ctr-electronics.com/phoenix/release/java/com/ctre/phoenix/sensors/BasePigeon.html#getYaw()
        return m_pigeon.getYaw();
    }

    public double getPitch() {
        // https://api.ctr-electronics.com/phoenix/release/java/com/ctre/phoenix/sensors/BasePigeon.html#getPitch()
        return m_pigeon.getPitch();
    }

    public double getRoll() {
        // https://api.ctr-electronics.com/phoenix/release/java/com/ctre/phoenix/sensors/BasePigeon.html#getRoll()
        return m_pigeon.getRoll();
    }

    public ErrorCode getLastError() {
        // https://api.ctr-electronics.com/phoenix/release/java/com/ctre/phoenix/sensors/BasePigeon.html#getLastError()
        return m_pigeon.getLastError();
    }

    public double getForwardAccel() {
        //TODO: Confirm that it is getZ() and not getX();
        return -m_accelerometer.getZ() * 9.81;
    }

    public double getForwardSpeed() {
        return headingSpeed;
    }

    // ------------------------------

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        headingSpeed = getForwardAccel();
        
        if (debug || kConfig.masterDebug) {
            angleEntry      .setDouble(getAngle());
            rateEntry       .setDouble(getRate());
            rotationEntry   .setDouble(getRotation2d().getDegrees());
            yawEntry        .setDouble(getYaw());
            pitchEntry      .setDouble(getPitch());
            rollEntry       .setDouble(getRoll());
            speedEntry      .setDouble(getForwardSpeed());
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

}