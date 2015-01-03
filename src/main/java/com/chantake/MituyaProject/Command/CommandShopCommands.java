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

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class CommandShopCommands {

    @Command(aliases = {"buy"}, usage = "", desc = "アイテムを買います", help = "/shop buy id amount",
            flags = "ia", min = 0, max = -1)
    @CommandPermissions({"mituya.shop.buy"})
    public static void buy(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        String id = null;
        if (message.argsLength() > 0) {
            id = message.getString(0);
        }
        int amount = 1;
        if (message.argsLength() > 1) {
            amount = message.getInteger(1);
        }
        if (message.hasFlag('i')) {
            id = message.getFlag('i');
        }
        if (message.hasFlag('a')) {
            amount = message.getFlagInteger('a');
        }

        if (id == null) {
            player.sendAttention("idもしくはアイテム名を入力してください");
        } else {
            plugin.getCommandShopManager().buy(id, amount, player, players);
        }
    }

    @Command(aliases = {"sell", "sl", "売る", "うる", "せｌｌ"}, usage = "", desc = "アイテムを売ります", help = "/shop sell id amount",
            flags = "ia", min = 0, max = -1)
    @CommandPermissions({"mituya.shop.sell"})
    public static void sell(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        String id = null;
        if (message.argsLength() > 0) {
            id = message.getString(0);
        }
        int amount = 1;
        if (message.argsLength() > 1) {
            if (message.getString(1).equalsIgnoreCase("all")) {
                amount = 2304;
            } else {
                amount = message.getInteger(1);
            }
        }
        if (message.hasFlag('i')) {
            id = message.getFlag('i');
        }
        if (message.hasFlag('a')) {
            amount = message.getFlagInteger('a');
        }

        if (id == null) {
            player.sendAttention("idもしくはアイテム名を入力してください");
        } else {
            plugin.getCommandShopManager().sell(id, amount, player, players);
        }
    }

    @Command(aliases = {"price", "ｐりせ", "価格", "かかく", "ねだん"}, usage = "", desc = "アイテムの価格を調べます", help = "/shop price id",
            flags = "i", min = 0, max = -1)
    @CommandPermissions({"mituya.shop.price"})
    public static void price(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        String id = null;
        if (message.argsLength() > 0) {
            id = message.getString(0);
        }
        if (message.hasFlag('i')) {
            id = message.getFlag('i');
        }
        if (id == null) {
            player.sendAttention("idもしくはアイテム名を入力してください");
        } else {
            plugin.getCommandShopManager().price(id, player);
        }
    }

    @Command(aliases = {"set", "せｔ", "設定"}, usage = "", desc = "アイテムの価格設定します", help = "/shop set id -b",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.shop.set"})
    public static void set(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            boolean buy_or_sell;
            switch (message.getString(0)) {
                case "buy":
                    buy_or_sell = true;
                    break;
                case "sell":
                    buy_or_sell = false;
                    break;
                default:
                    player.sendAttention("/shop set <buy/sell> id <stop/price>");
                    return;
            }
            if (message.argsLength() > 2) {
                if (message.getString(2).equals("stop")) {
                } else {
                    int id = message.getInteger(1);
                    int mine = message.getInteger(2);
                }
            }
        }
        player.sendAttention("/shop set <buy/sell> id <stop/price>");
    }
}
