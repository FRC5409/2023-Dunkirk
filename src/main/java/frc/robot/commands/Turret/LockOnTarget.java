package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Gyroscope;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.LedMode;
import frc.robot.util.InterpolatedData;

public class LockOnTarget extends CommandBase {

    private final Turret     m_turret;
    private final Limelight  m_limelight;
    private       Gyroscope  m_gyro;
    private final boolean    enableDriveBy;

    private int seeingTime = 0;

    private final boolean updateLocation = false;

    public LockOnTarget(Turret turret, Limelight limelight) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turret = turret;
        m_limelight = limelight;
        enableDriveBy = false;

        addRequirements(m_turret);
        
    }

    public LockOnTarget(Turret turret, Limelight limelight, Gyroscope gyro) {
        m_turret = turret;
        m_limelight = limelight;
        m_gyro = gyro;
        enableDriveBy = true;

        addRequirements(m_turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        seeingTime = 0;

        if (!m_limelight.isOn())
            m_limelight.setLedMode(LedMode.kModeOn);

        //Try here first to see how accurate it is
        updateLocation();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (updateLocation) {
            if (m_limelight.isVisable()) {
                if (m_turret.atSetpoint()) {
                    if (Math.abs(m_limelight.getXAngle()) >= kTurret.angleThreshold) {
                        updateLocation();
                    } else {
                        m_turret.setState(State.kLocked);
                    }
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
            int closestPoint = InterpolatedData.closestPoint(Math.abs(m_gyro.getForwardSpeed()), kTurret.driveBySteps);
            double[] dataX = kTurret.driveOffsetX;
            double[] dataY = kTurret.driveOffsetY;

            offset += InterpolatedData.getInterpolatedSpeed(
                    dataX[closestPoint],
                    dataY[closestPoint],
                    dataX[closestPoint + 1],
                    dataY[closestPoint + 1],
                    Math.abs(m_gyro.getForwardSpeed())
                ) * m_gyro.getForwardSpeed() > 0 ? -1 : 1;
        }

        double desiredSetpoint = offset + m_turret.convertToEncoder(m_limelight.getXAngle()) + m_turret.getTurretOffset();

        if (desiredSetpoint > kTurret.maxPosition)
            desiredSetpoint = kTurret.maxPosition;

        if (desiredSetpoint < -kTurret.maxPosition)
            desiredSetpoint = -kTurret.maxPosition;

        m_turret.setRefrence(desiredSetpoint);
    }

    public void oldTargetingCode() {
        double xOff = m_limelight.getXAngle();
        double volts = m_turret.getVolts();
        if (xOff < -kTurret.angleThreshold) {
            //left of target
            if (volts != -kTurret.lockingSpeed) {
                //doesn't let it go past it's max turning position
                if (m_turret.getPosition() > -kTurret.maxPosition) {
                    m_turret.setVolts(-kTurret.lockingSpeed);
                }
            }
            m_turret.setState(State.kLocking);

        } else if (xOff > kTurret.angleThreshold) {
            //right of target
            if (volts != kTurret.lockingSpeed) {
                //doesn't let it go past it's max turning position
                if (m_turret.getPosition() < kTurret.maxPosition) {
                    m_turret.setVolts(kTurret.lockingSpeed);
                }
            }
            m_turret.setState(State.kLocking);

        } else {
            //on target
            m_turret.stopMot();
            m_turret.setState(State.kLocked);
        }
    }
}
