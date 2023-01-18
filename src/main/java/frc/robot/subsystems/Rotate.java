// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Rotate extends SubsystemBase {
  private final CANSparkMax m_Rotateneo;
  private final ShuffleboardTab sb_rotateTab;
  private final RelativeEncoder m_encoder;

 

  /** Creates a new ExampleSubsystem. */
  public Rotate() {
    m_Rotateneo = new CANSparkMax(Constants.kRotate.kTurrentNeoID,MotorType.kBrushless);
    //m_Rotateneo.restoreFactoryDefaults();
		m_Rotateneo.setIdleMode(IdleMode.kBrake);
		m_Rotateneo.setInverted(true);	// Invert the rollers
    m_Rotateneo.burnFlash();

    m_encoder = m_Rotateneo.getEncoder(); 

    sb_rotateTab = Shuffleboard.getTab("Rotation");

    //ge_velocity = sb_rotateTab.add("velocity",rotateForward().getEntry());

    
    disable();
  }

  public void disable(){
    m_Rotateneo.set(0);
  }
  public void rotateForward(){
    m_Rotateneo.set(Constants.kRotate.krotateForwardSpeed);
  }
  
  public void rotateBackward(){
    m_Rotateneo.set(Constants.kRotate.krotateBackwardSpeed);
  }
  
  public double getSpeed(){
   return m_encoder.getVelocity();
  }

  public double getRotation(){
    return m_encoder.getPosition();
  }

  public double getPositionConversionFactor(){
    return m_encoder.getPositionConversionFactor();
  }

  public REVLibError setPositionConversionFactor(int i){
    return m_encoder.setPositionConversionFactor(Constants.kRotate.mulitFactor);
  }



  @Override
  public void periodic() {
    sb_rotateTab.addNumber("Velocity", this::getSpeed);
    sb_rotateTab.addDouble("Rotation", this::getRotation);
    sb_rotateTab.addNumber("PositionConversion", this:: getPositionConversionFactor);

    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
