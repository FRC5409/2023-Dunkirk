package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kIndexer;

public class Indexer extends SubsystemBase {

    private final CANSparkMax indexerMot;

    public Indexer() {
        indexerMot = new CANSparkMax(kIndexer.CANID, MotorType.kBrushless);

        indexerMot.restoreFactoryDefaults();
        
        indexerMot.setIdleMode(IdleMode.kBrake);
        indexerMot.setSmartCurrentLimit(kIndexer.currentLimit);
        indexerMot.setInverted(true);

        indexerMot.burnFlash();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

    public void startIntaking() {
        indexerMot.set(kIndexer.indexerSpeed);
    }

    public void stopIntaking() {
        indexerMot.set(0);
    }

}
