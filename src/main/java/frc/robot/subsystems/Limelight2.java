// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.kLimelight;

public class Limelight2 extends SubsystemBase {

	// Important NetworkTable values
	NetworkTable table;
	NetworkTableEntry tx, ty, ta, tv, ledMode;

	// Important variables
	double angleGoal;
	double distanceToTarget;
	double turningDir = 0;

	double x, y, targetArea, LEDMode;

	// Shuffleboard Tab and Entries
	private ShuffleboardTab sb_limelight;
	private GenericEntry xOffEntry, yOffEntry, targetAreaEntry, VisibilityEntry, ledModeEntry;

	private final XboxController m_joystick;


	/** Creates a new Limelight2. */
	public Limelight2(XboxController joystick) {

		table = NetworkTableInstance.getDefault().getTable("limelight");

		NetworkTableInstance.getDefault().startServer();
    	NetworkTableInstance.getDefault().setServerTeam(5409);

		// Getting data from the NetworkTables
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

		m_joystick = new XboxController(0);
	}

	@Override
	public void periodic() {

		double pov = m_joystick.getPOV();

		if (pov == 270) {
			turningDir = -1;
		} else if (pov == 90) {
			turningDir = 1;
		}

		// Reading important values periodically
		x = tx.getDouble(0.0);
		y = ty.getDouble(0.0);
		targetArea = ta.getDouble(-1);
		LEDMode = ledMode.getDouble(-1);

		// Getting the angle to the goal in radians (tan requires radians to work)
		angleGoal = (kLimelight.mountAngle + y) * (Math.PI / 180);
		// Getting distance to target using trig
		distanceToTarget = (kLimelight.targetHeight - kLimelight.heightOffFloor) / Math.tan(angleGoal);

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

	/** Gets the X position/offset */
	public double getXOffset() {return x;}

	/** Gets the Y position/offset */
	public double getYOffset() {return y;}

	/** Checks if the target is visibile or not */
	public boolean isVisible() {return tv.getDouble(0) == 1;}

	/** Gets the distance from the target */
	public double getTargetDistance() {return distanceToTarget;}

	/** Gets the turning direction */
	public double getTurningDir() {return turningDir;}

	/** Sets the turning direction */
	public void setTurningDir(double dir) {turningDir = dir;}

	/** Gets the angle to the target */
	public double getTargetAngle() {return angleGoal;}

	public void setPipeline() {;}
}
