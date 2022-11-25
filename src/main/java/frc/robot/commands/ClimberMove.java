package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.elevator.Elevator;

public class ClimberMove extends CommandBase {

    private final Elevator sys_elevator;
    private final XboxController c_joystick;

    public ClimberMove(Elevator subsystem, XboxController joystick) {
        sys_elevator = subsystem;
        c_joystick = joystick;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double zSpeedUp = c_joystick.getRightTriggerAxis();
        double zSpeedDown = c_joystick.getLeftTriggerAxis();

        double zSpeed = zSpeedUp - zSpeedDown;

        Elevator.m_left.set(zSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        
        return false;
    }

}