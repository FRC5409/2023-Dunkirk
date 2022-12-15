package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class MoveElevator extends CommandBase {

    private final Elevator sys_elevator;
    private double setPoint;
    private boolean extending;
    private boolean findingZero;
    private boolean overridden;
    private boolean grabbingBar;

    // Constructor for moving the elevator up to a setpoint
    public MoveElevator(Elevator subsystem, double destination) {
        sys_elevator = subsystem;
        setPoint = destination;
        extending = true;
        overridden = false;
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
        overridden = false;
        findingZero = false;
        setPoint = getRetractPos();
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
        overridden = false;

        addRequirements(sys_elevator);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if(findingZero) {
            sys_elevator.m_left.set(-0.1);
        } else if (extending && !findingZero) {
            if(sys_elevator.getRatchetState() == DoubleSolenoid.Value.kForward) {
                sys_elevator.unlockRatchet();
            }
            sys_elevator.setElevatorState(true);
            sys_elevator.moveElevator(setPoint);       
        } else if (!extending && !findingZero) {
            if (sys_elevator.getRatchetState() == DoubleSolenoid.Value.kReverse) {
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
            if (setPoint == Constants.kElevator.kRetractToBar && sys_elevator.getPosition() == setPoint) {
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
        if (!interrupted) {
            if (!overridden) {
                if (sys_elevator.getRatchetState() == DoubleSolenoid.Value.kReverse) {
                    sys_elevator.lockRatchet();
                }
                sys_elevator.setElevatorState(false);
                sys_elevator.setPrevPos(setPoint);
                if (grabbingBar) {
                    sys_elevator.disableMotors();
                }
            }
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (!findingZero) {
            return 0.5-setPoint <= sys_elevator.getPosition() && sys_elevator.getPosition() <= 0.5-setPoint;
        } else if (findingZero) {
            return sys_elevator.getPosition() == 0;
        } else {
            overridden = true;
            return true;
        }
    }

    private double getRetractPos() {
        if (sys_elevator.getPrevPos() == Constants.kElevator.kToMidRung || sys_elevator.getPrevPos() == Constants.kElevator.kToLowRung) {
            return Constants.kElevator.kRetractToBar;
        } else {
            return Constants.kElevator.kRetractToMin;
        }
    }
}