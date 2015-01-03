package com.chantake.MituyaProject.RSC.Session;

import com.chantake.MituyaProject.Command.RSCCommands;
import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * A Tool that sends chip info messages to its user.
 *
 * @author Tal Eisenberg
 */
public class ChipProbe extends Tool {

    /**
     * Prints a chip block info. When block points to a chip pin, the player receives an /rcpin message of this pin. When block points to an activation block,
     * debug mode is toggled for this player. When block points to any other structure block the chip info is sent.
     *
     * @param block Queried block.
     */
    @Override
    public void use(Block block) {
        Player player = session.getPlayer();
        try {
            RSCCommands.printPinInfo(block, player, session.getRSC());
        }
        catch (IllegalArgumentException ie) {
            // not probing a pin
            Circuit c = session.getRSC().getCircuitManager().getCircuitByStructureBlock(block.getLocation());

            if (c != null) {
                if (c.activationBlock.equals(block.getLocation())) {
                    player.performCommand("rcdebug");
                } else {
                    RSCCommands.printCircuitInfo(player, c, session.getRSC());
                }
            }

        }
    }
}
