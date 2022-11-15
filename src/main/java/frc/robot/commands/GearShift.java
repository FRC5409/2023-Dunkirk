package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class GearShift extends CommandBase {

    private final DriveTrain m_driveTrain;
    private boolean m_gearShifted;

    public GearShift(DriveTrain driveTrain) {

        m_driveTrain = driveTrain;
        m_gearShifted = false;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveTrain);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_gearShifted = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (! m_gearShifted)
            m_driveTrain.shiftGear();
            m_gearShifted = true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_gearShifted;
    }

}