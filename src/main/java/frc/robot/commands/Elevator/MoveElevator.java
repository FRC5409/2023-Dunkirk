package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class MoveElevator extends CommandBase {

    private final Elevator sys_elevator;
    private double setPoint;
    private boolean extending;
    //private Timer timer;

    // Constructor for moving the elevator up to a setpoint
    public MoveElevator(Elevator subsystem, double destination) {
        sys_elevator = subsystem;
        setPoint = destination;
        extending = true;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
    }

    // Constructor for moving the elevator down to the bar
    public MoveElevator(Elevator subsystem) {
        sys_elevator = subsystem;
        extending = false;
        setPoint = Constants.kElevator.kRetractToBar;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if (extending) {
            if(sys_elevator.getRatchetState() == Value.kForward) {
                sys_elevator.unlockRatchet();
                Timer.delay(1);
            }  
        } else if (!extending) {
            if (sys_elevator.getRatchetState() == Value.kReverse) {
                sys_elevator.lockRatchet();
            }
        }
        sys_elevator.setElevatorState(true);
        sys_elevator.moveElevator(setPoint);
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {  
        
        if (!interrupted) {
            if (sys_elevator.getRatchetState() == Value.kReverse) {
                sys_elevator.lockRatchet();
            }
            sys_elevator.setElevatorState(false);
            sys_elevator.disableMotors();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return Math.abs(setPoint - sys_elevator.getPosition()) < 0.5;
    }
}