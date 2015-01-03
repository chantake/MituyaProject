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
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.*;

/**
 *
 * @author chantake
 */
public class PluginCommands {

// <editor-fold defaultstate="collapsed" desc="plugin">
    @Command(aliases = {"plugin", "pg"}, usage = "", desc = "plugin command",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.plugin"})
    public static void pluginBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, InvalidPluginException, InvalidDescriptionException, UnknownDependencyException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            switch (message.getString(0)) {
                case "list":
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(ChatColor.AQUA).append("プラグインマネージャー").append(ChatColor.WHITE).append("] プラグインリスト：");
                    for (Plugin pg : plugin.getServer().getPluginManager().getPlugins()) {
                        if (pg.isEnabled()) {
                            sb.append(ChatColor.BLUE);
                        } else {
                            sb.append(ChatColor.RED);
                        }
                        sb.append(pg.getDescription().getFullName()).append(", ");
                    }
                    players.sendMessage(sb.toString());
                    break;
                case "enable":
                    if (message.argsLength() > 1) {
                        Plugin pg = plugin.getServer().getPluginManager().getPlugin(message.getString(1));
                        if (pg != null && pg.isEnabled()) {//有効だった場合
                            player.sendInfo(ChatColor.AQUA + "プラグインマネージャー", ChatColor.RED + "既に有効です");
                        } else {
                            File file = new File("plugins", message.getString(1) + ".jar");
                            PluginManager manager = plugin.getServer().getPluginManager();
                            player.sendInfo(ChatColor.AQUA + "プラグインマネージャー", file.getName() + " をロードします");
                            Plugin loadPlugin = manager.loadPlugin(file);
                            player.sendInfo(ChatColor.AQUA + "プラグインマネージャー", "プラグイン:" + loadPlugin.getName() + " を有効にします");
                            plugin.getServer().getPluginManager().enablePlugin(loadPlugin);
                            player.sendInfo(ChatColor.AQUA + "プラグインマネージャー", ChatColor.YELLOW + "プラグインを有効にしました");
                        }
                    } else {
                        player.sendAttention("/plugin enable プラグイン名");
                    }
                    break;
                case "disable":
                    if (message.argsLength() > 1) {
                        PluginManager manager = plugin.getServer().getPluginManager();
                        Plugin pg = manager.getPlugin(message.getString(1));
                        if (pg == null) {
                            player.sendInfo(ChatColor.AQUA + "プラグインマネージャー", ChatColor.RED + "プラグインが見つかりません");
                        } else if (!pg.isEnabled()) {//無効だった場合
                            player.sendInfo(ChatColor.AQUA + "プラグインマネージャー", ChatColor.RED + "既に無効です");
                        } else {
                            manager.disablePlugin(pg);
                            player.sendInfo(ChatColor.AQUA + "プラグインマネージャー", ChatColor.YELLOW + "プラグインを無効にしました");
                        }
                    } else {
                        player.sendAttention("/plugin disable プラグイン名");
                    }
                    break;
                default:
                    player.sendAttention("/plugin list  読み込んでるプラグインを表示します(有効：青　無効：赤)");
                    player.sendAttention("/plugin enable プラグイン名　プラグインを有効にします");
                    player.sendAttention("/plugin disable プラグイン名　プラグインを無効にします");
                    break;
            }
        } else {
            player.sendAttention("/plugin list  読み込んでるプラグインを表示します(有効：青　無効：赤)");
            player.sendAttention("/plugin enable プラグイン名　プラグインを有効にします");
            player.sendAttention("/plugin disable プラグイン名　プラグインを無効にします");
        }
    }
// </editor-fold>
}
