// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
	/** Creates a new Intake. */

	// The motor for the rollers
	private final WPI_TalonFX rollerMotor = new WPI_TalonFX(Constants.kIntake.kRollers.kIntakeMotorID);

	// The solenoids on either side of the intake
	private final DoubleSolenoid intakeLeft = new DoubleSolenoid(
		Constants.kPneumatics.kPneumaticsModuleType,
		Constants.kIntake.kDoubleSolenoids.kLeftFwdChannel,
		Constants.kIntake.kDoubleSolenoids.kLeftBwdChannel);
	private final DoubleSolenoid intakeRight = new DoubleSolenoid(
		Constants.kPneumatics.kPneumaticsModuleType,
		Constants.kIntake.kDoubleSolenoids.kRightFwdChannel,
		Constants.kIntake.kDoubleSolenoids.kRightBwdChannel);

	public Intake() {}

	/** Rolling the wheels forward by making the motor rotate CW */
	public void rollForward() {
		rollerMotor.set(Constants.kIntake.kRollers.kIntakeRollersSpeed);
	}

	/** Rolling the wheels backward by making the motor rotate CCW */
	public void rollBackward() {
		rollerMotor.set(-1 * Constants.kIntake.kRollers.kIntakeRollersSpeed);
	}

	/** Stop rolling the rollers */
	public void stopRolling() {
		rollerMotor.set(Constants.kIntake.kRollers.kIntakeRollersStopped);
	}

	/** Makes the left arm of the intake go fwd, bwd or stop
	 * 
	 * @param state One of the three int values 0 (kOff), 1 (kForward) & -1 (kReverse)
	*/
	public void leftSolSet(int state) {
		switch (state) {
			case 0:
				intakeLeft.set(Value.kOff);
				break;
			case 1:
				intakeLeft.set(Value.kForward);
				break;
			case -1:
				intakeLeft.set(Value.kReverse);
				break;
			default:
				intakeLeft.set(Value.kOff);
				break;
		}
	}

	/** Makes the right arm of the intake go fwd, bwd or stop
	 * 
	 * @param state One of the three int values 0 (kOff), 1 (kForward) & -1 (kReverse) 
	*/
	public void rightSolSet(int state) {
		switch (state) {
			case 0:
				intakeRight.set(Value.kOff);
				break;
			case 1:
				intakeRight.set(Value.kForward);
				break;
			case -1:
				intakeRight.set(Value.kReverse);
				break;
			default:
				intakeRight.set(Value.kOff);
				break;
		}
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
