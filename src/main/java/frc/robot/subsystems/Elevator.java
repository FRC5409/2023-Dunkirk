package frc.robot.subsystems;

import java.util.HashMap;

import io.github.oblarg.oblog.Loggable;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kElevator;


public class Elevator extends SubsystemBase implements Loggable{

    public CANSparkMax m_left;
    public CANSparkMax m_right;

    private RelativeEncoder s_encoder;

    private final DigitalInput s_magSwitch;

    public  SparkMaxPIDController c_pidController;

    private static boolean configEnabled = false;

    public static HashMap<String, NetworkTableEntry> shuffleboardFields;

    private static double kP;
    private static double kI;
    private static double kD;
    private static double kF;


    
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
        c_pidController.setOutputRange(-1, 1);

        try {
            kP = c_pidController.getP();
            kI = c_pidController.getI();
            kD = c_pidController.getD();
            kF = c_pidController.getFF();
        } catch(Exception e) {
            kP = 0;
            kI = 0;
            kD = 0;
            kF = 0;
        }
        setPIDFValues();
        m_left.burnFlash();

        shuffleboardFields = new HashMap<String, NetworkTableEntry>();

        ShuffleboardLayout pidLayout = Shuffleboard.getTab("Elevator").getLayout("PID Config", BuiltInLayouts.kList).withSize(5, 5);
        
        ShuffleboardLayout encoder = Shuffleboard.getTab("Elevator").getLayout("Encoder Values", BuiltInLayouts.kList);
        

        shuffleboardFields.put("Enable Config", pidLayout.add("Enable Configuration", false).withWidget(BuiltInWidgets.kToggleButton).withPosition(1, 1).getEntry());
    
        shuffleboardFields.put("kPConfig", pidLayout.add("Proportional Gain Config", c_pidController.getP()).withWidget(BuiltInWidgets.kTextView).withPosition(1, 2).getEntry());
            shuffleboardFields.put("kIConfig", pidLayout.add("Integral Gain Config", c_pidController.getI()).withWidget(BuiltInWidgets.kTextView).withPosition(1, 3).getEntry());
            shuffleboardFields.put("kDConfig", pidLayout.add("Derivative Gain Config", c_pidController.getD()).withWidget(BuiltInWidgets.kTextView).withPosition(1, 4).getEntry());
            shuffleboardFields.put("kFConfig", pidLayout.add("Feedforward Gain Config", c_pidController.getFF()).withWidget(BuiltInWidgets.kTextView).withPosition(1, 5).getEntry());

        shuffleboardFields.put("EncoderDistance", encoder.add("Encoder Distance Travelled", getPosition()).withWidget(BuiltInWidgets.kEncoder).getEntry());

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (shuffleboardFields.get("Enable Config").getBoolean(false) == true && configEnabled == false) {
            configEnabled = true;
        }

        if (shuffleboardFields.get("Enable Config").getBoolean(true) == false && configEnabled == true) {
            configEnabled = false;
        }

        if (configEnabled == true) {

            if (shuffleboardFields.get("kPConfig").getDouble(kP) != kP) {
                kP = shuffleboardFields.get("kPConfig").getDouble(kP);
                c_pidController.setP(kP);
            }
            if (shuffleboardFields.get("kIConfig").getDouble(kI) != kI) {
                kI = shuffleboardFields.get("kIConfig").getDouble(kI);
                c_pidController.setI(kI);
            }
            if (shuffleboardFields.get("kDConfig").getDouble(kD) != kD) {
                kD = shuffleboardFields.get("kDConfig").getDouble(kD);
                c_pidController.setD(kD);
            }
            if (shuffleboardFields.get("kFConfig").getDouble(kF) != kF) {
                kF = shuffleboardFields.get("kFConfig").getDouble(kF);
                c_pidController.setFF(kF);
            }
        }

        shuffleboardFields.get("Encoder").setNumber(getPosition());
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

    public void setPIDFValues() {
        c_pidController.setP(kP);
        c_pidController.setD(kI);
        c_pidController.setI(kD);
        c_pidController.setFF(kF);
    }
    
    public void moveElevator(double setpoint) {
        c_pidController.setReference(setpoint, ControlType.kPosition);
    }
}