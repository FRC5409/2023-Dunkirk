package frc.robot.commands.Drive;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveTrain;

public class SetCoastMode extends InstantCommand {

    private final DriveTrain m_driveTrain;

    public SetCoastMode(DriveTrain driveTrain) {

        m_driveTrain = driveTrain;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveTrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_driveTrain.setNeutralMode(NeutralMode.Coast);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}