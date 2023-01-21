package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ToggleLowGear extends InstantCommand {

    private final DriveTrain m_driveTrain;

    public ToggleLowGear(DriveTrain driveTrain) {

        m_driveTrain = driveTrain;
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
 
        m_driveTrain.changeGear(DoubleSolenoid.Value.kForward);
        
    }
}