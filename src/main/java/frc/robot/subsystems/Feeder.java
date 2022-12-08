package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants.kFeeder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {

    private final CANSparkMax feederMot;
    private final RelativeEncoder encoder;
    private final SparkMaxPIDController pidController;

    private boolean isFeeding = false;

    public Feeder() {
        feederMot = new CANSparkMax(kFeeder.feederID, MotorType.kBrushless);
        encoder = feederMot.getEncoder();
        pidController = feederMot.getPIDController();
        configMot();
        stopFeeding();
    }

    @Override
    public void periodic() {
        
    }

    public void configPIDF(double kP, double kI, double kD, double kF) {
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setFF(kF);
        pidController.setOutputRange(-1, 1);
    }

    @Override
    public void simulationPeriodic() {}

    public void configMot() {
        feederMot.restoreFactoryDefaults();

        feederMot.setIdleMode(IdleMode.kBrake);

        feederMot.burnFlash();
    }


    public void feed() {
        if (!isFeeding) {
            isFeeding = true;
            pidController.setReference(600, CANSparkMax.ControlType.kVelocity);
        }
    }//

    public void stopFeeding() {
        if (isFeeding) {
            isFeeding = false;
            feederMot.set(0);
        }
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }
}
