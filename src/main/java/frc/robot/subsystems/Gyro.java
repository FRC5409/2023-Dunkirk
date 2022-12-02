package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.Pigeon2Configuration;
import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Gyro extends SubsystemBase {

    // Pigeon hardware
    private final WPI_Pigeon2 m_pigeon;

    // Config Pigeon
    private final Pigeon2Configuration pigeon_config;

    // Shuffleboard
    private final ShuffleboardTab sb_gyroTab;
    private final NetworkTableEntry nt_angle;
    private final NetworkTableEntry nt_rate;
    private final NetworkTableEntry nt_rotationDegrees;
    private final NetworkTableEntry nt_yaw;
    private final NetworkTableEntry nt_pitch;
    private final NetworkTableEntry nt_roll;

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
    public Gyro() {
        m_pigeon = new WPI_Pigeon2(Constants.kGyro.kPigeonCAN);

        // Configure Pigeon
        pigeon_config = new Pigeon2Configuration();

        pigeon_config.MountPosePitch = Constants.kGyro.kMountPosePitch;
        pigeon_config.MountPoseRoll = Constants.kGyro.kMountPoseRoll;
        pigeon_config.MountPoseYaw = Constants.kGyro.kMountPoseYaw;

        m_pigeon.configAllSettings(pigeon_config);

        // Shuffleboard
        sb_gyroTab = Shuffleboard.getTab("Gyro");
        nt_angle = sb_gyroTab.add("Angle", getAngle()).getEntry();
        nt_rate = sb_gyroTab.add("Rate", getRate()).getEntry();
        nt_rotationDegrees = sb_gyroTab.add("Rotation degrees", getRotation2d().getDegrees()).getEntry();
        nt_yaw = sb_gyroTab.add("Yaw", getYaw()).getEntry();
        nt_pitch = sb_gyroTab.add("Pitch", getPitch()).getEntry();
        nt_roll = sb_gyroTab.add("Roll", getRoll()).getEntry();

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
    // ------------------------------

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        nt_angle.setDouble(getAngle());
        nt_rate.setDouble(getRate());
        nt_rotationDegrees.setDouble(getRotation2d().getDegrees());
        nt_yaw.setDouble(getYaw());
        nt_pitch.setDouble(getPitch());
        nt_roll.setDouble(getRoll());
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

}