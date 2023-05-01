package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class TrainingShooterCommand extends CommandBase {

    private final Shooter m_shooter;

    public TrainingShooterCommand(Shooter shooter) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;

        addRequirements(m_shooter);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_shooter.spinMotAtSpeed(m_shooter.getTrainingSpeed());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.stopMot();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
