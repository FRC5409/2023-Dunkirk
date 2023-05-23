package frc.robot.commands.Auto;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.kDriveTrain.kSide;
import frc.robot.subsystems.DriveTrain;

public class Auto extends SequentialCommandGroup {

    public Auto(DriveTrain drive) {

        addCommands(
            Commands.runOnce(() -> drive.setNeutralMode(NeutralMode.Coast)),
            Commands.run(() -> drive.arcadeDrive(1, 0), drive).withTimeout(1.9),
            Commands.runOnce(() -> drive.arcadeDrive(0, 0), drive),

            new WaitCommand(0.3),

            Commands.runOnce(() -> drive.setNeutralSide(kSide.kLeft, NeutralMode.Brake)).ignoringDisable(true),

            new WaitCommand(0.2).ignoringDisable(true),

            Commands.runOnce(() -> drive.setNeutralMode(NeutralMode.Coast)).ignoringDisable(true)
        );
    }
}
