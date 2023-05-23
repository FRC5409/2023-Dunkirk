package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kFeeder;
import frc.robot.Constants.kIndexer;
import frc.robot.Constants.kTurret;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class WrongCargo extends CommandBase {

    private final Turret     m_turret;
    private final Shooter    m_shooter;
    private final Feeder     m_feeder;
    private final Indexer    m_indexer;

    private double prevPos = 0;

    public WrongCargo(Turret turret, Shooter shooter, Feeder feeder, Indexer indexer) {
        m_turret     = turret;
        m_shooter    = shooter;
        m_feeder     = feeder;
        m_indexer    = indexer;

        super.withTimeout(kTurret.wrongCargoTime);

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_turret);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        prevPos = m_turret.getPosition();
        if (prevPos > 0) {
            m_turret.setRefrence(prevPos - kTurret.wrongCargoOffset);
        } else {
            m_turret.setRefrence(prevPos + kTurret.wrongCargoOffset);
        }

        m_indexer.forceFeed(kIndexer.indexerSpeed);
        m_feeder.forceFeed(kFeeder.feedSpeed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_turret.setRefrence(prevPos);

        m_indexer.stopForceSpeed();
        m_feeder.stopForceSpeed();

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
