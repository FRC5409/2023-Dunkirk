// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.BallBackOff;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.IntakeBall;
import frc.robot.commands.ShooterSpeed;
import frc.robot.commands.ToggleGear;
import frc.robot.commands.Elevator.MoveElevator;
import frc.robot.commands.Elevator.ZeroElevator;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.MiddleRollers;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

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
    private final Pneumatics sys_pneumatics;
    private final ExampleSubsystem sys_example;
    private final Intake sys_intake;
    private final MiddleRollers sys_middleRollers;
    private final Shooter sys_shooter;
    private final Feeder sys_feeder;
    private final Limelight sys_limelight;
    private final Elevator sys_elevator;

    // Controller
    private final CommandXboxController c_joystick;

    // Commands
    private final DefaultDrive cmd_defaultDrive;
    private final ToggleGear cmd_toggleGear;
    private final ExampleCommand cmd_example;
    private final IntakeBall cmd_intakeBall;
    private final ShooterSpeed cmd_shooterSpeed;
    //private final BallBackOff cmd_ballBackOff;

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {

        // Subsystems
        sys_driveTrain = new DriveTrain();
        sys_gyro = new Gyro();
        sys_pneumatics = new Pneumatics();
        sys_example = new ExampleSubsystem();
        sys_intake = new Intake();
        sys_middleRollers = new MiddleRollers();
        sys_shooter = new Shooter();
        sys_feeder = new Feeder();
        sys_limelight = new Limelight();
        sys_elevator = new Elevator();
        
        // Controller
        c_joystick = new CommandXboxController(0);

        // Commands
        cmd_defaultDrive = new DefaultDrive(sys_driveTrain, c_joystick);
        cmd_toggleGear = new ToggleGear(sys_driveTrain);
        cmd_example = new ExampleCommand(sys_example);
        cmd_intakeBall = new IntakeBall(sys_intake, sys_middleRollers);
        cmd_shooterSpeed = new ShooterSpeed(sys_shooter, c_joystick, sys_feeder);

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
        c_joystick.x().whileTrue(cmd_intakeBall);
        c_joystick.rightBumper().onTrue(cmd_toggleGear);

        c_joystick.a().onTrue(Commands.runOnce(sys_pneumatics::enable));
        c_joystick.a().onTrue(Commands.runOnce(sys_pneumatics::disable));

        c_joystick.leftBumper().whileTrue(cmd_shooterSpeed);

        c_joystick.start().onTrue(Commands.runOnce(sys_elevator::toggleActiveState));

        c_joystick
            .povUp()
            .and(sys_elevator::getActiveState)
            .onTrue(new MoveElevator(sys_elevator, Constants.kElevator.kToMidRung));
        c_joystick
            .povLeft()
            .and(sys_elevator::getActiveState)
            .onTrue(new MoveElevator(sys_elevator, Constants.kElevator.kToLowRung));
        c_joystick
            .povDown()
            .and(sys_elevator::getActiveState)
            .onTrue(new MoveElevator(sys_elevator));
        c_joystick
            .povRight()
            .and(sys_elevator::getActiveState)
            .and(() -> !sys_elevator.getElevatorState())
            .onTrue(new ZeroElevator(sys_elevator));
        
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous (placeholder)
        return cmd_example;
    }
}
