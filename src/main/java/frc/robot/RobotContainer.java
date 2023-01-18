// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.rmi.ssl.SslRMIServerSocketFactory;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.IntakeBall;
import frc.robot.commands.Rotation;
import frc.robot.commands.ShooterSpeed;
import frc.robot.commands.ToggleGear;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.Rotate;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

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
    private final Shooter sys_shooter;
    private final Feeder sys_feeder;
    private final Limelight sys_limelight;
    private final Rotate sys_rotate;

    // Controller
    private final XboxController sys_controller;
    private final JoystickButton but_main_A, but_main_B, but_main_X, but_main_Y, but_main_LBumper, but_main_RBumper,
        but_main_LAnalog, but_main_RAnalog, but_main_Back, but_main_Start;

    // Commands
    private final DefaultDrive cmd_defaultDrive;
    private final ToggleGear cmd_toggleGear;
    private final ExampleCommand cmd_example;
    private final IntakeBall cmd_intakeBall;
    private final ShooterSpeed cmd_shooterSpeed;
    private final Rotation cmd_rotation;

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {

        // Subsystems
        sys_driveTrain = new DriveTrain();
        sys_gyro = new Gyro();
        sys_pneumatics = new Pneumatics();
        sys_example = new ExampleSubsystem();
        sys_intake = new Intake();
        sys_shooter = new Shooter();
        sys_feeder = new Feeder();
        sys_limelight = new Limelight();
        sys_rotate = new Rotate();
        
        // Controller
        sys_controller = new XboxController(0);
        but_main_A = new JoystickButton(sys_controller, XboxController.Button.kA.value);
        but_main_B = new JoystickButton(sys_controller, XboxController.Button.kB.value);
        but_main_X = new JoystickButton(sys_controller, XboxController.Button.kX.value);
        but_main_Y = new JoystickButton(sys_controller, XboxController.Button.kY.value);
        but_main_LBumper = new JoystickButton(sys_controller, XboxController.Button.kLeftBumper.value);
        but_main_RBumper = new JoystickButton(sys_controller, XboxController.Button.kRightBumper.value);
        but_main_LAnalog = new JoystickButton(sys_controller, XboxController.Button.kLeftStick.value);
        but_main_RAnalog = new JoystickButton(sys_controller, XboxController.Button.kRightStick.value);
        but_main_Back = new JoystickButton(sys_controller, XboxController.Button.kBack.value);
        but_main_Start = new JoystickButton(sys_controller, XboxController.Button.kStart.value);

        // Commands
        cmd_defaultDrive = new DefaultDrive(sys_driveTrain, sys_controller);
        cmd_toggleGear = new ToggleGear(sys_driveTrain);
        cmd_example = new ExampleCommand(sys_example);
        cmd_intakeBall = new IntakeBall(sys_intake);
        cmd_shooterSpeed = new ShooterSpeed(sys_shooter, sys_controller, sys_feeder);
        cmd_rotation = new Rotation(sys_rotate);

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
      //  but_main_A.whenPressed(() -> sys_pneumatics.enable());
      //  but_main_B.whenPressed(() -> sys_pneumatics.disable());        


       // changing
        but_main_X.whileTrue(cmd_intakeBall);
        but_main_RBumper.onTrue(cmd_toggleGear);
        but_main_Y.whileTrue(cmd_rotation);

        but_main_A.onTrue(cmd_defaultDrive);


        but_main_LBumper.onTrue(cmd_shooterSpeed);

      


        
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
