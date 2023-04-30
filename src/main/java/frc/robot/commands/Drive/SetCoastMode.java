package frc.robot.commands.Drive;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Turret;

public class SetCoastMode extends InstantCommand {

    private final DriveTrain m_driveTrain;
    private final Turret m_turret;

    public SetCoastMode(DriveTrain driveTrain, Turret turret) {

        m_driveTrain = driveTrain;
        m_turret = turret;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveTrain, turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_driveTrain.setNeutralMode(NeutralMode.Coast);
        m_turret.setNeutralMode(IdleMode.kCoast);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}