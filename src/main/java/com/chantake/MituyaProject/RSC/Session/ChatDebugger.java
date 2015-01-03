package com.chantake.MituyaProject.RSC.Session;

import com.chantake.MituyaProject.RSC.BitSet.BitSetUtils;
import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A circuit debugger that uses chat messages.
 *
 * @author Tal Eisenberg
 */
class ChatDebugger extends Debugger {

    public ChatDebugger(UserSession session) {
        super(session);
    }

    @Override
    public void circuitDestroyed(Circuit c, CommandSender destroyer) {
        ChatColor ecolor = session.getRSC().getPrefs().getErrorColor();
        ChatColor dcolor = session.getRSC().getPrefs().getDebugColor();
        String dname;
        if (destroyer == null) {
            dname = "unknown cause";
        } else {
            dname = destroyer.getName();
        }

        debug(c, "The chip was " + ecolor + "deactivated " + dcolor + "by " + dname + " (@" + c.activationBlock.getX() + ","
                + c.activationBlock.getY() + "," + c.activationBlock.getZ() + ").");
    }

    @Override
    public void circuitDisabled(Circuit c) {
        debug(c, session.getRSC().getPrefs().getErrorColor() + "Chip is disabled.");
    }

    @Override
    public void circuitEnabled(Circuit c) {
        debug(c, "Chip is Enabled.");
    }

    @Override
    public void circuitMessage(Circuit c, String msg) {
        debug(c, msg);
    }

    @Override
    public void inputChanged(Circuit c, int idx, boolean state) {
        List<Flag> cflags = flags.get(c);
        if (cflags != null && cflags.contains(Flag.IO)) {
            try {
                BigInteger inputInt = BitSetUtils.bitSetToBigInt(c.getInputBits(), 0, c.inputs.length);
                String i = ChatColor.WHITE + BitSetUtils.bitSetToBinaryString(c.getInputBits(), 0, c.inputs.length) + " (0x"
                        + inputInt.toString(16) + ")";
                
                debug(c, "Input " + idx + " is " + (state ? "on" : "off") + ": " + i + ".");
            }
            catch (CloneNotSupportedException ex) {
                Logger.getLogger(ChatDebugger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void outputChanged(Circuit c, int idx, boolean state) {
        List<Flag> cflags = flags.get(c);
        if (cflags != null && cflags.contains(Flag.IO)) {

            try {
                BigInteger outputInt = BitSetUtils.bitSetToBigInt(c.getOutputBits(), 0, c.outputs.length);
                String o = ChatColor.YELLOW + BitSetUtils.bitSetToBinaryString(c.getOutputBits(), 0, c.outputs.length) + " (0x"
                        + outputInt.toString(16) + ")";
                
                debug(c, "Output " + idx + " is " + (state ? "on" : "off") + ": " + o + ".");
            }
            catch (CloneNotSupportedException ex) {
                Logger.getLogger(ChatDebugger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void debug(Circuit c, String msg) {
        Player p = session.getPlayer();
        if (p != null) {
            p.sendMessage(session.getRSC().getPrefs().getDebugColor() + c.getChipString() + ": " + msg);
        }
    }
}
