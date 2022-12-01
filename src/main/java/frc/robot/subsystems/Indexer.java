// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Indexer extends SubsystemBase {
  /** Creates a new Indexer. */

  // The NEO motors involved
  private final CANSparkMax feederMotor;

  public Indexer() {

		feederMotor = new CANSparkMax(Constants.kIndexer.kMotors.kFeederMotorID, MotorType.kBrushless);
	}

	/** Makes the feeder roll forward */
	public void feederForward() {
		feederMotor.set(Constants.kIndexer.kMotors.kFeederSpeed);
	}

	/** Makes the feeder stop */
	public void feederStop() {
		feederMotor.set(Constants.kIndexer.kMotors.kFeederStopped);
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run

		SmartDashboard.putNumber("Feeder Speed", feederMotor.get());
	}
}
