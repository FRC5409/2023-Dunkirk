package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.ctre.phoenix.sensors.WPI_CANCoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.kDriveTrain;
import frc.robot.Constants.kPneumatics;

public class DriveTrain extends SubsystemBase {

	// Motors
	private final WPI_TalonFX m_motorLeft1;
	private final WPI_TalonFX m_motorLeft2;
	private final WPI_TalonFX m_motorRight1;
	private final WPI_TalonFX m_motorRight2;

	private NeutralMode m_motorNeutralMode;

	private final DifferentialDrive diff_drive;

	// CANCoders
	private final WPI_CANCoder m_cancoderLeft;
	private final WPI_CANCoder m_cancoderRight;
	private final CANCoderConfiguration cancoderConfig;

	// Solenoids (gear shifting)
	private DoubleSolenoid m_doubleSolenoid;
	private DoubleSolenoid.Value m_gearShiftValue;

	// Gyro + odometry for tracking robot pose
	private final Gyro m_gyro;
	private final DifferentialDriveOdometry m_odometry;

	/**
	 * Drive train
	 * 
	 * Pneumatics code crash error:
	 * https://docs.wpilib.org/en/stable/docs/yearly-overview/known-issues.html#code-crash-when-initializing-a-ph-pcm-related-device
	 */
	public DriveTrain() {

		// Motors
		m_motorLeft1 = new WPI_TalonFX(kDriveTrain.kMotors.kLeft1CAN);
		m_motorLeft2 = new WPI_TalonFX(kDriveTrain.kMotors.kLeft2CAN);
		m_motorRight1 = new WPI_TalonFX(kDriveTrain.kMotors.kRight1CAN);
		m_motorRight2 = new WPI_TalonFX(kDriveTrain.kMotors.kRight2CAN);

		m_motorLeft2.follow(m_motorLeft1);
		m_motorRight2.follow(m_motorRight1);

		m_motorRight1.setInverted(true);
		m_motorRight2.setInverted(true);

		m_motorLeft1.configOpenloopRamp(kDriveTrain.kMotors.rampRate);
		m_motorLeft2.configOpenloopRamp(kDriveTrain.kMotors.rampRate);
		m_motorRight1.configOpenloopRamp(kDriveTrain.kMotors.rampRate);
		m_motorRight2.configOpenloopRamp(kDriveTrain.kMotors.rampRate);

		diff_drive = new DifferentialDrive(m_motorLeft1, m_motorRight1);

		// Set brake mode
		m_motorNeutralMode = NeutralMode.Brake;
		setNeutralMode(m_motorNeutralMode);

		// CANCoders
		m_cancoderLeft = new WPI_CANCoder(kDriveTrain.kCANCoder.kLeftCANCoder);
		m_cancoderRight = new WPI_CANCoder(kDriveTrain.kCANCoder.kRightCANCoder);

		resetCANCoders();

		// Config CANCoders
		cancoderConfig = new CANCoderConfiguration();
		cancoderConfig.sensorCoefficient = kDriveTrain.kCANCoder.kSensorCoefficient;
		cancoderConfig.unitString = kDriveTrain.kCANCoder.kUnitString;
		cancoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
		m_cancoderLeft.configAllSettings(cancoderConfig);
		m_cancoderRight.configAllSettings(cancoderConfig);

		// Solenoids
		try {
			m_doubleSolenoid = new DoubleSolenoid(
				kPneumatics.kHubModuleID,
				kPneumatics.kPneumaticsModuleType,
				kDriveTrain.Solenoids.kGearShiftHigh,
				kDriveTrain.Solenoids.kGearShiftLow);
			m_gearShiftValue = m_doubleSolenoid.get();
		} catch (NullPointerException exception) {
			DriverStation.reportError("Error creating Solenoid", exception.getStackTrace());
		}

		// Gyro and odometry
		m_gyro = new Gyro(); // new gyro? or take in parameter
		m_odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d());
	}

	/**
	 * Drive at specified speed and rotation.
	 * 
	 * @param xSpeed forward/backward speed
	 * @param zRotation left/right rotation
	 */
	public void arcadeDrive(double xSpeed, double zRotation) {
		diff_drive.arcadeDrive(xSpeed, zRotation);
	}

	/**
	 * Toggle brake/coast mode.
	 * @return the new mode
	 */
	public NeutralMode toggleNeutralMode() {
		NeutralMode newMode = (m_motorNeutralMode == NeutralMode.Brake) ? NeutralMode.Coast : NeutralMode.Brake;

		setNeutralMode(newMode);

		return newMode;
	}

	/**
	 * Set neutral mode
	 * @param newMode mode to set
	 */
	public void setNeutralMode(NeutralMode newMode) {
		m_motorNeutralMode = newMode;

		m_motorLeft1.setNeutralMode(m_motorNeutralMode);
		m_motorLeft2.setNeutralMode(m_motorNeutralMode);
		m_motorRight1.setNeutralMode(m_motorNeutralMode);
		m_motorRight2.setNeutralMode(m_motorNeutralMode);
	}

	// CANCoders ----------
	public void resetCANCoders() {
		m_cancoderLeft.setPosition(0);
		m_cancoderRight.setPosition(0);
	}

	public double getLeftDistance() {
		return -m_cancoderLeft.getPosition();
	}

	public double getRightDistance() {
		return m_cancoderRight.getPosition();
	}

	public double getLeftCANCoderVelocity() {
		return -m_cancoderLeft.getVelocity();
	}

	public double getRightCANCoderVelocity() {
		return m_cancoderRight.getVelocity();
	}
	// -------------------------

	// Gear shifting ----------
	/**
	 * Change gear
	 * @param newGear set to gear
	 */
	public void changeGear(DoubleSolenoid.Value newGear) {
		try {
			m_gearShiftValue = newGear;
			m_doubleSolenoid.set(m_gearShiftValue);
		} catch (NullPointerException exception) {
			DriverStation.reportError("Solenoids are null", exception.getStackTrace());
		}
	}

	public DoubleSolenoid.Value getGear() {
		return m_doubleSolenoid.get();
	}
	// -------------------------

	// Odometry ----------------
	public Pose2d getPose() {
		return m_odometry.getPoseMeters();
	}

	public DifferentialDriveWheelSpeeds getWheelSpeeds() {
		return new DifferentialDriveWheelSpeeds(m_cancoderLeft.getVelocity(), m_cancoderRight.getVelocity());
	}

	public void resetOdometry(Pose2d pose) {
		resetCANCoders();
		m_odometry.resetPosition(pose, m_gyro.getRotation2d());
	}
	// -------------------------

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		
		SmartDashboard.putNumber("CANCoder L Vel", getLeftCANCoderVelocity());
		SmartDashboard.putNumber("CANCoder R Vel", getRightCANCoderVelocity());
		SmartDashboard.putNumber("CANCoder L Dist", getLeftDistance());
		SmartDashboard.putNumber("CANCoder R Dist", getRightDistance());

		// Update odometry
		m_odometry.update(m_gyro.getRotation2d(), m_cancoderLeft.getPosition(), m_cancoderRight.getPosition());
	}

	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during simulation

	}

}