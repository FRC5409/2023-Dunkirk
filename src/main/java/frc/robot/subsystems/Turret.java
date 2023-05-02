package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kConfig;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;

public class Turret extends SubsystemBase {

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

    private double pos;

    private ShuffleboardTab  turretTab;
    private GenericEntry     encoderPosEntry;
    private GenericEntry     angleEntry;


    private final boolean debug = false;

    public Turret() {
        turretMot = new CANSparkMax(kTurret.CANID, MotorType.kBrushless);

        turretMot.restoreFactoryDefaults();

        turretMot.setIdleMode(IdleMode.kBrake);
        turretMot.setSmartCurrentLimit(kTurret.currentLimit);
        //TODO: make sure left is negative and right is positive

        turretMot.burnFlash();

        turretMot.getEncoder().setPosition(0);

        pos = 0;

        setPID(kTurret.kP, kTurret.kI, kTurret.kD);

        if (debug || kConfig.masterDebug) {
            turretTab        = Shuffleboard.getTab("Turret");
            encoderPosEntry  = turretTab.add("Position", getPosition()).getEntry();
            angleEntry       = turretTab.add("Angle", getAngle()).getEntry();
        }
    }

    /**
     * set the PID of the PID controller
     * @param kP p
     * @param kI i
     * @param kD d
     */
    public void setPID(double kP, double kI, double kD) {
        turretMot.getPIDController().setP(kP);
        turretMot.getPIDController().setI(kI);
        turretMot.getPIDController().setD(kD);
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

        this.pos = pos;

        turretMot.getPIDController().setReference(pos, ControlType.kPosition);
    }

    public boolean atSetpoint() {
        return Math.abs(pos - getPosition()) <= kTurret.encoderThreshold;
    }


    /**
     * Gets the current encoder position
     * @return encoder position
     */
    public double getPosition() {
        return turretMot.getEncoder().getPosition();
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
        return input * (kTurret.maxPosition / kTurret.maxAngle);
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
     * Gets if it's on of off
     * @return true if it's being used by something
     */
    public boolean isBeingUsed() {
        return getState() != State.kOff;
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

    public void setNeutralMode(IdleMode mode) {
        turretMot.setIdleMode(mode);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (debug || kConfig.masterDebug) {
            encoderPosEntry.setDouble(getPosition());
            angleEntry.setDouble(getAngle());
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

}
