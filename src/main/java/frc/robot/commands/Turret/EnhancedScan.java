package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.Constants.kTurret.State;
import frc.robot.subsystems.Gyroscope;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.LedMode;

public class EnhancedScan extends CommandBase {

    private final Turret m_turret;
    private final Limelight m_limelight;
    private final Gyroscope m_gyro;

    public EnhancedScan(Turret turret, Limelight limelight, Gyroscope gyro) {
        m_turret = turret;
        m_limelight = limelight;
        m_gyro = gyro;

        // Use addRequirements() here to declare subsystem dependencies.
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

        updateRefrence();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (m_turret.getController().atGoal()) {
            m_turret.setRefrence(m_turret.getController().getGoal().position * -1);
        } else {
            updateRefrence();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_limelight.isVisable();
    }

    public void updateRefrence() {
        m_turret.setRefrence(
            kTurret.maxPosition * ((m_turret.getLastPosition() - m_gyro.getAbsoluteAngle()) > 0 ? 1 : -1)
        );
    }

}
