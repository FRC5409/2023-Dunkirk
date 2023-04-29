package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.LedMode;

public class LockOnTarget extends CommandBase {

    private final Turret m_turret;
    private final Limelight m_limelight;

    private final boolean updateLocation = false;

    public LockOnTarget(Turret turret, Limelight limelight) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turret = turret;
        m_limelight = limelight;

        addRequirements(m_turret);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_turret.setState(State.kLocking);

        if (!m_limelight.isOn())
            m_limelight.setLedMode(LedMode.kModeOn);

        //Try here first to see how accurate it is
        updateLocation();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (updateLocation) {
            if (m_turret.atSetpoint()) {
                if (Math.abs(m_limelight.getXAngle()) >= kTurret.angleThreshold) {
                    updateLocation();
                }
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return !m_limelight.isVisable();
    }

    public void updateLocation() {
        double offset = m_turret.getAngle();

        double desiredSetpoint = offset + m_limelight.getXAngle();

        if (desiredSetpoint > kTurret.maxAngle)
            desiredSetpoint = kTurret.maxAngle;

        if (desiredSetpoint < -kTurret.maxAngle)
            desiredSetpoint = -kTurret.maxAngle;

        desiredSetpoint = m_turret.convertToEncoder(desiredSetpoint);

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
