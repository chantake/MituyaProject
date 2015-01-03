package com.chantake.MituyaProject.RSC.Circuits;

import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import com.chantake.MituyaProject.RSC.Circuit.CircuitIndex;
import com.chantake.MituyaProject.RSC.RedstoneChips;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tal Eisenberg
 */
public class BuildInCircuits implements CircuitIndex {

    @Override
    public Class<? extends Circuit>[] getCircuitClasses() {
        return new Class[]{adder.class, and.class, clock.class, counter.class, demultiplexer.class, divider.class, flipflop.class,
            multiplexer.class, multiplier.class, or.class, pisoregister.class, print.class, random.class, receiver.class,
            shiftregister.class, transmitter.class, xor.class, decoder.class, encoder.class, pixel.class, pulse.class, not.class,
            synth.class, srnor.class, terminal.class, router.class, ringcounter.class, iptransmitter.class, ipreceiver.class,
            comparator.class, delay.class, repeater.class, nand.class, nor.class, xnor.class, segdriver.class, dregister.class,
            sram.class, bintobcd.class, display.class, burst.class, photocell.class, pirsensor.class, rangefinder.class, daytime.class, slotinput.class,
            beacon.class, spark.class, vehicleid.class, playerid.class};
    }

    @Override
    public void onRedstoneChipsEnable(RedstoneChips rc) {
        rc.getPrefs().registerCircuitPreference(iptransmitter.class, "ports", "25600..25699");
        rc.getServer().getPluginManager().registerEvents(new BuildInCircuitsListener(), rc.getPlugin());
    }

    @Override
    public String getIndexName() {
        return "BuildIn Circuits";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
