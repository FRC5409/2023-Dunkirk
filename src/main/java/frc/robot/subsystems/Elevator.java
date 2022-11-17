package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.kElevator;

public class Elevator extends SubsystemBase {

    public static CANSparkMax m_left;
    private static CANSparkMax m_right;

    private static RelativeEncoder s_encoder;

    private static DigitalInput s_magSwitch;

    private static DifferentialDrive m_elevator;

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
}