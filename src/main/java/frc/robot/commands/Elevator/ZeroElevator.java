package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class ZeroElevator extends CommandBase {

    private final Elevator sys_elevator;

    public ZeroElevator(Elevator subsystem) {
        sys_elevator = subsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        sys_elevator.lockRatchet();
        sys_elevator.m_left.set(-0.1);
        sys_elevator.setElevatorState(true);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        sys_elevator.setElevatorState(false);
        sys_elevator.disableMotors();
        sys_elevator.zeroEncoder();

        System.out.println("reached here");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        System.out.println(!sys_elevator.detectLimSwitch());
        return sys_elevator.detectLimSwitch();
    }

}