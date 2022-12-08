// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class MiddleRollers extends SubsystemBase {
  /** Creates a new MiddleRollers. */

  private final CANSparkMax middleRollerMotor;

	public MiddleRollers() {

		middleRollerMotor = new CANSparkMax(Constants.kMiddleRollers.kRollerMotorID, MotorType.kBrushless);
		
		middleRollerMotor.restoreFactoryDefaults();
		middleRollerMotor.setIdleMode(IdleMode.kBrake);
		middleRollerMotor.setInverted(true);	// Invert the rollers
		middleRollerMotor.burnFlash();

	}

	/** Makes the rollers roll forward (Sucking the ball in) */
	public void rollForward() {
    	middleRollerMotor.set(Constants.kMiddleRollers.kRollersSpeed);
	}

	/** Makes the rollers stop rolling */
	public void stopRolling() {
    	middleRollerMotor.set(Constants.kMiddleRollers.kRollersStopped);
	}

	/** Makes the rollers roll backward (Spitting the ball out) */
	public void rollBackward() {
		middleRollerMotor.set(-Constants.kMiddleRollers.kRollersSpeed);
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run

		SmartDashboard.putNumber("Middle Rollers Speed", middleRollerMotor.get());
	}
}
