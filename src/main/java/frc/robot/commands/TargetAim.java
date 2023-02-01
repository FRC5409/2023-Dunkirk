// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.kDriveTrain;
import frc.robot.Constants.kLimelight;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;

public class TargetAim extends CommandBase {

	private final Limelight sys_limelight;
	private final DriveTrain sys_drivetrain;
	private final CommandXboxController m_joystick;

	double forwardSpeed, dir, turning;

	/** Creates a new TargetAim. */
	public TargetAim(Limelight limelight, DriveTrain drivetrain, CommandXboxController joystick) {

		sys_limelight = limelight;
		sys_drivetrain = drivetrain;
		m_joystick = joystick;

		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(sys_drivetrain, sys_limelight);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		sys_limelight.turnOn();
		//sys_limelight.setData("pipeline", 1);
		System.out.println("Initialized");
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {

		forwardSpeed = m_joystick.getRightTriggerAxis() - m_joystick.getLeftTriggerAxis();

        if (!sys_limelight.isVisible()) {
            //dir on the controller
            dir = sys_limelight.getTurningDir();
        } else {
            //dir returns -1 or 1 depending on if it's positive or if it's negative
            dir = sys_limelight.getXOffset() / Math.abs(sys_limelight.getXOffset());
        }

        turning = dir * kDriveTrain.kAiming.kTargetSpeed;

        sys_drivetrain.arcadeDrive(forwardSpeed, turning);
		System.out.println("Executed");
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		sys_drivetrain.arcadeDrive(0, 0);
		sys_limelight.turnOff();
		System.out.println("Ended");
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return Math.abs(sys_limelight.getXOffset()) <= kLimelight.targetStopAngle && sys_limelight.isVisible();
	}
}
/*
 * Ignore crop
 * Write code that sorts the mode
 * Lower or Higher
 */
