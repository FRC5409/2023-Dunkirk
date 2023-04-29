package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kShooter;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.util.InterpolatedData;

public class FiringCommand extends CommandBase {

    private final Shooter m_shooter;
    private final Turret m_turret;
    private final Feeder m_feeder;
    private final Indexer m_indexer;
    private final Limelight m_limelight;

    private int currentSpeed = -1;
    private boolean isFeeding = false;

    public FiringCommand(Shooter shooter, Turret turret, Feeder feeder, Indexer indexer, Limelight limelight) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        m_turret = turret;
        m_feeder = feeder;
        m_indexer = indexer;
        m_limelight = limelight;

        addRequirements(m_shooter, m_feeder, m_indexer);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        spinShooterAt(kShooter.shooterPrepSpeed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        double shooterSpeed = 2500; //Replace with interpolated data later

        if (m_turret.getState() == State.kLocked) {
            spinShooterAt((int) Math.round(shooterSpeed));
            if (Math.abs(m_shooter.getAverageSpeed() - shooterSpeed) < kShooter.shooterRPMPlay) {
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
        stopShooting();
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
            m_indexer.startIntaking();
            isFeeding = true;
        }
    }

    /**
     * stops feeding a peice of cargo
     */
    public void stopFeeding() {
        if (isFeeding) {
            m_feeder.stopFeeding();
            m_indexer.stopIntaking();
            isFeeding = false;
        }
    }

    /**
     * Stops the shooter motor
     */
    public void stopShooting() {
        m_shooter.stopMot();
    }

    public double getShooterSpeed() {
        double distanceToTarget = m_limelight.getDistanceToTarget();
        int closestPoint = InterpolatedData.closestPoint(distanceToTarget, kShooter.kShooterData.shooterSteps);

        double dataX[] = kShooter.kShooterData.shooterDataX;
        double dataY[] = kShooter.kShooterData.shooterDataY;

        double interpolatedSpeed = InterpolatedData.getInterpolatedSpeed(
            dataX[closestPoint],
            dataY[closestPoint],
            dataX[closestPoint + 1],
            dataY[closestPoint + 1],
            distanceToTarget
        );

        return interpolatedSpeed;
    }

}
