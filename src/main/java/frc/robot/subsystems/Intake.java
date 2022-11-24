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
		Constants.kPneumatics.kHubModuleID,
		Constants.kPneumatics.kPneumaticsModuleType,
		Constants.kIntake.kDoubleSolenoids.kLeftFwdChannel,
		Constants.kIntake.kDoubleSolenoids.kLeftBwdChannel);
	private final DoubleSolenoid intakeRight = new DoubleSolenoid(
		Constants.kPneumatics.kHubModuleID,
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
		rollerMotor.set(-Constants.kIntake.kRollers.kIntakeRollersSpeed);
	}

	/** Stop rolling the rollers */
	public void stopRolling() {
		rollerMotor.set(Constants.kIntake.kRollers.kIntakeRollersStopped);
	}

	/** Makes the arm of the intake go fwd, bwd or stop
	 * 
	 * @param state One of the three int values 0 (kOff), 1 (kForward) & -1 (kReverse)
	*/
	public void intakeArmSet(int state) {
		switch (state) {
			case Constants.kIntake.kDoubleSolenoids.kOffInt:
				intakeLeft.set(Value.kOff);
				intakeRight.set(Value.kOff);
				break;
			case Constants.kIntake.kDoubleSolenoids.kForwardInt:
				intakeLeft.set(Value.kForward);
				intakeRight.set(Value.kForward);
				break;
			case Constants.kIntake.kDoubleSolenoids.kReverseInt:
				intakeLeft.set(Value.kReverse);
				intakeRight.set(Value.kReverse);
				break;
			default:
				intakeLeft.set(Value.kOff);
				intakeRight.set(Value.kOff);
				break;
		}
	}

	public int getRightIntake() {
		if (intakeRight.get() == Value.kOff) {return 0;} 
		else if (intakeRight.get() == Value.kForward) {return 1;} 
		else if (intakeRight.get() == Value.kReverse) {return -1;} 
		else {return 7;}
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
