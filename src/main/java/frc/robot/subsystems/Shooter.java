package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kShooter;

public class Shooter extends SubsystemBase {

    private final XboxController m_joystick;

    private final WPI_TalonFX leftMot;
    private final WPI_TalonFX rightMot;

    // private final TimeOfFlight ToFFeeder;

    private double distance;

    private boolean isFeeding = false;

    public Shooter() {
        leftMot = new WPI_TalonFX(kShooter.leftMotID);
        rightMot = new WPI_TalonFX(kShooter.rightMotID); 

        // ToFFeeder = new TimeOfFlight(kShooter.ToFID);

        m_joystick = new XboxController(0);

        configMots();

        distance = 10;

        stopMotors();
    }

    @Override
    public void periodic() {
        if (Math.abs(m_joystick.getRightY()) >= 0.01) {//joystick drift
            distance -= m_joystick.getRightY() * 0.3;//changed the distance for teseting
        }
    }

    @Override
    public void simulationPeriodic() {}

    public void configMots() {
        leftMot.configFactoryDefault();
        rightMot.configFactoryDefault();

        leftMot.setNeutralMode(NeutralMode.Coast);
        rightMot.setNeutralMode(NeutralMode.Coast);
    
        rightMot.follow(leftMot);//following to shoot at the same speed
        rightMot.setInverted(true);

        setPIDFvalues(kShooter.kPID.kP, kShooter.kPID.kI, kShooter.kPID.kD, kShooter.kPID.kF);
    }

    public void setPIDFvalues(double p, double i, double d, double f) {
        leftMot.config_kP(0, p);
        leftMot.config_kI(0, i);
        leftMot.config_kD(0, d);
        leftMot.config_kF(0, f);
    }

    public double getVelocity() {
        return getAverageSpeed();
    }

    public double getAverageSpeed() {
        return (Math.abs(leftMot.getSelectedSensorVelocity()) + Math.abs(rightMot.getSelectedSensorVelocity())) / 2 / 2048.0 * 600;//convers to RPM
    }

    public void spinMotAtSpeed(double RPM) {
        leftMot.set(TalonFXControlMode.Velocity, RPM * 2048.0 / 600.0);//spins at RPM
    }

    public void feed() {
        if (!isFeeding) {
            isFeeding = true;
        }
    }

    public void stopFeeding() {
        if (isFeeding) {
            isFeeding = false;
        }
    }

    public void stopMotors() {
        leftMot.set(0);
        isFeeding = false;
    }

    public int closestPoint() {//finds the closest point at index x
        return (int) Math.round(distance / 15) - 1;//going up by 15 inches per step
    }

    public double getInterpolatedSpeed(double x1, double y1, double x2, double y2, double x) {//gets the new interpolated speed
        // Y = ( ( X - X1 )( Y2 - Y1) / ( X2 - X1) ) + Y1
        // Y: finding interpolated Y value
        // X: Target X cordinant
        // X1, Y1: first point
        // X2, Y2: second point

        return ((x - x1) * (y2 - y1) / (x2 - x1)) + y1;
    }

    public double getTargetDistance() {
        return distance;
    }

    public void setVolts(double volts) {
        leftMot.setVoltage(volts);
    }

}
