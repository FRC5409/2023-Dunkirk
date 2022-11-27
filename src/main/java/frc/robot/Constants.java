// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.subsystems.elevator.Elevator;

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
        }

        public static class kCANCoder {
            public final static int kLeftCANCoder = 5;
            public final static int kRightCANCoder = 6;

            public final static double kCountsPerRevolution = 4096;
            // TODO: Find this value, this is a placeholder
            public final static double kWheelDiameterInch = 3.937008; // 100mm to in
            public final static double kSensorCoefficient = 2 * Math.PI / 4096.0;
            public final static String kUnitString = "rad";
            // public final static double kSensorCoefficient = (Math.PI * kWheelDiameterInch) / kCountsPerRevolution;
            // public final static String kUnitString = "in";
        }

        public static class Solenoids {
            public final static int kGearShiftLow = 8;
            public final static int kGearShiftHigh = 9;
        }
    }

    public static class kGyro {
        public final static int kPigeonCAN = 23;

        // Gyroscope mounting offset
        public final static int kMountPoseRoll = 0;
        public final static int kMountPosePitch = 0;
        public final static int kMountPoseYaw = 0;
    }

    public static class kPneumatics {
        public final static int kHubModuleID = 2;

        // TODO: Adjust values as needed
        public final static int kMinPressure = 90;
        public final static int kMaxPressure = 120;

        public final static PneumaticsModuleType kPneumaticsModuleType = PneumaticsModuleType.REVPH;
    }

    public static class kElevator {

        public final static int kLeftCAN = 20;
        public final static int kRightCAN = 21;

        public final static int kMagSwitchDIO = 8;

        public final static double kExtendToMax = 0;
        public final static double kRetracttoMin = 0;
    }
}
