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

        try {
            m_compressor = new Compressor(Constants.kPneumatics.kHubModuleID, Constants.kPneumatics.kPneumaticsModuleType);

            enable();
        } catch (NullPointerException exception) {
            DriverStation.reportError("Error creating Compressor", exception.getStackTrace());
        }

        // Shuffleboard
        sb_pneumaticsTab = Shuffleboard.getTab("Compressor");
        nt_compressorEnabled = sb_pneumaticsTab.add("Enabled", getEnabled())
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .getEntry();
        nt_pressure = sb_pneumaticsTab.add("Pressure", getPressure()).getEntry();
        nt_current = sb_pneumaticsTab.add("Current", getCurrent()).getEntry();
        
    }

    public void enable() {
        try {
            m_compressor.enableAnalog(Constants.kPneumatics.kMinPressure, Constants.kPneumatics.kMaxPressure);
        } catch (NullPointerException exception) {
            DriverStation.reportError("Compressor is null", exception.getStackTrace());
        }
    }

    public void disable() {
        try {
            m_compressor.disable();
        } catch (NullPointerException exception) {
            DriverStation.reportError("Compressor are null", exception.getStackTrace());
        }
    }

    public boolean getEnabled() {
        try {
            return m_compressor.enabled();
        } catch (NullPointerException exception) {
            DriverStation.reportError("Compressor is null", exception.getStackTrace());
            return false;
        }
    }

    public boolean getPressureSwitchValue() {
        try {
            return m_compressor.getPressureSwitchValue();
        } catch (NullPointerException exception) {
            DriverStation.reportError("Compressor are null", exception.getStackTrace());
            return false;
        }
    }

    public double getCurrent() {
        try {
            return m_compressor.getCurrent();
        } catch (NullPointerException exception) {
            DriverStation.reportError("Compressor are null", exception.getStackTrace());
            return 0;
        }
    }

    public double getPressure() {
        try {
            return m_compressor.getPressure();
        } catch (NullPointerException exception) {
            DriverStation.reportError("Compressor are null", exception.getStackTrace());
            return 0;
        }
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