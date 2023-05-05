package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Limelight.LedMode;

public class TrainingShooterCommand extends CommandBase {

    private final Shooter    m_shooter;
    private final Limelight  m_limelight;

    public TrainingShooterCommand(Shooter shooter, Limelight limelight) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        m_limelight = limelight;

        addRequirements(m_shooter);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_shooter.spinMotAtSpeed(m_shooter.getTrainingSpeed());
        m_limelight.setLedMode(LedMode.kModeOn);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.stopMot();
        m_limelight.setLedMode(LedMode.kModeOff);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
