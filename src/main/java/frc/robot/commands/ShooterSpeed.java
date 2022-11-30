package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kShooter;
import frc.robot.Constants.kShooter.kShooterData;
import frc.robot.subsystems.Shooter;

public class ShooterSpeed extends CommandBase {

    private final Shooter m_shooter;
    private final XboxController m_joystick;
    // private final Limelight m_limelight;
    private double distance;
    private double joystickDistance;
    private int time;

    public ShooterSpeed(Shooter shooter, XboxController joystick) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        m_joystick = joystick;
        addRequirements(m_shooter);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        time = 0;
        joystickDistance = 10;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        joystickDistance += m_joystick.getLeftY();
        distance = joystickDistance; //inches
        int index = m_shooter.closestPoint();
        
        double shooterSpeed;
        //getting interpolated data
        try {
            shooterSpeed = m_shooter.getInterpolatedSpeed(kShooter.kShooterData.shooterDataX[index], kShooter.kShooterData.shooterDataY[index], kShooter.kShooterData.shooterDataX[index + 1], kShooter.kShooterData.shooterDataY[index + 1], distance);
        }   catch (Exception e) {
            //if its outside the data use the highest point of data
            shooterSpeed = kShooter.kShooterData.shooterDataY[index];
        }
        SmartDashboard.putNumber("Shooter speed: ", shooterSpeed);
        //spinning the motor
        m_shooter.spinMotAtSpeed(shooterSpeed);
        //if its reached its speed
        if (m_shooter.getVelocity() >= shooterSpeed - kShooter.shooterPlay && m_shooter.getVelocity() <= shooterSpeed + kShooter.shooterPlay) {
            //feed
            time++;
            m_shooter.feed();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.stopMotors();
        time = 0;
        joystickDistance = 10;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() { 
        if (time >= kShooter.feedTime) {
            time = 0;
            return true;
        } else {
            return false;
        }
        
    }

}
