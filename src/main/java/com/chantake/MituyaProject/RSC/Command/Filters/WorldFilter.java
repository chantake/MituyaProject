package com.chantake.MituyaProject.RSC.Command.Filters;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import com.chantake.MituyaProject.RSC.Command.CommandUtils;
import com.chantake.MituyaProject.RSC.RedstoneChips;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tal Eisenberg
 */
public class WorldFilter implements ChipFilter {
    public World world;

    @Override
    public void parse(CommandSender sender, String[] string) throws IllegalArgumentException {
        if (string.length == 1) {
            String sworld = string[0];
            if (sworld.equalsIgnoreCase("this")) {
                Player p = CommandUtils.enforceIsPlayer(sender, false);
                if (p == null) throw new IllegalArgumentException("Command sender must be a player when using `this`.");
                else world = p.getWorld();
            } else {
                world = RedstoneChips.inst().getServer().getWorld(string[0]);
            }

            if (world == null)
                throw new IllegalArgumentException("Unknown world: " + string[0]);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : string) sb.append(s);
            throw new IllegalArgumentException("Bad world filter: " + sb.toString() + ". Expecting 'world: <world-name>'.");
        }
    }

    @Override
    public Collection<Chip> filter(Collection<Chip> circuits) {
        List<Chip> filtered = circuits.stream().filter(chip -> chip.world.getName().equals(world.getName())).collect(Collectors.toList());

        return filtered;
    }
}
