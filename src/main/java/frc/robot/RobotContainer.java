// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants.kTurret.ScanningDirection;
import frc.robot.Constants.kTurret.State;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.Indexer.IntakeCargo;
import frc.robot.commands.Shooter.FiringCommand;
import frc.robot.commands.Turret.LockOnTarget;
import frc.robot.commands.Turret.Scan;
import frc.robot.commands.Turret.TurretGoTo;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.LedMode;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...

    // Subsystems
    public final DriveTrain sys_driveTrain;
    private final Gyro sys_gyro;
    private final Shooter sys_shooter;
    private final Turret sys_turret;
    private final Feeder sys_feeder;
    private final Indexer sys_indexer;
    private final Limelight sys_limelight;

    // Controller
    private final CommandXboxController joystickMain;
    private final CommandXboxController joystickSecondary;
    private final CommandXboxController joystickTesting;

    // Commands
    private final DefaultDrive cmd_defaultDrive;

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {

        // Subsystems
        sys_driveTrain = new DriveTrain();
        sys_gyro = new Gyro();
        sys_shooter = new Shooter();
        sys_turret = new Turret();
        sys_feeder = new Feeder();
        sys_indexer = new Indexer();
        sys_limelight = new Limelight();
        
        // Controller
        joystickMain = new CommandXboxController(0);
        joystickSecondary = new CommandXboxController(1);
        joystickTesting = new CommandXboxController(2);

        // Commands
        cmd_defaultDrive = new DefaultDrive(sys_driveTrain, joystickMain);

        sys_driveTrain.setDefaultCommand(cmd_defaultDrive);

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

        /* Main Joystick Button Bindings */

        joystickMain.leftBumper().onTrue(
            new ConditionalCommand(
                //if it's not scanning then start scanning
                new RepeatCommand(
                    new Scan(sys_turret, sys_limelight)
                    .andThen(new LockOnTarget(sys_turret, sys_limelight))
                ),

                //if it's scanning then stop scanning and reset
                new TurretGoTo(sys_turret, 0)
                .alongWith(
                    Commands.runOnce(() -> sys_limelight.setLedMode(LedMode.kModeOff)),
                    Commands.runOnce(() -> sys_turret.setState(State.kOff))
                ),

                () -> !sys_turret.isBeingUsed()
            )
        );

        joystickMain.rightBumper()
            .and(() -> sys_turret.isBeingUsed()).whileTrue(
                new FiringCommand(sys_shooter, sys_turret, sys_feeder)
            );

        joystickMain.y()
            .whileTrue(new IntakeCargo(sys_indexer));

        joystickMain.povLeft()
            .onTrue(Commands.runOnce(() -> sys_turret.setScanningDir(ScanningDirection.kLeft)));

        joystickMain.povRight()
            .onTrue(Commands.runOnce(() -> sys_turret.setScanningDir(ScanningDirection.kRight)));

        /* Secondary Joystick Button Bindings */


        /* Testing Button Bindings */

        joystickTesting.leftBumper()
            .onTrue(new Scan(sys_turret, sys_limelight)
        );
    }

}
