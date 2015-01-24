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

import com.chantake.MituyaProject.Exception.MituyaProjectException;
import com.chantake.MituyaProject.Gachapon.GachaponDataManager;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author ezura573
 */
public class EnchantmentCommands {

    @Command(aliases = {"enchantmentclear", "ec"}, usage = "", desc = "EnchantmentClear",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.enchantmentclear"})
    public static void gachaponBrush(CommandContext message, final MituyaProject plugin, final Player player, final PlayerInstance playerInstance) throws MituyaProjectException {
        final ItemStack is = player.getInventory().getItemInHand();

        if (is.getType() == Material.ENCHANTED_BOOK) {
            playerInstance.sendMessage(ChatColor.RED + "エンチャントブックのエンチャントは消せません。");
            return;
        }

        if (is == null || is.getType() == Material.AIR) {
            playerInstance.sendMessage(ChatColor.RED + "手にエンチャントを消したいアイテムを持って下さい。");
            return;
        }

        if (is.getEnchantments().size() < 1) {
            playerInstance.sendMessage(ChatColor.RED + "このアイテムはエンチャントが付いていません。");
            return;
        }

        String item_name = Tools.GetItemName(is);
        if (GachaponDataManager.isGachaponItemNameCoincident(item_name)) {
            playerInstance.sendMessage(ChatColor.RED + item_name + "のエンチャントを消すなんてとんでもない！！");
            return;
        }

        if (is.getDurability() > 0) {
            playerInstance.sendMessage(ChatColor.RED + "耐久値が減っているアイテムのエンチャントは消せません。");
            return;
        }

        playerInstance.sendMessage(ChatColor.YELLOW + "手に持っているアイテムのエンチャントを削除します。");
        playerInstance.sendMineYesNo(8000, new Runnable() {
            @Override
            public void run() {
                if (playerInstance.gainMine(-8000)) {
                    ItemStack new_is = new ItemStack(is.getTypeId());
                    player.setItemInHand(new_is);
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    playerInstance.sendMessage(ChatColor.AQUA + "エンチャントを削除しました。");
                } else {
                    playerInstance.sendMessage(ChatColor.RED + "Mineが不足しているためエンチャントを削除出来ませんでした。");
                }
            }
        });
    }
}
