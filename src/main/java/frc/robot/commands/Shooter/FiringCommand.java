package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kShooter;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class FiringCommand extends CommandBase {

    private final Shooter m_shooter;
    private final Turret m_turret;
    private final Feeder m_feeder;

    private int currentSpeed = -1;
    private boolean isFeeding = false;

    public FiringCommand(Shooter shooter, Turret turret, Feeder feeder) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        m_turret = turret;
        m_feeder = feeder;

        addRequirements(m_shooter, m_feeder);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        spinShooterAt(kShooter.shooterPrepSpeed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_turret.getState() == State.kLocked) {
            spinShooterAt(2500);
            if (Math.abs(m_shooter.getAverageSpeed() - 2500) < kShooter.shooterRPMPlay) {
                feed();
            } else {
                stopFeeding();
            }
        } else {
            spinShooterAt(kShooter.shooterPrepSpeed);
            stopFeeding();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        spinShooterAt(0);
        stopFeeding();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }


    /**
     * spin motor at specified RPM
     * @param RPM RPM
     */
    public void spinShooterAt(int RPM) {
        if (currentSpeed != RPM) {
            currentSpeed = RPM;
            m_shooter.spinMotAtSpeed(RPM);
        }
    }

    /**
     * Feeds a peice of cargo
     */
    public void feed() {
        if (!isFeeding) {
            m_feeder.feed();
            isFeeding = false;
        }
    }

    /**
     * stops feeding a peice of cargo
     */
    public void stopFeeding() {
        if (isFeeding) {
            m_feeder.stopFeeding();
            isFeeding = true;
        }
    }

}
