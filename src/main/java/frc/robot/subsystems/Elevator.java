package frc.robot.subsystems;

import java.util.HashMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.kElevator;
import frc.robot.Constants.kPneumatics;


public class Elevator extends SubsystemBase {

    public boolean elevatorActive;

    private CANSparkMax m_left;
    private CANSparkMax m_right;
    private  SparkMaxPIDController c_pidController;

    private RelativeEncoder s_encoder;

    private final DigitalInput s_magSwitch;

    private final DoubleSolenoid ratchetLock;

    public static HashMap<String, NetworkTableEntry> shuffleboardFields;

    private boolean isMoving;

    private double prevHeldPos = 0;


    
    public Elevator() {
        m_left = new CANSparkMax(kElevator.kLeftCAN, MotorType.kBrushless);
        m_left.setInverted(false);
        m_left.setIdleMode(IdleMode.kCoast);
        
        m_right = new CANSparkMax(kElevator.kRightCAN, MotorType.kBrushless);
        m_right.follow(m_left, true);
        m_right.setIdleMode(IdleMode.kCoast);
        
        s_encoder = m_left.getEncoder();
        zeroEncoder();
        
        ratchetLock = new DoubleSolenoid(kPneumatics.kHubModuleID, kPneumatics.kPneumaticsModuleType, 14, 15);
        unlockRatchet();

        s_magSwitch = new DigitalInput(kElevator.kMagSwitchDIO);      

        c_pidController = m_left.getPIDController();
        c_pidController.setOutputRange(-1, 1);
        setPIDFValues();
        m_left.burnFlash();

        shuffleboardFields = new HashMap<String, NetworkTableEntry>();
        
        ShuffleboardLayout encoder = Shuffleboard.getTab("Elevator").getLayout("Encoder Values", BuiltInLayouts.kList);
        
        ShuffleboardLayout active = Shuffleboard.getTab("Elevator").getLayout("Elevator Active", BuiltInLayouts.kList);

        ShuffleboardLayout extras = Shuffleboard.getTab("Elevator").getLayout("Extras", BuiltInLayouts.kList);

        shuffleboardFields.put("EncoderDistance", encoder.add("Encoder Distance Travelled", getPosition()).withWidget(BuiltInWidgets.kTextView).getEntry());

        shuffleboardFields.put("LimitSwitch", extras.add("Mag Switch Active?", detectLimSwitch()).withWidget(BuiltInWidgets.kBooleanBox).getEntry());

        shuffleboardFields.put("ElevatorActive", active.add("Elevator Active?", elevatorActive).withWidget(BuiltInWidgets.kBooleanBox).getEntry());

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        shuffleboardFields.get("EncoderDistance").setNumber(getPosition());
        shuffleboardFields.get("ElevatorActive").setBoolean(elevatorActive);
        shuffleboardFields.get("LimitSwitch").setBoolean(detectLimSwitch());
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
        c_pidController.setP(Constants.kElevator.kP);
        c_pidController.setD(Constants.kElevator.kI);
        c_pidController.setI(Constants.kElevator.kD);
        c_pidController.setFF(Constants.kElevator.kF);
    }
    
    public void moveElevator(double setPoint) {
        c_pidController.setReference(setPoint, ControlType.kPosition);
    }

    public void startZeroing() {
        m_left.set(-0.1);
    }

    public void setElevatorState(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public boolean getElevatorState() {
        return isMoving;
    }

    public void disableMotors() {
        m_left.disable();
        m_right.disable();
    }

    public void lockRatchet() {
        ratchetLock.set(DoubleSolenoid.Value.kForward);
    }

    public void unlockRatchet() {
        ratchetLock.set(DoubleSolenoid.Value.kReverse);
    }

    public Value getRatchetState() {
        return ratchetLock.get();
    }
}