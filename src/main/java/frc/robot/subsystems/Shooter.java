package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.playingwithfusion.TimeOfFlight;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kShooter;

public class Shooter extends SubsystemBase {

    private final XboxController m_joystick;

    private final WPI_TalonFX leftMot;
    private final WPI_TalonFX rightMot;
    private final WPI_TalonFX feederMot;//TODO: Find act motor

    private final TimeOfFlight ToFFeeder;

    private double distance;

    public Shooter() {
        leftMot = new WPI_TalonFX(kShooter.leftMotID);
        rightMot = new WPI_TalonFX(kShooter.rightMotID);
        feederMot = new WPI_TalonFX(kShooter.feederID);

        ToFFeeder = new TimeOfFlight(kShooter.ToFID);

        m_joystick = new XboxController(0);

        configMots();

        distance = 10;
    }

    @Override
    public void periodic() {
        if (Math.abs(m_joystick.getRightY()) >= 0.01) {//joystick drift
            distance -= m_joystick.getRightY() * 0.1;
        }
    }

    @Override
    public void simulationPeriodic() {}

    public void configMots() {
        leftMot.setNeutralMode(NeutralMode.Brake);
        rightMot.setNeutralMode(NeutralMode.Brake);
        feederMot.setNeutralMode(NeutralMode.Brake);
    
        rightMot.follow(leftMot);//following to shoot at the same speed
        rightMot.setInverted(true);

        //not sure if I should implement current limiting
    }

    public double getVelocity() {
        return leftMot.get();//TODO: find the velocity
    }

    public void spinMotAtSpeed(double RPM) {
        
    }

    public void feed() {
        feederMot.set(kShooter.feedSpeed);
    }

    public void stopMotors() {
        feederMot.set(0);
        leftMot.set(0);
    }

    public int closestPoint() {//finds the closest point at index x
        double closest = 999999999;
        int index = -1;
        for (int i = 0; i < kShooter.kShooterData.shooterDataX.length; i++) {
            if (Math.abs(kShooter.kShooterData.shooterDataX[i] - distance) < closest) {
                closest = Math.abs(kShooter.kShooterData.shooterDataX[i] - distance);
                index = i;
            } else {// data must be in order for this part to work, if not in order remove this
                break;
            }
        }
        DriverStation.reportError(Integer.toString(index), true);
        DriverStation.reportError(Double.toString(kShooter.kShooterData.shooterDataX[index]), true);
        return index;
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

    public boolean cargo() {
        return ToFFeeder.getRange() <= kShooter.cargoIsThere;//sees if cargo is in the indexer
    }

}
