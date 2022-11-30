package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kShooter;
import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kShooter;
import frc.robot.Constants.kShooter.kShooterData;
import frc.robot.subsystems.Shooter;

public class ShooterSpeed extends CommandBase {

    private ShuffleboardTab tab = Shuffleboard.getTab("Shooter");
    private NetworkTableEntry distanceEntry = tab.add("Distance to the target", 0).getEntry();
    private NetworkTableEntry targetSpeedEntry = tab.add("Target Speed", 0).getEntry();
    private NetworkTableEntry shooterSpeedEntry = tab.add("Shooter Speed", 0).getEntry();
    // private NetworkTableEntry kP = tab.add("kP", 0).getEntry();
    // private NetworkTableEntry kI = tab.add("kI", 0).getEntry();
    // private NetworkTableEntry kD = tab.add("kD", 0).getEntry();
    // private NetworkTableEntry kF = tab.add("kF", 0).getEntry();
    // private NetworkTableEntry timeEntry = tab.add("Time: ", 0).getEntry();//debugging
    // private NetworkTableEntry indexEntry = tab.add("Index: ", 0).getEntry();//debugging

    private final Shooter m_shooter;
    private final XboxController m_joystick;
    private final Feeder m_feeder;
    // private final Limelight m_limelight;
    private double distance;

    public ShooterSpeed(Shooter shooter, XboxController joystick, Feeder feeder) {//TODO: somepoint at limelight to this to grab the distace
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        m_joystick = joystick;
        m_feeder = feeder;
        addRequirements(m_shooter, m_feeder);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        distance = m_shooter.getTargetDistance(); //inches
        int index = m_shooter.closestPoint();
        // indexEntry.setDouble(index);
        
        double shooterSpeed = 0;
        //getting interpolated data
        try {
            shooterSpeed = m_shooter.getInterpolatedSpeed(kShooter.kShooterData.shooterDataX[index], kShooter.kShooterData.shooterDataY[index], kShooter.kShooterData.shooterDataX[index + 1], kShooter.kShooterData.shooterDataY[index + 1], distance);
        }   catch (Exception e) {
            //if its outside the data use the highest point of data
            DriverStation.reportError("Distance outside shooter data: " + index, true);
            shooterSpeed = kShooter.kShooterData.shooterDataY[kShooter.kShooterData.shooterDataY.length - 1];
        }
        //if its reached its speed
        if (Math.abs(shooterSpeed - m_shooter.getAverageSpeed()) <= kShooter.shooterRPMPlay) {
            //feed
            m_feeder.feed();
        }

        m_shooter.spinMotAtSpeed(shooterSpeed);
        distanceEntry.setDouble(distance);
        targetSpeedEntry.setDouble(shooterSpeed);
        shooterSpeedEntry.setDouble(m_shooter.getAverageSpeed());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.stopMotors();
        m_feeder.stopFeeding();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() { 
        if (!m_joystick.getLeftBumper()) {//if button is off return true
            return true;
        } else {
            return false;
        }
    }
}
