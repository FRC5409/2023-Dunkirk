package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;

public class runFeeder extends CommandBase {

    private final Feeder m_feeder;
    private final XboxController m_joystick;

    private ShuffleboardTab tab = Shuffleboard.getTab("Feeder");
    private NetworkTableEntry kP = tab.add("kP", 0).getEntry();
    private NetworkTableEntry kI = tab.add("kI", 0).getEntry();
    private NetworkTableEntry kD = tab.add("kD", 0).getEntry();
    private NetworkTableEntry kF = tab.add("kF", 0).getEntry();
    private NetworkTableEntry velocity = tab.add("Velocity: ", 0).getEntry();
    private NetworkTableEntry targetVelocity = tab.add("Target Velocity: ", 0).getEntry();

    public runFeeder(Feeder feeder, XboxController joystick) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_feeder = feeder;
        m_joystick = joystick;
        addRequirements(m_feeder);
        targetVelocity.setDouble(600);
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_feeder.configPIDF(kP.getDouble(0), kI.getDouble(0), kD.getDouble(0), kF.getDouble(0));
        m_feeder.feed();
        velocity.setDouble(m_feeder.getVelocity());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_feeder.stopFeeding();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_joystick.getAButtonReleased();
    }

}
