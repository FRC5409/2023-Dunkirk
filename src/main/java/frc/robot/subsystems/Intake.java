// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.kIntake;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class Intake extends SubsystemBase {

  public enum Value {
    kOff,
    kForward,
    kReverse
  }

  private final WPI_TalonFX rollarMotor;
  private final DoubleSolenoid leftDoubleSolenoid;
  private final DoubleSolenoid rightDoubleSolenoid;

  public Intake() {
    rollarMotor = new WPI_TalonFX(Constants.kIntake.kRollarMotor);
    leftDoubleSolenoid = new DoubleSolenoid(Constants.kPneumatics.kHubModuleID, Constants.kPneumatics.kPneumaticsModuleType,Constants.kIntake.kleftSolenoidForward,Constants.kIntake.kleftSolenoidBackward);
    rightDoubleSolenoid = new DoubleSolenoid(Constants.kPneumatics.kHubModuleID, Constants.kPneumatics.kPneumaticsModuleType, Constants.kIntake.krightSolenoidForward,Constants.kIntake.krightSolenoidBackward);

    motorStop();
    doubleSoldnoidUp();
    setupRollarMotor();


  }
  public void setupRollarMotor(){
    rollarMotor.setInverted(true);
    rollarMotor.configFactoryDefault();
    rollarMotor.setNeutralMode(NeutralMode.Coast);
    setPIDFvalues(kIntake.kPID.kP, kIntake.kPID.kI, kIntake.kPID.kD, kIntake.kPID.kF);

  }

  public void setPIDFvalues(double p, double i, double d, double f) {
    rollarMotor.config_kP(0, p, kIntake.timeOutMs);
    rollarMotor.config_kI(0, i, kIntake.timeOutMs);
    rollarMotor.config_kD(0, d, kIntake.timeOutMs);
    rollarMotor.config_kF(0, f, kIntake.timeOutMs);
  }

  public double getVelocity() {
    return getAverageSpeed();
  } 
  public double getAverageSpeed() {
    return (Math.abs(rollarMotor.getSelectedSensorVelocity()))/ 2048.0 * 600;//convers to RPM
  }

  public void spinRollarAtSpeed(double RPM) {
    rollarMotor.set(TalonFXControlMode.Velocity, RPM * 2048.0 / 600.0);//spins at RPM
}
 
  public void motorForward(){
    rollarMotor.set(Constants.kIntake.kmotorForward*-1); // makes motor move forward
  }

	public void motorBackward(){
    rollarMotor.set(Constants.kIntake.kmotorBackward* -1); // makes motor move backwards
  }

  public void motorStop (){
    rollarMotor.set(0); // stops motor
  }

  public void doubleSoldnoidDown(){
    leftDoubleSolenoid.set(kReverse);
    rightDoubleSolenoid.set(kReverse);
  }

  public void doubleSoldnoidUp(){
    leftDoubleSolenoid.set(kForward);
    rightDoubleSolenoid.set(kForward);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
