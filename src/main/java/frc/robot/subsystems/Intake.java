// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Intake extends SubsystemBase {

  private final WPI_TalonFX rollarMotor;
   = new WPI_TalonFX(7)

  public Intake() {

  }

  public void forward(){
    rollarMotor.set(-1); // makes motor move forward
  }

	public void backwards(){
    rollarMotor.set(1); // makes motor move backwards
  }

  public void stop (){
    rollarMotor.set(0); // stops motor
  }

  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
