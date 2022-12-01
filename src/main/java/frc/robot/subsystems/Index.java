// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;




import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
// could be PWMSparkMax instead of Spark
public class Index extends SubsystemBase {
  private final CANSparkMax indexNeo1;
  private final Spark indexNeo2;

  /** Creates a new Index. */
  public Index() {
    indexNeo1 = new Spark(Constants.kIndex.kindexNeo1);
    indexNeo2 = new Spark(Constants.kIndex.kindexNeo2);
  }

  public void indexNeo1forward(){
    indexNeo1.set(Constants.kIndex.kindexNeo1Forward* -1); // makes motor move forward
  }

	public void indexNeo1Backward(){
    indexNeo1.set(Constants.kIndex.kindexNeo1Backward* -1); // makes motor move backwards
  }

  public void indexNeo1Stop (){
    indexNeo1.set(0); // stops motor
  }

  public void indexNeo2forward(){
    indexNeo2.set(Constants.kIndex.kindexNeo2Forward* -1); // makes motor move forward
  }

	public void indexNeo2Backward(){
    indexNeo2.set(Constants.kIndex.kindexNeo2Backward* -1); // makes motor move backwards
  }

  public void indexNeo2Stop (){
    indexNeo2.set(0); // stops motor
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
