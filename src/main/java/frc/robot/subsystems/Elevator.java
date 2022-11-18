package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kElevator;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class Elevator extends SubsystemBase implements Loggable {

    public static CANSparkMax m_left;
    private static CANSparkMax m_right;

    private static RelativeEncoder s_encoder;

    private static DigitalInput s_magSwitch;


    private static double kP;
    private static double kI;
    private static double kD;
    private static double kF;

    public static SparkMaxPIDController c_pidController;


    public Elevator() {
        m_left = new CANSparkMax(kElevator.kLeftCAN, MotorType.kBrushless);
            m_left.setInverted(false);
            m_left.setIdleMode(IdleMode.kBrake);

        m_right = new CANSparkMax(kElevator.kRightCAN, MotorType.kBrushless);
            m_right.follow(m_left, true);
            m_right.setIdleMode(IdleMode.kBrake);
        
        s_encoder = m_left.getEncoder();
        zeroEncoder();

        s_magSwitch = new DigitalInput(kElevator.kMagSwitchDIO);

        c_pidController = m_left.getPIDController();
        }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        
        if (detectLimSwitch()) {
            zeroEncoder();
        }

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        
    }

    
    public void zeroEncoder() {
        s_encoder.setPosition(0);
    }
    
    public double getPosition() {
        return s_encoder.getPosition();
    }
    
    public boolean detectLimSwitch() {
        return !s_magSwitch.get();
    }

    public void setPID() {
        c_pidController.setP(kP);
        c_pidController.setI(kI);
        c_pidController.setD(kD);
        c_pidController.setFF(kF);
    }

    @Config
    public void PIDConfigField(double kP, double kI, double kD, double kF) {
        Elevator.kP = kP;
        Elevator.kI = kI;
        Elevator.kD = kD;
        Elevator.kF = kF;
    
        setPID();
    }
}