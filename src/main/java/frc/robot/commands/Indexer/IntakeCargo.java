package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class IntakeCargo extends CommandBase {

    private final Indexer m_indexer;

    public IntakeCargo(Indexer indexer) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_indexer = indexer;

        // addRequirements(m_indexer);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_indexer.startIntaking(1);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_indexer.stopIntaking(1);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
