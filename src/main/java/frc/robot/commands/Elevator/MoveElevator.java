package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class MoveElevator extends CommandBase {

    private final Elevator sys_elevator;
    private final XboxController c_joystick;
    private static double setPoint;
    public boolean isMoving;
    public boolean direction;

    public MoveElevator(Elevator subsystem, XboxController joystick, boolean direction) {
        sys_elevator = subsystem;
        c_joystick = joystick;
        isMoving = false;
        this.direction = direction;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(sys_elevator);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        isMoving = false;
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (!isMoving && direction) {
            moveUp();
        }
    
        if (!isMoving && !direction) {
            moveDown();
        }
                
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    public void moveUp() {
        setPoint = Constants.kElevator.kExtendToMax;
        sys_elevator.moveElevator(Constants.kElevator.kExtendToMax);
        isMoving = true;
    }

    public void moveDown() {
        setPoint = Constants.kElevator.kRetractToMin;
        sys_elevator.moveElevator(Constants.kElevator.kRetractToMin);
        isMoving = true;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return 1-setPoint <= sys_elevator.getPosition() && sys_elevator.getPosition() <= 1-setPoint;
    }

}