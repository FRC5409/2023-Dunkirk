package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ToggleGear extends InstantCommand {

    private final DriveTrain m_driveTrain;
    private DoubleSolenoid.Value m_gearState;

    public ToggleGear(DriveTrain driveTrain) {

        m_driveTrain = driveTrain;
        m_gearState = m_driveTrain.getGear();
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

        System.out.println("reached here");
        m_gearState = m_driveTrain.getGear();
        switch (m_gearState) {
            case kForward:
                System.out.println("reached here4");
                m_driveTrain.changeGear(DoubleSolenoid.Value.kReverse);
                break;
            
            case kReverse:
                System.out.println("reached here 3");
                m_driveTrain.changeGear(DoubleSolenoid.Value.kForward);
                break;
        
            case kOff:
                System.out.println("reached here 5");
                m_driveTrain.changeGear(DoubleSolenoid.Value.kForward);
                break;
        }
    }

}