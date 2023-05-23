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
    private final Feeder     m_feeder;
    private final Indexer    m_indexer;
    private final Shooter    m_shooter;

    private double prevPos = 0;

    public WrongCargo(Turret turret,  Feeder feeder, Indexer indexer, Shooter shooter) {
        m_turret     = turret;
        m_feeder     = feeder;
        m_indexer    = indexer;
        m_shooter    = shooter;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_turret);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        prevPos = m_turret.getPosition();
        m_turret.setMaxSpeed(kTurret.maxVolts);
        m_turret.configAccel(kTurret.maxOffsetAccel);
        if (prevPos > 0) {
            m_turret.setRefrence(prevPos - kTurret.wrongCargoOffset);
        } else {
            m_turret.setRefrence(prevPos + kTurret.wrongCargoOffset);
        }

        m_indexer.forceSpeed(kIndexer.indexerSpeed);
        m_feeder.forceSpeed(kFeeder.feedSpeed);

        m_shooter.spinMotAtSpeed(2500);
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

        m_shooter.stopMot();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
