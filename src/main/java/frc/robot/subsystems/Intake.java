// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */

  private final WPI_TalonFX rollerMotor = new WPI_TalonFX(0); // Not sure which number to use so I used 0

  public Intake() {

    // Sendables
    addChild("Roller Motor", rollerMotor);
  }

  /** Rolling in the ball by making the motor rotate CW */
  public void startRolling() {
    rollerMotor.set(ControlMode.PercentOutput, 1.0);
  }

  /** Stop rolling the rollers */
  public void stopRolling() {
    rollerMotor.set(ControlMode.PercentOutput, 0.0);
  }

  /** Checks if the roller motor is rolling */
  public boolean isRolling() {
    return rollerMotor.get() == 1.0;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putBoolean("isRolling", isRolling());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during a simulation

    SmartDashboard.putBoolean("isRolling", isRolling());
  }
}
