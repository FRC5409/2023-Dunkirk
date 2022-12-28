package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class MoveElevator extends CommandBase {

    private final Elevator sys_elevator;
    private double setPoint;
    private boolean extending;
    private boolean findingZero;
    private boolean grabbingBar;

    // Constructor for moving the elevator up to a setpoint
    public MoveElevator(Elevator subsystem, double destination) {
        sys_elevator = subsystem;
        setPoint = destination;
        extending = true;
        findingZero = false;
        if (sys_elevator.getPrevPos() == setPoint) {
            isFinished();
        }
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
    }

    // Constructor for moving the elevator down to the bar
    public MoveElevator(Elevator subsystem) {
        sys_elevator = subsystem;
        extending = false;
        findingZero = false;
        setPoint = Constants.kElevator.kRetractToBar;
        if (sys_elevator.getPrevPos() == setPoint) {
            isFinished();
        }

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
    }

    // Constructor for moving down to find the zero
    public MoveElevator(Elevator subsystem, boolean findingZero) {
        this.findingZero = findingZero;
        sys_elevator = subsystem;

        addRequirements(sys_elevator);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if(findingZero) {
            sys_elevator.m_left.set(-0.1);
        } else if (extending && !findingZero) {
            if(sys_elevator.getRatchetState() == Value.kForward) {
                sys_elevator.unlockRatchet();
            }
            sys_elevator.setElevatorState(true);
            sys_elevator.moveElevator(setPoint);       
        } else if (!extending && !findingZero) {
            if (sys_elevator.getRatchetState() == Value.kReverse) {
                sys_elevator.lockRatchet();
            }
            sys_elevator.setElevatorState(true);
            sys_elevator.moveElevator(setPoint);
        }
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if(!extending && !findingZero) {
            if (setPoint == Constants.kElevator.kRetractToBar && setPoint - 0.5 <= sys_elevator.getPosition() && sys_elevator.getPosition() == setPoint) {
                grabbingBar = true;
            } 
        }

        if (findingZero) {
            if (sys_elevator.detectLimSwitch()) {
                sys_elevator.zeroEncoder();
                sys_elevator.m_left.set(0);
            }
        }
 
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        if (sys_elevator.getRatchetState() == Value.kReverse) {
            sys_elevator.lockRatchet();
        }
        sys_elevator.setElevatorState(false);
        sys_elevator.setPrevPos(setPoint);
        sys_elevator.disableMotors();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (!findingZero) {
            return setPoint-0.5 <= sys_elevator.getPosition() && sys_elevator.getPosition() <= setPoint+0.5;
        } else if (findingZero) {
            return sys_elevator.getPosition() == 0;
        } else {
            return true;
        }
    }
}