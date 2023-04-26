package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.scanningDirection;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class Scan extends CommandBase {

    private final Turret m_turrent;
    private final Limelight m_limelight;
    private int time;

    public Scan(Turret turrent, Limelight limelight) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turrent = turrent;
        m_limelight = limelight;

        addRequirements(m_turrent);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_limelight.turnOnLimelight();
        time = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double volts = Math.cos(time / kTurret.CosDiv) * kTurret.maxScanOutput;
        
        if (m_turrent.getPosition() < -kTurret.maxPosition || m_turrent.getPosition() > kTurret.maxPosition) {
            m_turrent.stopMot();
        } else {
            if (m_turrent.getScanningDir() == scanningDirection.kLeft) {
                m_turrent.setVolts(-volts);
            } else {
                m_turrent.setVolts(volts);
            }
        }

        time++;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_limelight.isVisable();
    }

}
