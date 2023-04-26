package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants.kFeeder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {

    private final CANSparkMax feederMot;

    public Feeder() {
        feederMot = new CANSparkMax(kFeeder.feederID, MotorType.kBrushless);

        configMot();
    }

    @Override
    public void periodic() {}

    @Override
    public void simulationPeriodic() {}

    public void configMot() {
        feederMot.restoreFactoryDefaults();

        feederMot.setIdleMode(IdleMode.kBrake);

        feederMot.burnFlash();
    }


    public void feed() {
        feederMot.set(kFeeder.feedSpeed);
    }

    public void stopFeeding() {
        feederMot.set(0);
    }
}
