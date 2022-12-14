package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.Pigeon2Configuration;
import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Gyro extends SubsystemBase {

    // Pigeon hardware
    private final WPI_Pigeon2 m_pigeon;

    // Config Pigeon
    private final Pigeon2Configuration pigeon_config;

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
        
        SmartDashboard.putNumber("Gyro Angle", getAngle());
        SmartDashboard.putNumber("Gyro Rate", getRate());
        SmartDashboard.putNumber("Gyro Rotation", getRotation2d().getDegrees());
        SmartDashboard.putNumber("Gyro Yaw", getYaw());
        SmartDashboard.putNumber("Gyro Pitch", getPitch());
        SmartDashboard.putNumber("Gyro Roll", getRoll());
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

}