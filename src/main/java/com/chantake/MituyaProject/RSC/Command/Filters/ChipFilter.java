package com.chantake.MituyaProject.RSC.Command.Filters;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import org.bukkit.command.CommandSender;

import java.util.Collection;

/**
 * @author Tal Eisenberg
 */
public interface ChipFilter {
    public abstract void parse(CommandSender s, String[] string) throws IllegalArgumentException;

    public abstract Collection<Chip> filter(Collection<Chip> circuits);
}
