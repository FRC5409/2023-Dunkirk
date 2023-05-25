package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.subsystems.Gyroscope;
import frc.robot.subsystems.Turret;

public class TargetWithLimelightOff extends CommandBase {

    private final Turret m_turret;
    private final Gyroscope m_gyro;

    public TargetWithLimelightOff(Turret turret, Gyroscope gyro) {
        m_turret = turret;
        m_gyro = gyro;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_turret);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_turret.setMaxSpeed(kTurret.maxVolts);
        m_turret.configAccel(kTurret.maxScanningAccel);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_turret.setRefrence(m_turret.getLastPosition() - m_gyro.getAbsoluteAngle());
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
