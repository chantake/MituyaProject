package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import com.chantake.MituyaProject.RSC.Chip.ChipCollection;
import com.chantake.MituyaProject.RSC.Chip.IO.InputPin;
import com.chantake.MituyaProject.RSC.Chip.IO.InterfaceBlock;
import com.chantake.MituyaProject.RSC.Chip.IO.OutputPin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tal Eisenberg
 */
public class RCpin extends RCCommand {

    public static void printPinInfo(Block pinBlock, CommandSender sender) {
        com.chantake.MituyaProject.RSC.RedstoneChips rc = com.chantake.MituyaProject.RSC.RedstoneChips.inst();

        boolean success = false;
        ChipCollection allChips = rc.chipManager().getAllChips();

        List<InputPin> inputList = allChips.getInputPinBySource(pinBlock.getLocation());

        if (inputList != null) {
            printInputInfo(sender, inputList);
            success = true;
        }

        InputPin input = allChips.getInputPin(pinBlock.getLocation());
        if (input != null) {
            List<InputPin> i = new ArrayList<>();
            i.add(input);
            printInputInfo(sender, i);
            success = true;
        }

        List<OutputPin> outputList = allChips.getOutputPinByOutputBlock(pinBlock.getLocation());

        if (outputList != null) {
            printOutputInfo(sender, outputList);
            success = true;
        }

        OutputPin o = allChips.getOutputPin(pinBlock.getLocation());
        if (o != null) {
            List<OutputPin> list = new ArrayList<>();
            list.add(o);
            printOutputInfo(sender, list);
            success = true;
        }

        Chip c = allChips.getByStructureBlock(pinBlock.getLocation());
        if (c != null && c.interfaceBlocks != null) {

            InterfaceBlock i = null;
            Location pinLoc = pinBlock.getLocation();

            for (InterfaceBlock bl : c.interfaceBlocks) {
                if (bl.getLocation().equals(pinLoc)) {
                    i = bl;
                    break;
                }
            }

            if (i != null) {
                printInterfaceInfo(sender, i);
                success = true;
            }
        }

        if (!success) throw new IllegalArgumentException("You must point at a chip io block.");
    }

    private static void printInputInfo(CommandSender sender, List<InputPin> inputList) {
        for (InputPin io : inputList) {
            Chip c = io.getChip();
            int i = io.getIndex();
            info(sender, c.toString() + ": " + ChatColor.WHITE + "input pin "
                    + i + " - " + (c.circuit.inputs[i] ? ChatColor.RED + "on" : ChatColor.WHITE + "off"));
        }
    }

    private static void printOutputInfo(CommandSender sender, List<OutputPin> outputList) {
        for (OutputPin o : outputList) {
            Chip c = o.getChip();
            int i = o.getIndex();
            info(sender, c.toString() + ": " + ChatColor.YELLOW + "output pin "
                    + i + " - " + (c.circuit.outputs[i] ? ChatColor.RED + "on" : ChatColor.WHITE + "off"));
        }
    }

    private static void printInterfaceInfo(CommandSender sender, InterfaceBlock in) {
        Chip c = in.getChip();
        int i = in.getIndex();
        info(sender, c.toString() + ": " + ChatColor.YELLOW + "interface block " + i + ".");
    }

    @Override
    public boolean isPlayerRequired() {
        return true;
    }

    @Override
    public void run(CommandSender sender, Command command, String label, String[] args) {
        Block target = CommandUtils.targetBlock((Player) sender);
        try {
            printPinInfo(target, sender);
        } catch (IllegalArgumentException e) {
            error(sender, e.getMessage());
        }
    }
}
