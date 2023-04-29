package frc.robot.commands.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DriveTrain;

public class DefaultDrive extends CommandBase {

    // Subsystems & hardware
    private final DriveTrain m_driveTrain;
    private final CommandXboxController m_controller;

    // Values
    private double m_xSpeed;
    private double m_zRotation;

    /**
     * Default drive command
     * 
     * Controls:
     * Right trigger = acceleration
     * Left trigger = deceleration/backwards
     * Left stick = rotation (must be accelerating)
     * Right stick = spin (not accelerating)
     * 
     * @param driveTrain
     * @param controller
     */
    public DefaultDrive(DriveTrain driveTrain, CommandXboxController controller) {

        m_driveTrain = driveTrain;
        m_controller = controller;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveTrain);

        // Reset values
        m_xSpeed = 0;
        m_zRotation = 0;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        // Right trigger = accelerate
        // Left trigger = decelerate / accelerate backwards
        final double acceleration = m_controller.getRightTriggerAxis();
        final double deceleration = m_controller.getLeftTriggerAxis();

        m_xSpeed = acceleration - deceleration;

        // Left stick x controls rotation
        m_zRotation = m_controller.getLeftX();

        m_driveTrain.arcadeDrive(m_xSpeed, m_zRotation);

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