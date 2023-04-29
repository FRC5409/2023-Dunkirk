package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class SpinShooterAt extends CommandBase {

    private final Shooter m_shooter;
    private final double speed;

    public SpinShooterAt(Shooter shooter, int RPM) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        speed = RPM;

        addRequirements(m_shooter);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_shooter.spinMotAtSpeed(speed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}