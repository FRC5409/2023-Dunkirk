package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class BurnFlash extends CommandBase {

    private static Elevator sys_elevator;

    public BurnFlash(Elevator subsystem) {
        sys_elevator = subsystem;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        sys_elevator.m_left.burnFlash();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

}