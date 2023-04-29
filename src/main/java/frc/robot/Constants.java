// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static class kConfig {
                                               /*   Team 5409   */
        public static final int teamNumber           = 5409;  
    }                                          /*  The Chargers */

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
    }

    public static class kGyro {
        public final static int kPigeonCAN = 23;

        // Gyroscope mounting offset
        public final static int kMountPoseRoll = 0;
        public final static int kMountPosePitch = 0;
        public final static int kMountPoseYaw = 0;
    }

    public static class kShooter {
        public final static int leftMotID = 5;
        public final static int rightMotID = 8;
        public final static int ToFID = 11;

        public final static double shooterRPMPlay = 50;

        public final static int shooterPrepSpeed = 1000;

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

    public static class kTurret {

        public enum State {
            kScaning,
            kLocked,
            kLocking,
            kOff
        }

        public final static int CANID                    = -1;

        public final static int currentLimit             = 20;

        public final static int maxScanOutput            = 3;   //in Volts
        public final static int turretSpeed              = 50;  //bigger the number slower the movement

        public final static int lockingSpeed             = 4; //in volts

        public final static double kP                    = 0.05;
        public final static double kI                    = 0;
        public final static double kD                    = 0;

        public final static double encoderThreshold      = 0.1;
        public final static double targetingThreshold    = 1;

        public final static double maxPosition           = 1.76;// Encoder max position
        public final static int maxAngle                 = 90;  //   Angle max position
    }

    public static class kFeeder {
        public final static int feederID = 9;

        public final static double feedSpeed = 0.4;
    }

    public static class kIndexer {
        public final static int CANID = 8;

        public final static int currentLimit = 20;

        public final static double indexerSpeed = 0.4;
    }

    public static class kLimelight {
        public final static double mountAngle = 45;
        public final static double targetHeight = 140;
        public final static double heightOffFloor = 70;

        public final static int HORIZONTAL_FOV = 54;
        public final static int VERTICAL_FOV = 41;

    }
    
}
