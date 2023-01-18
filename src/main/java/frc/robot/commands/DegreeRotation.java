// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Rotate;

public class DegreeRotation extends CommandBase {
  private final Rotate m_rotate;
  /** Creates a new DegreeRotation. */
  public DegreeRotation(Rotate rotate) {
    m_rotate = rotate;

    addRequirements(m_rotate);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}
  

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    if (m_rotate.getPositionConversionFactor() == 90){
     // m_rotate.setPositionConversionFactor(0);
      m_rotate.disable();
    } else{
      m_rotate.rotateForward();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_rotate.disable();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
