// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.MiddleRollers;

public class OuttakeBall extends CommandBase {
	/** Creates a new OuttakeBall. */

	private final MiddleRollers m_middleRollers;
	private final Feeder m_feeder;

	public OuttakeBall(MiddleRollers middleRollers, Feeder feeder) {

		m_feeder = feeder;
		m_middleRollers = middleRollers;
		
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(m_middleRollers, m_feeder);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		m_middleRollers.stopRolling();
		m_feeder.stopFeeding();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		m_middleRollers.rollBackward();
		m_feeder.feedBackward();
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		m_middleRollers.stopRolling();
		m_feeder.stopFeeding();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
