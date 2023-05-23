package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants.kConfig;
import frc.robot.Constants.kFeeder;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {

    private final CANSparkMax       feederMot;
    private final RelativeEncoder   feederEnc;

    private ShuffleboardTab         feederTab;
    private GenericEntry            feederVelocityEntry;

    private boolean force = false;
    private double forceSpeed = 0;

    private final boolean debug = false;

    public Feeder() {
        feederMot = new CANSparkMax(kFeeder.feederID, MotorType.kBrushless);

        feederMot.restoreFactoryDefaults();

        feederMot.setIdleMode(IdleMode.kBrake);

        feederMot.setSmartCurrentLimit(kFeeder.currentLimit);

        feederMot.burnFlash();

        feederEnc = feederMot.getEncoder();

        if (debug || kConfig.masterDebug) {
            feederTab = Shuffleboard.getTab("Feeder");
            feederVelocityEntry = feederTab.add("Velocity", getEncoderVelocity()).getEntry();
        }
    }

    @Override
    public void periodic() {
        if (debug || kConfig.masterDebug) {
            feederVelocityEntry.setDouble(getEncoderVelocity());
        }
    }

    @Override
    public void simulationPeriodic() {}


    /**
     * Starts feeding the cargo to the shooter
     */
    public void feed() {
        // feederMot.getPIDController().setReference(kFeeder.feedSpeed, ControlType.kVelocity);
        if (force) {
            forceSpeed = kFeeder.feedSpeed;
        } else {
            feederMot.set(kFeeder.feedSpeed);
        }
    }

    public void revsere() {
        if (force) {
            forceSpeed = kFeeder.reverseSpeed;
        } else {
            feederMot.set(kFeeder.reverseSpeed);
        }
    }

    /**
     * Stops the motor completly
     */
    public void stopMotor() {
        if (force) {
            forceSpeed = 0;
        } else {
            feederMot.set(0);
        }
    }

    /**
     * Gets the velocity of the feeder motor
     */
    public double getEncoderVelocity() {
        return feederEnc.getVelocity();
    }

    public void forceFeed(double speed) {
        feederMot.set(speed);
        force = true;
    }

    public void stopForceSpeed() {
        force = false;
        feederMot.set(forceSpeed);
    }

}
