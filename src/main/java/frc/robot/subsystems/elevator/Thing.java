package frc.robot.subsystems.elevator;

import java.util.HashMap;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Thing extends SubsystemBase {

    private HashMap<String, Double> thing;


    public Thing() {
        if (thing.isEmpty()) {
            thing.put("a", 1.0);
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        thing.replace("a", 2.0);
        System.out.println(thing.get("a"));
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
        thing.replace("a", 2.0);
        System.out.println(thing.get("a"));
    }

}