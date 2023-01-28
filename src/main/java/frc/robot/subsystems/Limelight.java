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
import frc.robot.Constants.kLimelight;

public class Limelight extends SubsystemBase {

	// Important NetworkTable values
	NetworkTable limeTable;
	NetworkTableEntry nt_xOffset, nt_yOffset, nt_targetArea, nt_visibility, nt_ledMode, nt_crop;

	// Important variables
	double angleToTarget;
	double lowTargetDist, highTargetDist;
	double turningDir = 0;

	private final XboxController m_joystick;

	// Shuffleboard Tab and Entries
	private ShuffleboardTab sb_limelight;
	private GenericEntry xOffEntry, yOffEntry, targetAreaEntry, visibilityEntry, ledModeEntry, cropEntry;


	/** Creates a new Limelight. */
	public Limelight(XboxController joystick) {

		limeTable = NetworkTableInstance.getDefault().getTable("limelight");

		NetworkTableInstance.getDefault().startServer();
    	NetworkTableInstance.getDefault().setServerTeam(5409);

		// Getting data from NetworkTables
		nt_xOffset = limeTable.getEntry("tx");
		nt_yOffset = limeTable.getEntry("ty");
		nt_targetArea = limeTable.getEntry("ta");
		nt_visibility = limeTable.getEntry("tv");
		nt_ledMode = limeTable.getEntry("ledMode");
		nt_crop = limeTable.getEntry("crop");

		// Shuffleboard stuff
		sb_limelight = Shuffleboard.getTab("Limelight");

		xOffEntry = sb_limelight.add("X Offset", nt_xOffset.getDouble(0)).getEntry();
		yOffEntry = sb_limelight.add("Y Offset", nt_yOffset.getDouble(0)).getEntry();
		targetAreaEntry = sb_limelight.add("Target Area", nt_targetArea.getDouble(-1)).getEntry();
		visibilityEntry = sb_limelight.add("Target Visibility", isVisible()).getEntry();
		ledModeEntry = sb_limelight.add("LED Mode", nt_ledMode.getDouble(-1)).getEntry();
		cropEntry = sb_limelight.add("Crop", nt_crop.getDoubleArray(new double[] {2, 2, 2, 2})).getEntry();

		m_joystick = new XboxController(0);
	}

	@Override
	public void periodic() {

		// Turning direction based on POV
		double pov = m_joystick.getPOV();

		if (pov == 270) turningDir = -1;
		else if (pov == 90) turningDir = 1;

		getTargetAngle();// Getting the angle to the target

		lowTargetDist = getDistanceToTarget(0);// Getting distance to target(s) using trigonometry
		highTargetDist = getDistanceToTarget(1);

		// Updating data on Shuffleboard
		xOffEntry.setDouble(getXOffset());
		yOffEntry.setDouble(getYOffset());
		targetAreaEntry.setDouble(nt_targetArea.getDouble(0.0));
		ledModeEntry.setDouble(nt_ledMode.getDouble(0.0));
		visibilityEntry.setBoolean(isVisible());
		cropEntry.setDoubleArray(getCrop());

	}

	/** Turns the limelight off */
	public void turnOff() {nt_ledMode.setNumber(1);}

	/** Turns the limelight on */
	public void turnOn() {nt_ledMode.setNumber(3);}

	/** Gets data from an entry */
	public double getData(String key) {return limeTable.getEntry(key).getDouble(0);}

	/** Gets the X position/offset */
	public double getXOffset() {return nt_xOffset.getDouble(0);}

	/** Gets the Y position/offset */
	public double getYOffset() {return nt_yOffset.getDouble(0);}

	/** Gets the angle to the target */
	public double getTargetAngle() {return (kLimelight.mountAngle + getYOffset()) * (Math.PI / 180);}

	/** Gets the turning direction */
	public double getTurningDir() {return turningDir;}

	/** Gets the crop rectangle coordinates */
	public double[] getCrop() {return nt_crop.getDoubleArray(new double[] {0, 0, 0, 0});}

	/** Gets the distance from the target 
	 * 
	 * @param level The level of the target (0: Middle node & 1: Upper node)
	*/
	public double getDistanceToTarget(int level) {
		if (level == 0) return (kLimelight.lowTargetHeight - kLimelight.heightOffFloor) / Math.tan(angleToTarget);
		else if (level == 1) return (kLimelight.highTargetHeight - kLimelight.heightOffFloor) / Math.tan(angleToTarget);
		else return -1;
	}

	/** Checks if the target is visible or not */
	public boolean isVisible() {return nt_visibility.getDouble(0) == 1;}

	/** Sets data in an entry */
	public void setData(String key, double data) {limeTable.getEntry(key).setDouble(data);}

	/** Sets the turning direction */
	public void setTurningDir(double dir) {turningDir = dir;}

	/** Sets the crop rectangle */
	public void setCrop(double xMin, double xMax, double yMin, double yMax) {
		nt_crop.setDoubleArray(new double[] {xMin, xMax, yMin, yMax});
	}

	/** Crops the area for whichever level required and checks if a target is visible or not */
	public int pickTarget() {
		setCrop(
		kLimelight.kCrop.kUpperHalf.minX,
		kLimelight.kCrop.kUpperHalf.maxX,
		kLimelight.kCrop.kUpperHalf.minY,
		kLimelight.kCrop.kUpperHalf.maxY
		);

		if (isVisible()) return 1;

		setCrop(
		kLimelight.kCrop.kLowerHalf.minX,
		kLimelight.kCrop.kLowerHalf.maxX,
		kLimelight.kCrop.kLowerHalf.minY,
		kLimelight.kCrop.kLowerHalf.maxY
		);
		
		if (isVisible()) return 0;

		else return -1;
	}
}
