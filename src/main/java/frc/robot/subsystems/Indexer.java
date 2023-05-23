package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kIndexer;

public class Indexer extends SubsystemBase {

    private final CANSparkMax        indexerMot;
    private final RelativeEncoder    indexerEnc;

    private boolean parallelCommands[] = {false, false};
    
    private boolean force = false;
    private double forceSpeed = 0;

    public Indexer() {
        indexerMot = new CANSparkMax(kIndexer.CANID, MotorType.kBrushless);

        indexerMot.restoreFactoryDefaults();
        
        indexerMot.setIdleMode(IdleMode.kBrake);
        indexerMot.setSmartCurrentLimit(kIndexer.currentLimit);
        indexerMot.setInverted(true);

        indexerMot.burnFlash();

        indexerEnc = indexerMot.getEncoder();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

    /**
     * Starts intaking cargo
     */
    public void startIntaking() {
        if (force) {
            forceSpeed = kIndexer.indexerSpeed;
        } else {
            indexerMot.set(kIndexer.indexerSpeed);
        }
    }

    /**
     * Starts intaking with parallel usage
     * @param index which command you are using
     */
    public void startIntaking(int index) {
        if (!parallelCommands[0] && !parallelCommands[1]) {
            startIntaking();
        }
        parallelCommands[index] = true;
    }

    /**
     * Stop intaking cargo
     */
    public void stopIntaking() {
        if (force) {
            forceSpeed = 0;
        } else {
            indexerMot.set(0);
        }
    }
    
    /**
     * Stop intaking with parallel usage
     * @param index which command you are using
     */
    public void stopIntaking(int index) {
        parallelCommands[index] = false;
        if (!parallelCommands[0] && !parallelCommands[1]) {
            stopIntaking();
        }
    }

    /**
     * Reverses the indexer motor
     */
    public void reverseIntake() {
        if (force) {
            forceSpeed = -kIndexer.reversingSpeed;
        } else {
            indexerMot.set(-kIndexer.reversingSpeed);
        }
    }

    /**
     * Gets the velocity of the indexer
     * @return The encoder velocity
     */
    public double getVelocity() {
        return indexerEnc.getVelocity();
    }

    /**
     * Sets the speed of the feeder motor
     * @param speed speed you want to give the motor
     */
    public void setSpeed(double speed) {
        if (force) {
            forceSpeed = speed;
        } else {
            indexerMot.set(speed);
        }
    }

    /**
     * Starts force speed
     */
    public void forceSpeed(double speed) {
        forceSpeed = getSpeedInput();
        setSpeed(speed);
        force = true;
    }

    /**
     * Stops force speed and goes to the last used speed change
     */
    public void stopForceSpeed() {
        force = false;
        setSpeed(forceSpeed);
    }

    /**
     * Gets the last applied speed to the motor
     * @return the last applied speed
     */
    public double getSpeedInput() {
        return indexerMot.getAppliedOutput();
    }

    /**
     * Returns if the motor is being forced to go a certain speed
     * @return true/false
     */
    public boolean isForced() {
        return force;
    }

}
