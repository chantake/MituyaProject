/*
 * MituyaProject
 * Copyright (C) 2011-2015 chantake <http://328mss.com/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.chantake.MituyaProject.Command;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class NPCCommands {

    //public static BasicHumanNpcList HumanNPCList;
// <editor-fold defaultstate="collapsed" desc="npc">
    @Command(aliases = {"npc"}, usage = "", desc = "npc command",
            flags = "", min = 0, max = -1)
    public static void npcBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        NPC npc = (NPC)players.getLocation().getWorld().spawnEntity(null, EntityType.PLAYER);
        /*
         if (message.getString(0).equals("create")) {
         if (message.argsLength() < 3) {
         return;
         }
         if (NPCCommands.HumanNPCList.get(message.getString(1)) != null) {
         player.sendAttention("そのIDは既にあります");
         return;
         }
         Location l = players.getLocation();
         BasicHumanNpc hnpc = NpcSpawner.SpawnBasicHumanNpc(message.getString(1), message.getString(2), players.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
         NPCCommands.HumanNPCList.put(message.getString(1), hnpc);
         ItemStack is = new ItemStack(Material.BOOKSHELF);
         is.setAmount(1);
         hnpc.getBukkitEntity().setItemInHand(is);
         }*/

    }
// </editor-fold>

}
