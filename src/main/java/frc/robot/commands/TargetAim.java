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

	double xOff, forwardSpeed;
	boolean highTarget, lowTarget;

	public TargetAim(Limelight2 limelight2, DriveTrain drivetrain, CommandXboxController joystick) {
		sys_limelight2 = limelight2;
		sys_drivetrain = drivetrain;
		c_joystick = joystick;

		xOff = sys_limelight2.getXOffset();
		forwardSpeed = c_joystick.getRightTriggerAxis() - c_joystick.getLeftTriggerAxis();

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
		/*checkForTargetInCrop(1);
		checkForTargetInCrop(2);
		
		if (highTarget) {lockOnTarget();}
		else if (!highTarget && lowTarget) {lockOnTarget();}*/
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

	/** The logic for locking onto the Target */
	private void lockOnTarget() {
		if (Math.abs(xOff) >= kDriveTrain.kAiming.kTargetPlay) {
			sys_drivetrain.arcadeDrive(forwardSpeed, (xOff / Math.abs(xOff)) * kDriveTrain.kAiming.kTargetSpeed);
		} else {
			sys_drivetrain.arcadeDrive(forwardSpeed, sys_limelight2.getTurningDir() * kDriveTrain.kAiming.kScanningSpeed);
		}
	}

	private void checkForTargetInCrop(int level) {
		
		if (level == 1) {
			sys_limelight2.setCrop(-1, 1, 0, 1);

			if (sys_limelight2.isVisible()) {highTarget = true;}
			else {highTarget = false;}
		}

		if (level == 2) {
			sys_limelight2.setCrop(-1, 1, -1, 0);
			
			if (sys_limelight2.isVisible()) {lowTarget = true;}
			else {lowTarget = false;}
		}
	}
}
