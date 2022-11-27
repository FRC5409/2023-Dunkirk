package frc.robot.subsystems.elevator;

import java.io.FileReader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


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

    public static CANSparkMax m_left;
    public static CANSparkMax m_right;

    private RelativeEncoder s_encoder;

    private final DigitalInput s_magSwitch;

    public  SparkMaxPIDController c_pidController;
    
    private static JSONParser parser;
    private static Object jsonObject;
    private static JSONObject jsonFile;

    private static boolean configEnabled = false;

    private static HashMap<String, NetworkTableEntry> shuffleboardFields;
    private static ShuffleboardLayout pidLayout;


    
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
        setPIDFValues();

        pidLayout = Shuffleboard.getTab("Elevator").getLayout("PID Config", BuiltInLayouts.kList);

        shuffleboardFields.put("kPConfig", pidLayout.add("Proportional Gain Config", (double) jsonFile.get("kP")).withWidget(BuiltInWidgets.kTextView).getEntry());
        shuffleboardFields.put("kIConfig", pidLayout.add("Integral Gain Config", (double) jsonFile.get("kI")).withWidget(BuiltInWidgets.kTextView).getEntry());
        shuffleboardFields.put("kDConfig", pidLayout.add("Derivative Gain Config", (double) jsonFile.get("kD")).withWidget(BuiltInWidgets.kTextView).getEntry());
        shuffleboardFields.put("kFConfig", pidLayout.add("Feedforward Gain Config", (double) jsonFile.get("kF")).withWidget(BuiltInWidgets.kTextView).getEntry());

        shuffleboardFields.put("Enable Config", pidLayout.add("Enable Configuration", false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry());

        parser = new JSONParser();
        jsonObject = getJsonFile();

        jsonFile = (JSONObject) jsonObject;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (shuffleboardFields.get("Enable Config").getBoolean(false) == true && configEnabled == false) {
            configEnabled = true;
            shuffleboardFields.put("kPConfig", pidLayout.add("Proportional Gain Config", (double) jsonFile.get("kP")).withWidget(BuiltInWidgets.kTextView).getEntry());
            shuffleboardFields.put("kIConfig", pidLayout.add("Integral Gain Config", (double) jsonFile.get("kI")).withWidget(BuiltInWidgets.kTextView).getEntry());
            shuffleboardFields.put("kDConfig", pidLayout.add("Derivative Gain Config", (double) jsonFile.get("kD")).withWidget(BuiltInWidgets.kTextView).getEntry());
            shuffleboardFields.put("kFConfig", pidLayout.add("Feedforward Gain Config", (double) jsonFile.get("kF")).withWidget(BuiltInWidgets.kTextView).getEntry());
        }

        if (shuffleboardFields.get("Enable Config").getBoolean(true) == false && configEnabled == true) {
            configEnabled = false;
        }

        if (configEnabled == true) {

            if (shuffleboardFields.get("kPConfig").getDouble(0) != (double) jsonFile.get("kP")) {
                jsonFile.put("kP", shuffleboardFields.get("kPConfig").getDouble(0));
                jsonFile = (JSONObject) getJsonFile();
            }
            if (shuffleboardFields.get("kIConfig").getDouble(0) != (double) jsonFile.get("kI")) {
                jsonFile.put("kI", shuffleboardFields.get("kIConfig").getDouble(0));
                jsonFile = (JSONObject) getJsonFile();
            }
            if (shuffleboardFields.get("kDConfig").getDouble(0) != (double) jsonFile.get("kD")) {
                jsonFile.put("kD", shuffleboardFields.get("kDConfig").getDouble(0));
                jsonFile = (JSONObject) getJsonFile();
            }
            if (shuffleboardFields.get("kFConfig").getDouble(0) != (double) jsonFile.get("kF")) {
                jsonFile.put("kF", shuffleboardFields.get("kFConfig").getDouble(0));
                jsonFile = (JSONObject) getJsonFile();
            }
            

            if (c_pidController.getP() != (double) jsonFile.get("kP")) {
                c_pidController.setP((double) jsonFile.get("kP"));
            } 
            if (c_pidController.getI() != (double) jsonFile.get("kI")) {
                c_pidController.setI((double) jsonFile.get("kI"));
            } 
            if (c_pidController.getD() != (double) jsonFile.get("kD")) {
                c_pidController.setD((double) jsonFile.get("kD"));
            }  
            if (c_pidController.getFF() != (double) jsonFile.get("kF")) {
                c_pidController.setFF((double) jsonFile.get("kF"));
            }
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

    public void setPIDFValues() {
        c_pidController.setP((double) jsonFile.get("kP"));
        c_pidController.setI((double) jsonFile.get("kI"));
        c_pidController.setD((double) jsonFile.get("kD"));
        c_pidController.setFF((double) jsonFile.get("kF"));
    }

    public Object getJsonFile() {
        try {
            return parser.parse(new FileReader("ElevatorPID.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void moveElevator(double setpoint) {
        c_pidController.setReference(setpoint, ControlType.kPosition);
    }

}