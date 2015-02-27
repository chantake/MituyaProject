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
package com.chantake.MituyaProject.World;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import org.bukkit.ChatColor;

import java.util.Map;

/**
 *
 * @author fumitti
 */
public class Jackpot {

    public static int LimitJackpot1 = 15000000;//FINALでもよし？百万
    public static int jackpot2;
    public static int LimitJackpot2 = (new java.util.Random().nextInt(8) + 1) * 250000;//貯め

    public static void addjackpot(int add, MituyaProject plugin) {
        if (LimitJackpot1 < Parameter328.jackpot) {
            Parameter328.jackpot = LimitJackpot1;
            int charge = new java.util.Random().nextInt(8) + 1;
            String Chance = "";
            switch (charge) {
                case 1:
                case 2:
                case 3:
                    Chance = "!!!";
                    break;
                case 4:
                case 5:
                case 6:
                    Chance = "!!";
                    break;
                case 7:
                case 8:
                case 9:
                    Chance = "!";
                    break;
            }
            LimitJackpot2 = charge * 250000;
            plugin.broadcastMessage(ChatColor.GOLD.toString() + "JackPot", ChatColor.YELLOW + "BBのストックが貯まりました" + Chance + "これ以降はRBストックへ加算します" + Chance);
        } else if (LimitJackpot1 == Parameter328.jackpot) {
            addjackpot2(add, plugin);
        } else {
            Parameter328.jackpot += add;
        }
    }

    public static String checkJP() {
        int charge = (int)(((float)(jackpot2) / ((float)(LimitJackpot2))) * 10);
        String Chance = "";
        switch (charge) {
            case 0:
                Chance = "道のりは長い...";
                break;
            case 1:
                Chance = "道のりは長い..";
                break;
            case 2:
                Chance = "道のりは長い.";
                break;
            case 3:
                Chance = "道のりは長い";
                break;
            case 4:
                Chance = "まぁまぁかな？";
                break;
            case 5:
                Chance = "まぁまぁかな？";
                break;
            case 6:
                Chance = "まぁまぁかな？";
                break;
            case 7:
                Chance = "熱いかも!";
                break;
            case 8:
                Chance = "熱いかも！";
                break;
            case 9:
                Chance = "爆発寸前！？";
                break;
        }
        return "BBStock:" + Parameter328.jackpot + " RBStock:" + Chance;
    }

    private static void addjackpot2(int add, final MituyaProject plugin) {
        jackpot2 += add;
        if (LimitJackpot2 <= jackpot2) {
            jackpot2 = 0;
            int charge = new java.util.Random().nextInt(8) + 1;
            String Chance = "";
            switch (charge) {
                case 1:
                case 2:
                case 3:
                    Chance = "!!!";
                    break;
                case 4:
                case 5:
                case 6:
                    Chance = "!!";
                    break;
                case 7:
                case 8:
                case 9:
                    Chance = "!";
                    break;
            }
            int chanceint = LimitJackpot2 / 250000;
            LimitJackpot2 = charge * 250000;
            int onlineusers = plugin.getServer().getOnlinePlayers().size();
            if (onlineusers != 0) {
                final int BonusMine = new java.util.Random().nextInt(900000) + 100000 * chanceint * (int)((plugin.getServer().getOnlinePlayers().size() * 0.5) + 1);
                final int onesmine = BonusMine / onlineusers;
                plugin.broadcastMessage(ChatColor.GOLD.toString() + "JackPot", ChatColor.YELLOW + "レギュラーボーナス放出" + Chance + " ログイン者全員で山分けです" + Chance + "");
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.broadcastMessage(ChatColor.GOLD.toString() + "JackPot", ChatColor.YELLOW + "今回の全放出量は" + BonusMine + "Mineです！"), 5 * 20L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.broadcastMessage(ChatColor.GOLD.toString() + "JackPot", ChatColor.YELLOW + "今回の一人あたりの放出量は" + onesmine + "Mineです！");
                    for (Map.Entry<String, PlayerInstance> entry : plugin.getInstanceManager().getPlayers().entrySet()) {
                        PlayerInstance playerInstance = entry.getValue();
                        playerInstance.gainMine(onesmine);
                    }
                }, 15 * 20L);
            }
        }
    }
}
