package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;

public class MoveElevator extends CommandBase {

    private final Elevator sys_elevator;
    private final XboxController c_joystick;
    private static double setPoint;
    public boolean pidMove;
    private boolean isMoving;

    public MoveElevator(Elevator subsystem, XboxController joystick) {
        sys_elevator = subsystem;
        c_joystick = joystick;
        pidMove = false;
        isMoving = false;
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

        if (pidMove) {
            if (-5 <= c_joystick.getPOV(0) && c_joystick.getPOV(0) <= 5 && !isMoving) {
                setPoint = Constants.kElevator.kExtendToMax;
                sys_elevator.moveElevator(Constants.kElevator.kExtendToMax);
                isMoving = true;
                
            }
        
            if (185 <= c_joystick.getPOV(0) && c_joystick.getPOV(0) <= 5 && !isMoving) {
                setPoint = Constants.kElevator.kRetractToMin;
                sys_elevator.moveElevator(Constants.kElevator.kRetractToMin);
                isMoving = true;
            }
        } else {
            double zSpeedUp = c_joystick.getRightTriggerAxis();
            double zSpeedDown = c_joystick.getLeftTriggerAxis();

            double zSpeed = zSpeedUp - zSpeedDown;

            Elevator.m_left.set(zSpeed);
        }
                
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        
    }

    public void toggleMove() {
        this.pidMove = !pidMove;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (pidMove) {
            return 1-setPoint <= sys_elevator.getPosition() && sys_elevator.getPosition() <= 1-setPoint;
        } else {
            return false;
        }
    }

}