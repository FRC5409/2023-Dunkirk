// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.kDriveTrain;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight2;

public class TargetAim extends CommandBase {
	/** Creates a new TargetAim. */

	private final Limelight2 sys_limelight2;
	private final DriveTrain sys_drivetrain;
	private final CommandXboxController c_joystick;

	public TargetAim(Limelight2 limelight2, DriveTrain drivetrain, CommandXboxController joystick) {
		sys_limelight2 = limelight2;
		sys_drivetrain = drivetrain;
		c_joystick = joystick;

		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(sys_drivetrain);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		sys_limelight2.turnOn();
		// System.out.println("Initialized");
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		double xOff = sys_limelight2.getXOffset();
		double forwardSpeed = c_joystick.getRightTriggerAxis() - c_joystick.getLeftTriggerAxis();
		if (sys_limelight2.isVisible()) {
			if (Math.abs(xOff) >= kDriveTrain.kAiming.kTargetPlay) {
				sys_drivetrain.arcadeDrive(forwardSpeed, (xOff / Math.abs(xOff)) * kDriveTrain.kAiming.kTargetSpeed);
			}
		} else {
			sys_drivetrain.arcadeDrive(forwardSpeed, sys_limelight2.getTurningDir() * kDriveTrain.kAiming.kScanningSpeed);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		sys_drivetrain.arcadeDrive(0, 0);
		sys_limelight2.turnOff();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
