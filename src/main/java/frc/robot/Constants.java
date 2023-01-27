// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class kDriveTrain {
        public static class kMotors {
            public final static int kLeft1CAN = 1;
            public final static int kLeft2CAN = 2;
            public final static int kRight1CAN = 3;
            public final static int kRight2CAN = 4;

            public final static int currentLimit = 90;
            public final static double rampRate = 0.5;
        }

        public static class kCANCoder {
            public final static int kLeftCANCoder = 5;
            public final static int kRightCANCoder = 6;

            public final static double kCountsPerRevolution = 4096;
            public final static double kSensorCoefficient = (Math.PI * kDriveTrain.kWheel.kWheelDiameter) / kCountsPerRevolution;
            public final static String kUnitString = "m";
        }

        public static class kWheel {
            public final static double kWheelDiameter = 0.09404; // m
            public final static double kWheelCircumference = Math.PI * kWheelDiameter; // m
        }

        public static class Solenoids {
            public final static int kGearShiftLow = 8;
            public final static int kGearShiftHigh = 9;
        }

        public static class kAiming {
            public final static double kTargetPlay = 5.5; // To be found
            public final static double kTargetSpeed = 0.5; // To be found
            public final static double kScanningSpeed = 0.6; // To be found
        }
    }

    public static class kGyro {
        public final static int kPigeonCAN = 23;

        // Gyroscope mounting offset
        public final static int kMountPoseRoll = 0;
        public final static int kMountPosePitch = 0;
        public final static int kMountPoseYaw = 0;
    }

    public static class kMiddleRollers {
        public final static int kRollerMotorID = 8;
        public final static double kRollersSpeed = 0.4;
        public final static double kRollersStopped = 0.0;
    }

    public static class kIntake {
        public static class kRollers {
            public final static int kIntakeMotorID = 11;
            public final static double kIntakeRollersSpeed = 0.4;
            public final static double kIntakeRollersStopped = 0.0;
        }

        public static class kDoubleSolenoids {
            public final static int kLeftFwdChannel = 4;
            public final static int kLeftBwdChannel = 5;
            public final static int kRightFwdChannel = 10;
            public final static int kRightBwdChannel = 11;
        }
    }

    public static class kPneumatics {
        public final static int kHubModuleID = 2;

        public final static int kMinPressure = 90;
        public final static int kMaxPressure = 120;

        public final static PneumaticsModuleType kPneumaticsModuleType = PneumaticsModuleType.REVPH;
    }

    public static class kShooter {
        public final static int leftMotID = 5;
        public final static int rightMotID = 8;
        public final static int ToFID = 11;

        public final static double shooterRPMPlay = 50;

        public final static int timeOutMs = 30;

        public static class kPID {
            public final static double kP = 0.42;
            public final static double kI = 0;
            public final static double kD = 0;
            public final static double kF = 0.0505;
        }

        // public final static double cargoIsThere = 30;

        public static class kShooterData {
            public final static double[] shooterDataX = {  15,   30,   45,   60,   75,   80,  105,  120,  135,  150};//distance to target
            public final static double[] shooterDataY = {1800, 1900, 2000, 2100, 2600, 3000, 3800, 4300, 4600, 5000};//speed to spin at
        }
    }

    public static class kFeeder {
        public final static int feederID = 9;

        public final static double feedSpeed = 0.4;
    }

    public static class kLimelight {
        public final static double mountAngle = 45;//TODO: get real value
        public final static double targetHeight = 140;// For 2022
        public final static double highTargetHeight = 140;//TODO: get real value for 2023
        public final static double lowTargetHeight = 70;//TODO: get real value for 2023
        public final static double heightOffFloor = 70;
    }

    public static class kElevator {

        public final static int kLeftCAN = 20;
        public final static int kRightCAN = 21;

        public final static int kMagSwitchDIO = 8;

        public final static double kRetractToMin = 0.0;
        public final static double kToMidRung = 104.0;
        public final static double kToLowRung = 55.0;
        public final static double kRetractToBar = 7.0;

        public final static double kP = 0.7;
        public final static double kI = 0.0;
        public final static double kD = 0.0;
        public final static double kF = 0.0;
    }
}
