// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Rotate extends SubsystemBase {
  private final CANSparkMax m_Rotateneo;
 

  /** Creates a new ExampleSubsystem. */
  public Rotate() {
    m_Rotateneo = new CANSparkMax(Constnats.kRotate,Motortype.Brushless);
   
		m_Rotateneo.setIdleMode();
		m_Rotateneo.setInverted(true);	// Invert the rollers
    m_Rotateneo.burnFlash();
  }

  public void disable(){
    m_Rotateneo.set(0);
  }
  public void rotateForward(){
    m_Rotateneo.set();
  }
  
  public void rotateBackward(){}

  public void rotateLeft (){


  }


  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
}
