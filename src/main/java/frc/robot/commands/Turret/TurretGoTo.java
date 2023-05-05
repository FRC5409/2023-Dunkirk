package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.kTurret;
import frc.robot.subsystems.Turret;

public class TurretGoTo extends CommandBase {

    private final Turret m_turret;
    private final double position;

    public TurretGoTo(Turret turret, double pos) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_turret = turret;
        position = pos;

        addRequirements(m_turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_turret.setRefrence(position);
        m_turret.setMaxSpeed(kTurret.maxVolts);
        m_turret.enable();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_turret.getController().atSetpoint();
    }

}
