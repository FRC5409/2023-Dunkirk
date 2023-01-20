// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight2;

public class TargetAim extends CommandBase {
	/** Creates a new LimelightDriveSweep. */

	private final Limelight2 sys_limelight;
	private final DriveTrain sys_drivetrain;
	private final XboxController m_joystick;

	public TargetAim(Limelight2 limelight, DriveTrain drivetrain, XboxController joystick) {

		sys_limelight = limelight;
		sys_drivetrain = drivetrain;
		m_joystick = joystick;

		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(sys_drivetrain);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		sys_limelight.turnOnLimelight();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		double xOff = sys_limelight.getXOffset();
		double forwardSpeed = m_joystick.getRightTriggerAxis() - m_joystick.getLeftTriggerAxis();
		if (sys_limelight.isVisible()) {
			if (Math.abs(xOff) >= Constants.kDriveTrain.kAiming.kTargetPlay) {
				sys_drivetrain.arcadeDrive(forwardSpeed, (xOff / Math.abs(xOff)) * Constants.kDriveTrain.kAiming.kTargetSpeed);
			}
		} else {
			sys_drivetrain.arcadeDrive(forwardSpeed, sys_limelight.getTurningDir() * Constants.kDriveTrain.kAiming.kScanningSpeed);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		sys_drivetrain.arcadeDrive(0, 0);
		sys_limelight.turnOffLimelight();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
