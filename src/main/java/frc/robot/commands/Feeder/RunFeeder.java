package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;

public class RunFeeder extends CommandBase {

    private final Feeder m_feeder;

    public RunFeeder(Feeder feeder) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_feeder = feeder;
        
        addRequirements(m_feeder);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_feeder.feed();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_feeder.stopMotor();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
