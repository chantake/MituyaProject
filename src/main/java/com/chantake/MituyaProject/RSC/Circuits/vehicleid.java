package com.chantake.MituyaProject.RSC.Circuits;

import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.vehicle.VehicleMoveEvent;

/**
 *
 * @author Tal Eisenberg
 */
public class vehicleid extends Circuit {

    private final int resetPin = 1;
    private final int disablePin = 0;
    private boolean pinDisabled = false;
    private int lastInterface = -1;

    @Override
    public void inputChange(int inIdx, boolean state) {
        if (inIdx == resetPin && state) {
            for (int i = 0; i < outputs.length; i++) {
                sendOutput(i, false);
            }
        } else if (inIdx == disablePin) {
            pinDisabled = state;
            if (pinDisabled) {
                for (int i = 0; i < outputs.length; i++) {
                    sendOutput(i, false);
                }
                BuildInCircuitsListener.deregisterVehicleidCircuit(this);
            } else {
                BuildInCircuitsListener.registerVehicleidCircuit(this);
            }
        }
    }

    @Override
    protected boolean init(CommandSender sender, String[] args) {
        if (outputs.length == 0 || interfaceBlocks.length == 0) {
            error(sender, "Expecting at least 2 output pins and at least 1 interface block.");
            return false;
        }

        BuildInCircuitsListener.registerVehicleidCircuit(this);
        return true;
    }

    void onVehicleMove(VehicleMoveEvent event) {
        if (pinDisabled) {
            return;
        }

        Location to = event.getTo();
        boolean found = false;

        for (int i = 0; i < interfaceBlocks.length; i++) {
            Location in = interfaceBlocks[i].getLocation();
            if (to.getBlockX() == in.getBlockX() && to.getBlockY() - in.getBlockY() <= 1 && to.getBlockZ() == in.getBlockZ()) {
                found = true;
                if (i != lastInterface) {
                    int vid = event.getVehicle().getEntityId();
                    if (hasDebuggers()) {
                        debug("Vehicle " + vid + " detected at interface block " + i);
                    }

                    sendInt(1, outputs.length - 1, vid);
                    sendOutput(0, true);
                    sendOutput(0, false);

                    lastInterface = i;
                    return;
                }
            }
        }

        if (!found) {
            lastInterface = -1;
        }
        // no match

    }

    @Override
    public void circuitShutdown() {
        BuildInCircuitsListener.deregisterVehicleidCircuit(this);
    }
}
