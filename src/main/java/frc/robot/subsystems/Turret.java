package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.scanningDirection;
import frc.robot.Constants.kTurret.state;

public class Turret extends SubsystemBase {

    private final CANSparkMax turretMot;

    private state currentState = state.kOff;
    private scanningDirection scanDir = scanningDirection.kLeft;

    private final boolean debug = true;

    private ShuffleboardTab turretTab;
    private GenericEntry encoderPosEntry;

    public Turret() {
        turretMot = new CANSparkMax(kTurret.CANID, MotorType.kBrushless);

        turretMot.restoreFactoryDefaults();
        turretMot.setIdleMode(IdleMode.kBrake);
        turretMot.setSmartCurrentLimit(kTurret.currentLimit);
        turretMot.burnFlash();

        turretMot.getEncoder().setPosition(0);

        setPID(kTurret.kP, kTurret.kI, kTurret.kD);

        if (debug) {
            turretTab = Shuffleboard.getTab("Turret");
            encoderPosEntry = turretTab.add("Position", getPosition()).getEntry();
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
        turretMot.getPIDController().setReference(pos, ControlType.kPosition);
    }


    /**
     * Gets the current encoder position
     * @return encoder position
     */
    public double getPosition() {
        return turretMot.getEncoder().getPosition();
    }

    /**
     * Sets the turret state
     * @param State kOff, kScanning, kLocking, kLocked
     */
    public void setState(state State) {
        currentState = State;
    }

    /**
     * Gets the current turrent state
     * @return kOff, kScanning, kLocking, kLocked
     */
    public state getState() {
        return currentState;
    }


    /**
     * Gets if it's on of off
     * @return true if it's being used by something
     */
    public boolean isBeingUsed() {
        return getState() != state.kOff;
    }

    /**
     * Set which way should be scanned
     * @param dir kLeft, kRight
     */
    public void setScanningDir(scanningDirection dir) {
        scanDir = dir;
    }

    /**
     * Get the current perfered scanning direction
     * @return kLeft, kRight
     */
    public scanningDirection getScanningDir() {
        return scanDir;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (debug) {
            encoderPosEntry.setDouble(getPosition());
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

}
