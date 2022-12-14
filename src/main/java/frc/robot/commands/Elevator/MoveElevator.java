package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class MoveElevator extends CommandBase {

    private final Elevator sys_elevator;
    private double setPoint;
    private boolean extending;
    private boolean findingZero;
    private boolean overridden;

    public MoveElevator(Elevator subsystem, double destination) {
        sys_elevator = subsystem;
        setPoint = destination;
        extending = true;
        overridden = false;
        findingZero = false;
        if (sys_elevator.getPrevPos() == Constants.kElevator.kToMidRung 
            || sys_elevator.getPrevPos() == Constants.kElevator.kToLowRung) {
            isFinished();
        }
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
    }

    public MoveElevator(Elevator subsystem) {
        sys_elevator = subsystem;
        extending = false;
        overridden = false;
        findingZero = false;
        if (sys_elevator.getPrevPos() == 0) {
            isFinished();
        }

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
    }

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
            setPoint = getRetractPos();
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
            if(!overridden) {
                sys_elevator.lockRatchet();
                sys_elevator.setElevatorState(false);
                sys_elevator.setPrevPos(setPoint);
            }
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (!findingZero && sys_elevator.getPrevPos() != Constants.kElevator.kToMidRung && extending
             || sys_elevator.getPrevPos() != 0 && !extending) {
            return 1-setPoint <= sys_elevator.getPosition() && sys_elevator.getPosition() <= 1-setPoint;
        } else if (findingZero) {
            return sys_elevator.getPosition() == 0;
        } else {
            overridden = true;
            return true;
        }
    }

    private double getRetractPos() {
        if (sys_elevator.getPrevPos() == Constants.kElevator.kToMidRung) {
            return Constants.kElevator.kRetractMid;
        } else {
            return Constants.kElevator.kRetractLow;
        }
    }

}