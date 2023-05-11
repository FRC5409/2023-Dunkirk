package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;

public class ReverseIndexer extends CommandBase {

    private final Indexer m_indexer;
    private final Feeder m_feeder;

    public ReverseIndexer(Indexer inderxer, Feeder feeder) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_indexer = inderxer;
        m_feeder = feeder;

        addRequirements(m_indexer, m_feeder);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_indexer.reverseIntake();
        m_feeder.revsere();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_indexer.stopIntaking();
        m_feeder.stopMotor();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
