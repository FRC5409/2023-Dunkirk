// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.MiddleRollers;

public class IntakeBall extends CommandBase {
	/** Creates a new intakeBall */

	private final Intake m_intake;
	private final MiddleRollers m_middleRollers;

	public IntakeBall(Intake intake, MiddleRollers middleRollers) {

		m_intake = intake;
		m_middleRollers = middleRollers;

		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(m_intake, m_middleRollers);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		m_intake.intakeDown();
		m_intake.stopRolling();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		m_intake.rollForward();
		m_middleRollers.rollForward();
	}

	// Called once the command ends or is interrupted
	@Override
	public void end(boolean interrupted) {
		m_intake.stopRolling();
		m_middleRollers.stopRolling();
		m_intake.intakeUp();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
