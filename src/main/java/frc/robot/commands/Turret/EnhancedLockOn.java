package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Gyroscope;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.LedMode;

public class EnhancedLockOn extends CommandBase {

    private final Turret m_turret;
    private final Limelight m_limelight;
    private final Gyroscope m_gyro;

    private double lastKnownPosition;

    public EnhancedLockOn(Turret turret, Limelight limelight, Gyroscope gyro) {
        m_turret = turret;
        m_limelight = limelight;
        m_gyro = gyro;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_turret);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if (!m_limelight.isOn())
            m_limelight.setLedMode(LedMode.kModeOn);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        lastKnownPosition = m_turret.getAngle() + m_gyro.getAbsoluteAngle();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_turret.setLastKnownPosition(lastKnownPosition);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return !m_limelight.isVisable();
    }

}