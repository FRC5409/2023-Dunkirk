// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Indexer extends SubsystemBase {
  /** Creates a new Indexer. */

  // The motor for the rollers
  private final CANSparkMax rollerMotor;

  public Indexer() {
    rollerMotor = new CANSparkMax(Constants.kIndexer.kRollers.kIndexerMotorID, MotorType.kBrushless);

  }

  /** Makes the rollers roll forward */
  public void rollForward() {
      rollerMotor.set(Constants.kIndexer.kRollers.kIndexerRollersSpeed);
  }

  /** Makes the rollers stop rolling */
  public void stopRolling() {
      rollerMotor.set(Constants.kIndexer.kRollers.kIndexerRollersStopped);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
