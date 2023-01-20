// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Limelight2 extends SubsystemBase {
	/** Creates a new Limelight2. */

	// Important NetworkTable values
	NetworkTable table;
	NetworkTableEntry tx;
	NetworkTableEntry ty;
	NetworkTableEntry ta;
	NetworkTableEntry tv;
	NetworkTableEntry ledMode;

	// Important variables
	double angleGoal;
	double distanceToTarget;
	double turningDir;

	double x;
	double y;
	double targetArea;
	double LEDMode;

	private ShuffleboardTab sb_limelight;


	public Limelight2() {

		NetworkTableInstance.getDefault().startServer();
    	NetworkTableInstance.getDefault().setServerTeam(5409);

		table = NetworkTableInstance.getDefault().getTable("limelight");
		tx = table.getEntry("tx");
		ty = table.getEntry("ty");
		ta = table.getEntry("ta");
		tv = table.getEntry("tv");
		ledMode = table.getEntry("ledMode");

		sb_limelight = Shuffleboard.getTab("limelight");

		distanceToTarget = -1;
	}

	@Override
	public void periodic() {

		// Reading important values periodically
		x = tx.getDouble(0.0);
		y = ty.getDouble(0.0);
		targetArea = ta.getDouble(-1);
		LEDMode = ledMode.getDouble(-1);

		angleGoal = (Constants.kLimelight.mountAngle + y) * (Math.PI / 180);
		distanceToTarget = (Constants.kLimelight.targetHeight - Constants.kLimelight.heightOffFloor) / Math.tan(angleGoal);

		// Putting them on SmartDashboard
		SmartDashboard.putNumber("LED Mode", LEDMode);
		SmartDashboard.putNumber("LimelightXOffset", x);
		SmartDashboard.putNumber("LimelightYOffset", y);
		SmartDashboard.putNumber("LimelightTargetArea", targetArea);
		SmartDashboard.putBoolean("is Visible", isVisible());

	}

	public void turnOffLimelight() {
		ledMode.setNumber(1);
	}

	public void turnOnLimelight() {
		ledMode.setNumber(3);
	}

	public double getXOffset() {
		return x;
	}

	public double getYOffset() {
		return y;
	}

	public boolean isVisible() {
		return tv.getDouble(0) == 1;
	}

	public double getTargetDistance() {
		return distanceToTarget;
	}

	public double getTurningDir() {
		return turningDir;
	}

	public double getTargetAngle() {
		return angleGoal;
	}
}