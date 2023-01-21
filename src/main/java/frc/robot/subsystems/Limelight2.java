// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Limelight2 extends SubsystemBase {

	// Important NetworkTable values
	NetworkTable table;
	NetworkTableEntry tx, ty, ta, tv;
	NetworkTableEntry ledMode;

	// Important variables
	double angleGoal;
	double distanceToTarget;
	double turningDir;
	double x, y;
	double targetArea;
	double LEDMode;

	// Shuffleboard Tab and Entries
	private ShuffleboardTab sb_limelight;
	private ShuffleboardLayout limelightLayout;
	private GenericEntry xOffEntry, yOffEntry, targetAreaEntry, VisibilityEntry, ledModeEntry;


	/** Creates a new Limelight2. */
	public Limelight2() {

		NetworkTableInstance.getDefault().startServer();
    	NetworkTableInstance.getDefault().setServerTeam(5409);

		// Getting data from the NetworkTables
		table = NetworkTableInstance.getDefault().getTable("limelight");
		tx = table.getEntry("tx");
		ty = table.getEntry("ty");
		ta = table.getEntry("ta");
		tv = table.getEntry("tv");
		ledMode = table.getEntry("ledMode");

		// Shuffleboard stuff
		sb_limelight = Shuffleboard.getTab("limelight");
		//sb_limelight.add("Limelight Data", limelightLayout.addNumber(getName(), null).withWidget(BuiltInWidgets.kTextView));
		xOffEntry = sb_limelight.add("X Offset", x).getEntry();
		yOffEntry = sb_limelight.add("Y Offset", y).getEntry();
		targetAreaEntry = sb_limelight.add("Target Area", targetArea).getEntry();
		VisibilityEntry = sb_limelight.add("Target Visibility", isVisible()).getEntry();
		ledModeEntry = sb_limelight.add("LED Mode", LEDMode).getEntry();

		distanceToTarget = -1;
	}

	@Override
	public void periodic() {

		// Reading important values periodically
		x = tx.getDouble(0.0);
		y = ty.getDouble(0.0);
		targetArea = ta.getDouble(-1);
		LEDMode = ledMode.getDouble(-1);

		// Getting the angle to the goal in radians (tan requires radians to work)
		angleGoal = (Constants.kLimelight.mountAngle + y) * (Math.PI / 180);
		// Getting distance to target using trig
		distanceToTarget = (Constants.kLimelight.targetHeight - Constants.kLimelight.heightOffFloor) / Math.tan(angleGoal);

		// Updating data on Shuffleboard
		xOffEntry.setDouble(x);
		yOffEntry.setDouble(y);
		targetAreaEntry.setDouble(targetArea);
		ledModeEntry.setDouble(LEDMode);
		VisibilityEntry.setBoolean(isVisible());
	}

	/** Turns the limelight off */
	public void turnOff() {ledMode.setNumber(1);}

	/** Turns the limelight on */
	public void turnOn() {ledMode.setNumber(3);}

	/** Gets the x position/offset */
	public double getXOffset() {return x;}

	/** Gets the y position/offset */
	public double getYOffset() {return y;}

	/** Checks if the target is visibile or not */
	public boolean isVisible() {
        try {
            return tv.getDouble(0) == 1;
        } catch (Exception e) { 
            return false;
        }
    }

	/** Gets the distance from the target */
	public double getTargetDistance() {return distanceToTarget;}

	/** Gets the turning direction */
	public double getTurningDir() {return turningDir;}

	/** Sets the turning direction */
	public void setTurningDir(double dir) {turningDir = dir;}

	/** Gets the angle to the target */
	public double getTargetAngle() {return angleGoal;}
}
