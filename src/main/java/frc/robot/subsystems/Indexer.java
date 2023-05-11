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
        indexerMot.set(kIndexer.indexerSpeed);
    }

    /**
     * Stop intaking cargo
     */
    public void stopIntaking() {
        indexerMot.set(0);
    }

    /**
     * Reverses the indexer motor
     */
    public void reverseIntake() {
        indexerMot.set(-kIndexer.reversingSpeed);
    }

    /**
     * Gets the velocity of the indexer
     * @return The encoder velocity
     */
    public double getVelocity() {
        return indexerEnc.getVelocity();
    }

}
