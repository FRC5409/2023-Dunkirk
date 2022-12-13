// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kIntake;
import frc.robot.subsystems.Intake;

public class IntakeMovement extends CommandBase {

  private final Intake m_intake;

  private ShuffleboardTab tab = Shuffleboard.getTab("Intake");
  private NetworkTableEntry kP = tab.add("kP", 0).getEntry();
  private NetworkTableEntry kI = tab.add("kI", 0).getEntry();
  private NetworkTableEntry kD = tab.add("kD", 0).getEntry();
  private NetworkTableEntry kF = tab.add("kF", 0).getEntry();


  /** Creates a new solenoidsmovement. */
  public IntakeMovement(Intake intake) {
    m_intake = intake;

    addRequirements(m_intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intake.doubleSoldnoidDown();
    m_intake.motorStop();

    m_intake.setPIDFvalues(kP.getDouble(0),kI.getDouble(0),kD.getDouble(0),kF.getDouble(0));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double rollarSpeed = 0;

    rollarSpeed = kIntake.krollarSpeed;
    m_intake.spinRollarAtSpeed(rollarSpeed);
  }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.doubleSoldnoidUp();
    m_intake.motorStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}