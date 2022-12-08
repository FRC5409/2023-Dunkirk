// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

	// The motor for the rollers
	private final WPI_TalonFX rollerMotor;

	// The solenoids on either side of the intake
	private final DoubleSolenoid intakeLeft;
	private final DoubleSolenoid intakeRight;

	/** Creates a new Intake. */
	public Intake() {
		rollerMotor = new WPI_TalonFX(Constants.kIntake.kRollers.kIntakeMotorID);
		
		rollerMotor.setNeutralMode(NeutralMode.Brake);	// Set brake mode
		rollerMotor.setInverted(true);	// Invert the motor

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

	}

	/** Rolling the wheels forward (Sucking the ball in) */
	public void rollForward() {
		rollerMotor.set(Constants.kIntake.kRollers.kIntakeRollersSpeed);
	}

	/** Stop rolling the rollers */
	public void stopRolling() {
		rollerMotor.set(Constants.kIntake.kRollers.kIntakeRollersStopped);
	}

	/** Rolling the wheels backward (Spitting the ball out) */
	public void rollBackward() {
		rollerMotor.set(-Constants.kIntake.kRollers.kIntakeRollersSpeed);
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

		SmartDashboard.putData("Roller Motor", rollerMotor);
		SmartDashboard.putData("Left Solenoid", intakeLeft);
		SmartDashboard.putData("Right Solenoid", intakeRight);
	}

	@Override
	public void simulationPeriodic() {
		// This method will be called once per scheduler run during a simulation

		SmartDashboard.putData("Roller Motor", rollerMotor);
		SmartDashboard.putData("Left Solenoid", intakeLeft);
		SmartDashboard.putData("Right Solenoid", intakeRight);
	}
}
