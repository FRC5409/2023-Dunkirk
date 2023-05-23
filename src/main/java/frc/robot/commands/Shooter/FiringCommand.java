package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kShooter;
import frc.robot.Constants.kShooter.kShooterData;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Gyroscope;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.util.InterpolatedData;

public class FiringCommand extends CommandBase {

    private final Shooter    m_shooter;
    private final Turret     m_turret;
    private final Feeder     m_feeder;
    private final Indexer    m_indexer;
    private final Limelight  m_limelight;
    private       Gyroscope  m_gyro;
    private final boolean    enableDriveBy;

    private int currentSpeed = -1;
    private boolean isFeeding = false;

    private boolean guessDrive = false;

    public FiringCommand(Shooter shooter, Turret turret, Feeder feeder, Indexer indexer, Limelight limelight) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        m_turret = turret;
        m_feeder = feeder;
        m_indexer = indexer;
        m_limelight = limelight;
        m_gyro = null;
        enableDriveBy = false;
        
        addRequirements(m_shooter, m_feeder, m_indexer);
    }

    public FiringCommand(Shooter shooter, Turret turret, Feeder feeder, Indexer indexer, Limelight limelight, boolean driveBy) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooter = shooter;
        m_turret = turret;
        m_feeder = feeder;
        m_indexer = indexer;
        m_limelight = limelight;
        m_gyro = null;
        enableDriveBy = driveBy;
        guessDrive = false;
        
        addRequirements(m_shooter, m_feeder, m_indexer);
    }

    public FiringCommand(Shooter shooter, Turret turret, Feeder feeder, Indexer indexer, Limelight limelight, Gyroscope gyro) {
        m_shooter = shooter;
        m_turret = turret;
        m_feeder = feeder;
        m_indexer = indexer;
        m_limelight = limelight;
        m_gyro = gyro;
        enableDriveBy = true;

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

        if (enableDriveBy && !m_indexer.isForced()) {
            if (guessDrive) {
                int closestPoint = InterpolatedData.closestPoint(Math.abs(m_gyro.getForwardSpeed()), kShooter.kShooterData.driveBySteps);
                double[] dataX = kShooterData.driveSpeedX;
                double[] dataY = kShooterData.driveSpeedY;

                shooterSpeed += InterpolatedData.getInterpolatedSpeed(
                        dataX[closestPoint],
                        dataY[closestPoint],
                        dataX[closestPoint + 1],
                        dataY[closestPoint + 1],
                        Math.abs(m_gyro.getForwardSpeed())
                    ) * m_gyro.getForwardSpeed() > 0 ? -1 : 1;

            } else {
                double distanceChange = m_limelight.getDistanceChange();
                int closestPoint = InterpolatedData.closestPoint(Math.abs(distanceChange), kShooterData.shooterSteps);
                int[] dataX = kShooterData.shooterDataX;
                int[] dataY = kShooterData.shooterDataY;

                shooterSpeed += InterpolatedData.getInterpolatedSpeed(
                    dataX[closestPoint],
                    dataY[closestPoint],
                    dataX[closestPoint + 1],
                    dataY[closestPoint + 1],
                    Math.abs(distanceChange)
                ) * distanceChange > 0 ? 1: -1;
                
            }
        }

        if (m_indexer.isForced()) {
            spinShooterAt(kShooter.wrongCargoSpeed);
        } else {
            if (m_turret.getState() == State.kLocked) {
                spinShooterAt((int) Math.round(shooterSpeed));
                if (Math.abs(m_shooter.getAverageSpeed() - shooterSpeed) < kShooter.shooterRPMPlay) {
                    feed();
                } else {
                    stopFeeding();
                }
            } else {
                stopFeeding();
            }
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
            m_indexer.startIntaking(0);
            isFeeding = true;
        }
    }

    /**
     * stops feeding a peice of cargo
     */
    public void stopFeeding() {
        if (isFeeding) {
            m_feeder.stopMotor();
            m_indexer.stopIntaking(0);
            isFeeding = false;
        }
    }

    /**
     * Stops the shooter motor
     */
    public void stopShooting() {
        m_shooter.stopMot();
    }

    /**
     * Gets the shooter speed based on the distance to the target
     * @return shooter speed in RPM
     */
    public double getShooterSpeed() {
        double distanceToTarget = m_limelight.getDistanceToTarget();
        int closestPoint = InterpolatedData.closestPoint(distanceToTarget, kShooterData.shooterSteps);

        int dataX[] = kShooterData.shooterDataX;
        int dataY[] = kShooterData.shooterDataY;

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
