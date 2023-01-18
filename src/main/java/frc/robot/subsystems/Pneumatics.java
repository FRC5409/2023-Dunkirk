package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class Pneumatics extends SubsystemBase {

    private Compressor m_compressor;
    private final ShuffleboardTab sb_pneumaticsTab;
    private final GenericEntry nt_compressorEnabled;
    private final GenericEntry nt_pressure;
    private final GenericEntry nt_current;

    /**
     * Pneumatics control subsystem
     * 
     * https://docs.wpilib.org/en/stable/docs/software/hardware-apis/pneumatics/pneumatics.html
     * https://revrobotics.ca/rev-11-1107/
     * 
     * Pneumatics code crash error:
     * https://docs.wpilib.org/en/stable/docs/yearly-overview/known-issues.html#code-crash-when-initializing-a-ph-pcm-related-device
     */
    public Pneumatics() {
        m_compressor = new Compressor(Constants.kPneumatics.kHubModuleID, Constants.kPneumatics.kPneumaticsModuleType);
        enable();

        // Shuffleboard
        sb_pneumaticsTab = Shuffleboard.getTab("Compressor");
        nt_compressorEnabled = sb_pneumaticsTab.add("Enabled", getEnabled())
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .getEntry();
        nt_pressure = sb_pneumaticsTab.add("Pressure", getPressure()).getEntry();
        nt_current = sb_pneumaticsTab.add("Current", getCurrent()).getEntry();
        
    }

    public void enable() {
        m_compressor.enableAnalog(Constants.kPneumatics.kMinPressure, Constants.kPneumatics.kMaxPressure);
    }

    public void disable() {
        m_compressor.disable();
    }

    public boolean getEnabled() {
        return m_compressor.isEnabled();
    }

    public boolean getPressureSwitchValue() {
        return m_compressor.getPressureSwitchValue();
    }

    public double getCurrent() {
        return m_compressor.getCurrent();
    }

    public double getPressure() {
        return m_compressor.getPressure();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        nt_pressure.setDouble(getPressure());
        nt_current.setDouble(getCurrent());

        // Shuffleboard compressor control
        boolean userEnabledCompressor = nt_compressorEnabled.getBoolean(getEnabled());

        if (userEnabledCompressor && !getEnabled()) {
            // enabled from shuffleboard
            enable();
        } else if (!userEnabledCompressor && getEnabled()) {
            // disabled from shuffleboard
            disable();
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation

        nt_pressure.setDouble(getPressure());
        nt_current.setDouble(getCurrent());

        // Shuffleboard compressor control
        boolean userEnabledCompressor = nt_compressorEnabled.getBoolean(getEnabled());

        if (userEnabledCompressor && !getEnabled()) {
            // enabled from shuffleboard
            enable();
        } else if (!userEnabledCompressor && getEnabled()) {
            // disabled from shuffleboard
            disable();
        }
    }

}