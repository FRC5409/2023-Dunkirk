// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

	// The motor for the rollers
	private final WPI_TalonFX rollerMotor;

	// The solenoids on either side of the intake
	private final DoubleSolenoid intakeLeft;
	private final DoubleSolenoid intakeRight;

	// Shuffleboard
	private final ShuffleboardTab sb_intakeTab;
	private final NetworkTableEntry nt_rollerMotors;
	private final NetworkTableEntry nt_leftSolenoid;
	private final NetworkTableEntry nt_rightSolenoid;

	/** Creates a new Intake. */
	public Intake() {
		rollerMotor = new WPI_TalonFX(Constants.kIntake.kRollers.kIntakeMotorID);

		rollerMotor.setInverted(true);

		// Invert the motor
		// rollerMotor.setInverted(true);

		intakeLeft = new DoubleSolenoid(
			Constants.kPneumatics.kHubModuleID,
			Constants.kPneumatics.kPneumaticsModuleType,
			Constants.kIntake.kDoubleSolenoids.kLeftFwdChannel,
			Constants.kIntake.kDoubleSolenoids.kLeftBwdChannel);

		intakeRight = new DoubleSolenoid(
			Constants.kPneumatics.kHubModuleID,
			Constants.kPneumatics.kPneumaticsModuleType,
			Constants.kIntake.kDoubleSolenoids.kRightFwdChannel,
			Constants.kIntake.kDoubleSolenoids.kRightBwdChannel);

		stopRolling();
		intakeUp();

		// Shuffleboard
		sb_intakeTab = Shuffleboard.getTab("Intake");
		nt_rollerMotors = sb_intakeTab.add("Roller motors", rollerMotor.get()).getEntry();
		nt_leftSolenoid = sb_intakeTab.add("L solenoid", intakeLeft.get().toString()).getEntry();
		nt_rightSolenoid = sb_intakeTab.add("R solenoid", intakeLeft.get().toString()).getEntry();

	}

	/** Rolling the wheels forward by making the motor rotate CW */
	public void rollForward() {
		rollerMotor.set(Constants.kIntake.kRollers.kIntakeRollersSpeed);
	}

	/** Rolling the wheels backward by making the motor rotate CCW */
	public void rollBackward() {
		rollerMotor.set(-Constants.kIntake.kRollers.kIntakeRollersSpeed);
	}

	/** Stop rolling the rollers */
	public void stopRolling() {
		rollerMotor.set(Constants.kIntake.kRollers.kIntakeRollersStopped);
	}

	/** Makes the intake go down */
	public void intakeDown() {
		intakeLeft.set(Value.kReverse);
		intakeRight.set(Value.kReverse);
	}

	/** Makes the intake go up */
	public void intakeUp() {
		intakeLeft.set(Value.kForward);
		intakeRight.set(Value.kForward);
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run

		nt_rollerMotors.setDouble(rollerMotor.get());
		nt_leftSolenoid.setString(intakeLeft.get().toString());
		nt_rightSolenoid.setString(intakeRight.get().toString());
	}

	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during a simulation

	}
}
