package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class LockOnTarget extends CommandBase {

    private final Turret m_turret;
    private final Limelight m_limelight;

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
            m_limelight.turnOnLimelight();

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double xOff = m_limelight.getXOffset();
        double volts = m_turret.getVolts();
        if (xOff < -kTurret.targetingThreshold) {
            //left of target
            if (volts != -kTurret.lockingSpeed) {
                //doesn't let it go past it's max turning position
                if (m_turret.getPosition() > -kTurret.maxPosition) {
                    m_turret.setVolts(-kTurret.lockingSpeed);
                }
            }
            m_turret.setState(State.kLocking);

        } else if (xOff > kTurret.targetingThreshold) {
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

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
