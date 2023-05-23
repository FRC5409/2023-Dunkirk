package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants.kConfig;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;

public class Turret extends ProfiledPIDSubsystem {

    public enum ScanningDirection {
        kLeft(-1), kRight(1), kNotSelected(kTurret.defaultScanDir.value);

        ScanningDirection(int value) {
            this.value = value;
        }

        public final int value;
    }

    private final CANSparkMax turretMot;

    private State currentState           = State.kOff;
    private ScanningDirection scanDir    = ScanningDirection.kNotSelected;

    private double offset;

    private double maxSpeed = kTurret.maxVolts;

    private ShuffleboardTab  turretTab;
    private GenericEntry     encoderPosEntry;
    private GenericEntry     angleEntry;
    private GenericEntry     stateEntry;

    private final boolean debug = false;

    public Turret() {
        super(
            new ProfiledPIDController(
                kTurret.kP,
                kTurret.kI,
                kTurret.kD,
                new TrapezoidProfile.Constraints(
                    kTurret.maxSpeed,
                    kTurret.maxScanningAccel
                    )
                )
            );
        turretMot = new CANSparkMax(kTurret.CANID, MotorType.kBrushless);
        getController().setTolerance(kTurret.encoderThreshold);

        turretMot.restoreFactoryDefaults();

        zeroEncoder();

        turretMot.setIdleMode(IdleMode.kBrake);

        turretMot.setSmartCurrentLimit(kTurret.currentLimit);

        turretMot.burnFlash();

        offset = 0;

        if (debug || kConfig.masterDebug) {
            turretTab        = Shuffleboard.getTab("Turret");
            encoderPosEntry  = turretTab.add("Position", getPosition()).getEntry();
            angleEntry       = turretTab.add("Angle", getAngle()).getEntry();
            stateEntry       = turretTab.add("State", 0).getEntry();
        }
    }

    /**
     * set the PID of the PID controller
     * @param kP p
     * @param kI i
     * @param kD d
     */
    public void setPID(double kP, double kI, double kD) {
        m_controller.setP(kP);
        m_controller.setI(kI);
        m_controller.setD(kD);
    }

    /**
     * Sets the voltage of the turret
     * @param volts volts you want to give
     */
    public void setVolts(double volts) {
        turretMot.setVoltage(volts);
    }

    /**
     * Gets the volts being applied to the motor
     * @return volts
     */
    public double getVolts() {
        return turretMot.getBusVoltage();
    }

    @Override
    public void useOutput(double output, TrapezoidProfile.State setpoint) {
        output *= 12;
        
        if (output > maxSpeed)
            output = maxSpeed;
        if (output < -maxSpeed) 
            output = -maxSpeed;

        setVolts(output);
    }

    @Override
    public double getMeasurement() {
        return getPosition();
    }

    /**
     * Sets the max speed the turret can be applied to
     * @param volts Max volts that be applied at once
     */
    public void setMaxSpeed(double volts) {
        maxSpeed = volts;
    }


    /**
     * Stops the motor
     */
    public void stopMot() {
        turretMot.set(0);
    }
    
    /**
     * Goes to a specified location
     * @param pos the position you want to go to
     */
    public void setRefrence(double pos) {
        //safety check
        if (pos > kTurret.maxPosition)
            pos = kTurret.maxPosition;

        if (pos < -kTurret.maxPosition)
            pos = -kTurret.maxPosition;

        // turretMot.getPIDController().setReference(encoderPos, ControlType.kPosition);
        setGoal(pos);

        if (!isEnabled())
            enable();
    }


    /**
     * Gets the current encoder position
     * @return encoder position
     */
    public double getPosition() {
        return turretMot.getEncoder().getPosition();
    }

    /**
     * Zeros the encoder
     */
    public void zeroEncoder() {
        turretMot.getEncoder().setPosition(0);
    }

    /**
     * Gets the angle of the current turret position
     * @return angle in degrees
     */
    public double getAngle() {
        return convertToEncoder(getPosition());
    }

    /**
     * Converts back to the encoder units
     * @param input angle in degrees
     * @return Encoder unit
     */
    public double convertToEncoder(double input) {
        return input * kTurret.angleToPosition;
    }

    /**
     * Sets the turret state
     * @param State kOff, kScanning, kLocking, kLocked
     */
    public void setState(State State) {
        currentState = State;
    }

    /**
     * Gets the current turrent state
     * @return kOff, kScanning, kLocking, kLocked
     */
    public State getState() {
        return currentState;
    }

    /**
     * Getting the state value, this is for shuffleboard debuging
     * @return The current state value
     */
    public int getStateNumber() {
        return currentState.value;
    }


    /**
     * Gets if it's on or off
     * @return true if it's being used by something
     */
    public boolean isBeingUsed() {
        return getState() != State.kOff;
    }

    public boolean isTargetting() {
        return getState() == State.kLocked || getState() == State.kLocking;
    }

    /**
     * Set which way should be scanned
     * @param dir kLeft, kRight
     */
    public void setScanningDir(ScanningDirection dir) {
        scanDir = dir;
    }

    /**
     * Get the current perfered scanning direction
     * @return kLeft, kRight
     */
    public ScanningDirection getScanningDir() {
        return scanDir;
    }

    /**
     * Sets the neutral mode of the turret
     * @param mode kBreak, kCoast
     */
    public void setIdleMode(IdleMode mode) {
        turretMot.setIdleMode(mode);
    }

    /**
     * Sets the offset of the turret
     * @param offset offset in position
     */
    public void setTurretOffset(double offset) {
        this.offset = offset;
    }

    /**
     * Gets the applied turret offset
     * @return offset in position
     */
    public double getTurretOffset() {
        return offset;
    }

    /**
     * Configers the max acceleration applied to the motor
     * @param accel in units per second squared
     */
    public void configAccel(double accel) {
        getController().setConstraints(
            new TrapezoidProfile.Constraints(
                kTurret.maxSpeed,
                accel
            )
        );
    }

    @Override
    public void periodic() {
        super.periodic();
        // This method will be called once per scheduler run
        if (debug || kConfig.masterDebug) {
            encoderPosEntry.setDouble(getPosition());
            angleEntry.setDouble(getAngle());
            stateEntry.setInteger(getStateNumber());
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

}
