// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

import frc.robot.Constants;

public class TakeInTheBall extends CommandBase {
	/** Creates a new TakeInTheBall. */

	private final Intake m_intake;
	// private final XboxController m_controller;
	//Timer timer;

	public TakeInTheBall(Intake intake) {

		this.m_intake = intake;
		// this.m_controller = controller;
		//this.timer = new Timer();

		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(m_intake);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		m_intake.intakeArmSet(Constants.kIntake.kDoubleSolenoids.kReverseInt);
		m_intake.stopRolling();
		//timer.reset();
		//timer.start();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		//System.out.println(m_intake.getRightIntake());
		m_intake.rollForward();
	}

	// Called once the command ends or is interrupted
	@Override
	public void end(boolean interrupted) {
		m_intake.stopRolling();
		m_intake.intakeArmSet(Constants.kIntake.kDoubleSolenoids.kForwardInt);
		//timer.stop();
		//timer.reset();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
