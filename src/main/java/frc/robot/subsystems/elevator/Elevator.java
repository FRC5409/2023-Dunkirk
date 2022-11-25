package frc.robot.subsystems.elevator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
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
import io.github.oblarg.oblog.annotations.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Elevator extends SubsystemBase implements Loggable {

    public static CANSparkMax m_left;
    private static CANSparkMax m_right;

    private static RelativeEncoder s_encoder;

    private static DigitalInput s_magSwitch;

    public static SparkMaxPIDController c_pidController;
    
    private static double setPoint = 0;
    
    private static String directory = System.getProperty("user.dir");
    private static JSONParser parser;
    private static Object jsonObject;

    private static JSONObject jsonFile;
    private static JSONObject pidJsonObject;
    
    private static double kP;
    private static double kI;
    private static double kD;
    private static double kF;
    


    @Log
    public static double DistanceTravelled = s_encoder.getPosition();
    
    public Elevator() throws NoSuchFieldException, SecurityException, FileNotFoundException, IOException, ParseException {
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

        parser = new JSONParser();
        jsonObject = getJsonParser();

        jsonFile = (JSONObject) jsonObject;
        pidJsonObject = (JSONObject) jsonFile.get("PIDF");
        
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        
        if (c_pidController.getP() != (double) pidJsonObject.get("kP")) {
            c_pidController.setP((double) pidJsonObject.get("kP"));
        } else if (c_pidController.getI() != (double) pidJsonObject.get("kI")) {
            c_pidController.setI((double) pidJsonObject.get("kI"));
        } else if (c_pidController.getD() != (double) pidJsonObject.get("kD")) {
            c_pidController.setD((double) pidJsonObject.get("kD"));
        } else if (c_pidController.getD() != (double) pidJsonObject.get("kD")) {
            c_pidController.setFF((double) pidJsonObject.get("kF"));
        }

        /*
        if (detectLimSwitch()) {
            zeroEncoder();
        }
        */
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

    public Object getJsonParser() throws FileNotFoundException, IOException, ParseException {
        return parser.parse(new FileReader("ElevatorPID.json"));
    }

    public void setPIDFValues() {
        c_pidController.setP(kP);
        c_pidController.setI(kI);
        c_pidController.setD(kD);
        c_pidController.setFF(kF);
    }
    
    public void moveElevator(double setpoint) {
        c_pidController.setReference(setpoint, ControlType.kPosition);
    }

}