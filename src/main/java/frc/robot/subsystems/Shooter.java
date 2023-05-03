package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kConfig;
import frc.robot.Constants.kShooter;

public class Shooter extends SubsystemBase {

    private final WPI_TalonFX leftMot;
    private final WPI_TalonFX rightMot;

    private double currentRPM;

    private ShuffleboardTab  shooterTab;
    private GenericEntry     speedEntry;
    private GenericEntry     desiredSpeedEntry;

    private ShuffleboardTab  trainingTab;
    private GenericEntry     shooterSpeedEntry;

    private final boolean debug = false;

    public Shooter() {
        leftMot = new WPI_TalonFX(kShooter.leftMotID);
        rightMot = new WPI_TalonFX(kShooter.rightMotID); 

        configMots();

        currentRPM = 0;

        if (debug || kConfig.masterDebug) {
            shooterTab           = Shuffleboard.getTab("Shooter");
            speedEntry           = shooterTab.add("Speed", 0).getEntry();
            desiredSpeedEntry    = shooterTab.add("Desired Speed", 0).getEntry();
        }
        
        if (kConfig.training) {
            trainingTab          = Shuffleboard.getTab("Training");
            shooterSpeedEntry    = trainingTab.add("Shooter Speed", 1000).getEntry();
        }
    }

    @Override
    public void periodic() {
        if (debug || kConfig.masterDebug) {
            speedEntry.setDouble(getAverageSpeed());
            desiredSpeedEntry.setDouble(currentRPM);
        }
    }

    @Override
    public void simulationPeriodic() {}

    public void configMots() {
        leftMot.configFactoryDefault();
        rightMot.configFactoryDefault();

        leftMot.setNeutralMode(NeutralMode.Coast);
        rightMot.setNeutralMode(NeutralMode.Coast);

        SupplyCurrentLimitConfiguration currentLimit = new SupplyCurrentLimitConfiguration();
        currentLimit.enable = true;
        currentLimit.currentLimit = kShooter.currentLimit;

        leftMot.configSupplyCurrentLimit(currentLimit);
        rightMot.configSupplyCurrentLimit(currentLimit);
    
        rightMot.follow(leftMot);//following to shoot at the same speed
        rightMot.setInverted(true);

        setPIDFvalues(kShooter.kPID.kP, kShooter.kPID.kI, kShooter.kPID.kD, kShooter.kPID.kF);
    }

    /**
     * Sets the PIDF values of the PID controller
     * @param p
     * @param i
     * @param d
     * @param f
     */
    public void setPIDFvalues(double p, double i, double d, double f) {
        leftMot.config_kP(0, p);
        leftMot.config_kI(0, i);
        leftMot.config_kD(0, d);
        leftMot.config_kF(0, f);
    }

    /**
     * @deprecated use getAverageSpeed();
     * @return getAverageSpeed
     */
    public double getVelocity() {
        return getAverageSpeed();
    }

    /**
     * Gets the absolute average speed of the shooter in RPM
     * @return
     */
    public double getAverageSpeed() {
        return (Math.abs(leftMot.getSelectedSensorVelocity()) + Math.abs(rightMot.getSelectedSensorVelocity())) / 2 / 2048.0 * 600;//convers to RPM
    }

    /**
     * Spins the motor at a set RPM
     * @param RPM The RPM you want to spin at
     */
    public void spinMotAtSpeed(double RPM) {
        currentRPM = RPM;
        leftMot.set(TalonFXControlMode.Velocity, RPM * 2048.0 / 600.0);//spins at RPM
    }

    /**
     * Sets the volts of the shooter
     * @param volts Volts you want to give the shooter
     */
    public void setVolts(double volts) {
        currentRPM = -1;
        leftMot.setVoltage(volts);
    }

    /**
     * Stops the motor from spinning
     */
    public void stopMot() {
        currentRPM = -1;
        leftMot.set(0);
    }

    public double getTrainingSpeed() {
        return shooterSpeedEntry.getDouble(0);
    }

}
