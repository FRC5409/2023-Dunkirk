// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Index extends SubsystemBase {
  private final CANSparkMax middleRollar;
  //private final CANSparkMax indexNeo2;


  /** Creates a new Index. */
  public Index(){
    middleRollar = new CANSparkMax(Constants.kIndex.kindexmiddleRollar, MotorType.kBrushless);
  //  indexNeo2 = new CANSparkMax();
    middleRollar.restoreFactoryDefaults();
		middleRollar.setIdleMode(IdleMode.kBrake);
		middleRollar.setInverted(true);	// Invert the rollers
    middleRollar.burnFlash();

  }

  public void indexNeo1forward(){
    middleRollar.set(Constants.kIndex.kindexmiddleRollarForward*-1); // makes motor move forward
  }

	public void indexNeo1Backward(){
    middleRollar.set(Constants.kIndex.kindexmiddleRollarBackward*-1); // makes motor move backwards
  }

  public void indexNeo1Stop (){
    middleRollar.set(0); // stops motor
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
