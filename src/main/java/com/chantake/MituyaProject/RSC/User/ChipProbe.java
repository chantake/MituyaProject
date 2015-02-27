package com.chantake.MituyaProject.RSC.User;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import com.chantake.MituyaProject.RSC.Chip.ChipCollection;
import com.chantake.MituyaProject.RSC.Chip.ChipFactory.MaybeChip;
import com.chantake.MituyaProject.RSC.Command.RCinfo;
import com.chantake.MituyaProject.RSC.Command.RCpin;
import com.chantake.MituyaProject.RSC.RCPrefs;
import com.chantake.MituyaProject.RSC.RedstoneChips;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * A Tool that sends chip info messages to its user.
 *
 * @author Tal Eisenberg
 */
public class ChipProbe extends Tool {

    /**
     * Prints a chip block info.
     * When block points to a chip pin, the player receives an /rcpin message of this pin.
     * When block points to an activation block, debug mode is toggled for this player.
     * When block points to any other structure block the chip info is sent.
     * When block is a sign block of an inactive chip the chip will be activated.
     *
     * @param block Queried block.
     */
    @Override
    public void use(Block block) {
        Player player = session.getPlayer();
        try {
            RCpin.printPinInfo(block, player);
        } catch (IllegalArgumentException ie) {
            // not probing a pin
            ChipCollection chips = RedstoneChips.inst().chipManager().getAllChips();
            Chip c = chips.getByStructureBlock(block.getLocation());

            if (c != null) {
                if (c.activationBlock.equals(block.getLocation()))
                    player.performCommand("rcdebug");
                else RCinfo.printCircuitInfo(player, c);
            } else {
                // try to activate
                MaybeChip m = RedstoneChips.inst().chipManager().maybeCreateAndActivateChip(block, player, -1);
                if (m == MaybeChip.ChipError)
                    player.sendMessage(RCPrefs.getErrorColor() + m.getError());
            }

        }
    }
}
