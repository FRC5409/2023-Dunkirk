package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.LedMode;
import frc.robot.util.InterpolatedData;

public class LockOnTarget extends CommandBase {

    private final Turret     m_turret;
    private final Limelight  m_limelight;
    private final boolean    enableDriveBy;

    private int seeingTime = 0;

    private final boolean updateLocation = true;

    private boolean guessDrive = false;

    public LockOnTarget(Turret turret, Limelight limelight, boolean driveBy) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turret = turret;
        m_limelight = limelight;
        enableDriveBy = driveBy;

        addRequirements(m_turret);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_turret.setMaxSpeed(kTurret.maxVolts);
        seeingTime = 0;

        if (!m_limelight.isOn())
            m_limelight.setLedMode(LedMode.kModeOn);

        //Try here first to see how accurate it is
        updateLocation();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_limelight.isVisable()) {
            if (m_turret.getController().atSetpoint()) {
                if (Math.abs(m_limelight.getXAngle()) >= kTurret.angleThreshold) {
                    if (updateLocation) {
                        updateLocation();
                    }
                } else {
                    m_turret.setState(State.kLocked);
                }
            }
        }

        if (!m_limelight.isVisable()) {
            seeingTime++;
        } else {
            seeingTime = 0;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        //when not seen for x amount of time, finish
        return seeingTime > kTurret.maxNotSeeingTargetTime;
    }

    public void updateLocation() {
        m_turret.setState(State.kLocking);

        double offset = m_turret.getPosition();

        if (enableDriveBy) {
            if (guessDrive) {
                double distanceChange = m_limelight.getDistanceChange();
                int closestPoint = InterpolatedData.closestPoint(Math.abs(distanceChange), kTurret.driveBySteps);
                double[] dataX = kTurret.driveOffsetX;
                double[] dataY = kTurret.driveOffsetY;

                offset += InterpolatedData.getInterpolatedSpeed(
                        dataX[closestPoint],
                        dataY[closestPoint],
                        dataX[closestPoint + 1],
                        dataY[closestPoint + 1],
                        Math.abs(distanceChange)
                    ) * distanceChange > 0 ? -1 : 1;
            } else {
                double angleChange = m_limelight.getAngleChange();
                offset += m_turret.convertToEncoder(angleChange);
            }
        }

        double desiredSetpoint = offset + m_turret.convertToEncoder(m_limelight.getXAngle()) + m_turret.getTurretOffset();

        if (desiredSetpoint > kTurret.maxPosition)
            desiredSetpoint = kTurret.maxPosition;

        if (desiredSetpoint < -kTurret.maxPosition)
            desiredSetpoint = -kTurret.maxPosition;

        m_turret.setRefrence(desiredSetpoint);
    }

}
