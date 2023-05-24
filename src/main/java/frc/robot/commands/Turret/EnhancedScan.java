package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.LedMode;

public class EnhancedScan extends CommandBase {

    private final Turret m_turret;
    private final Limelight m_limelight;

    public EnhancedScan(Turret turret, Limelight limelight) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turret = turret;
        m_limelight = limelight;

        addRequirements(m_turret);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_turret.setMaxSpeed(kTurret.maxVolts);
        m_turret.configAccel(kTurret.maxScanningAccel);

        if (!m_limelight.isOn())
            m_limelight.setLedMode(LedMode.kModeOn);
            
        m_turret.setState(State.kScaning);

        if (m_turret.getLastKnownPosition() > 0)
            m_turret.setRefrence(kTurret.maxPosition);
        else
            m_turret.setRefrence(-kTurret.maxPosition);
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_limelight.isVisable();
    }

}
