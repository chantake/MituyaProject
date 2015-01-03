package com.chantake.MituyaProject.RSC.Circuit.Filter;

import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import com.chantake.MituyaProject.RSC.RedstoneChips;
import java.util.Collection;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Tal Eisenberg
 */
public abstract class CircuitFilter {

    protected RedstoneChips rc;

    public CircuitFilter setPlugin(RedstoneChips rc) {
        this.rc = rc;
        return this;
    }

    public abstract void parse(CommandSender s, String[] string) throws IllegalArgumentException;

    public abstract Collection<Circuit> filter(Collection<Circuit> circuits);
}
