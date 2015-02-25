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
import com.chantake.MituyaProject.Exception.PlayerNotFoundException;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chantake
 */
public class TeleportCommands {

    @Command(aliases = {"tp"},
            usage = "[location/public/PlayerID]", desc = "Teleport",
            flags = "s", min = 0, max = -1)
    @CommandPermissions({"mituya.player.teleport"})
    public static void tpBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws MituyaProjectException {
        if (message.argsLength() > 0) {
            switch (message.getString(0)) {
                case "location":
                    if (!player.UseSaveLocation()) {
                        player.sendAttention("ロケーションがセーブされていません。  ロケーションセーブコマンドを使ってください。'/save location'.");
                    } else if (players.getWorld() != player.getLocation().getWorld()) {
                        player.sendAttention("ワールドが違います　ロケーションワールド：" + player.getLocation().getWorld().getName());
                    } else {
                        player.setTp();
                        player.sendProcessing("ロケーションにテレポート中");
                        player.teleport(player.getLocation());
                    }
                    break;
                case "harvest":
                    if (players.getWorld().equals(plugin.getWorldManager().getWorld("harvest"))) {
                        player.sendProcessing("元いたワールドにテレポート中");
                        player.teleport(player.getHarvest());
                    } else {
                        player.sendProcessing("Harvestワールドにテレポート中");
                        Location loc = players.getLocation().clone();
                        if (player.teleport(plugin.getWorldManager().getWorld("harvest").getSpawnLocation())) {
                            player.setHarvest(loc);
                        }
                    }
                    break;
                case "world":
                    /*if (!player.getMainWorldInvite()) {
                     player.sendAttention("招待がないとブロックの破壊などができません");
                     }*/
                    player.sendProcessing("メインワールドにテレポート中");
                    player.teleport(plugin.getWorldSpawn(plugin.getWorldManager().getWorld(Parameter328.MainWorld)));
                    break;
                case "newworld":
                case "new_world":
                    player.sendProcessing("新ワールドにテレポート中");
                    player.teleport(plugin.getWorldSpawn(plugin.getWorldManager().getWorld(Parameter328.SecondWorld)));
                    break;
                case "flat":
                case "superflat":
                case "sf":
                    player.sendProcessing("スーパーフラットワールドにテレポート中");
                    player.teleport(plugin.getWorldManager().getWorld(Parameter328.SuperFlat).getSpawnLocation());
                    break;
                case "public":
                    int tppint = player.getTpPublic();
                    String[] tppstring = {"拒否", "許可", "認証"};
                    tppint++;
                    if (tppint == 3) {
                        tppint = 0;
                    }
                    player.setTpPublic(tppint);
                    player.sendSuccess("テレポートのパブリック設定しました。 : " + tppstring[tppint]);
                    break;
                default:
                    final PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));
                    if (ins == null) {
                        throw new PlayerNotFoundException(message.getString(0));
                    }
                    Player cp = ins.getPlayer();
                    if (cp == null) {
                        throw new PlayerOfflineException(ins.getName());
                    } else if (cp.getWorld() != players.getWorld() && !player.hasPermission(Rank.GM)) {
                        player.sendAttention("プレーヤー " + ChatColor.YELLOW + ins.getName() + ChatColor.RED + " は他のワールド(" + cp.getWorld().getName() + ")にいます。");
                    } else if (cp.isFlying()) {
                        player.sendAttention("プレーヤー " + ChatColor.YELLOW + ins.getName() + ChatColor.RED + " は現在テレポートできない状態です。");
                        if (player.hasPermission(Rank.Admin)) {
                            player.sendAttention(ChatColor.YELLOW + " Admin権限でCreativeモードに変更したうえでテレポートします");
                            player.getPlayer().setGameMode(GameMode.CREATIVE);
                            player.teleport(cp.getLocation());
                            player.setTp();
                        }
                    } else if (ins.getTpPublic() == 0) {
                        String ms = ChatColor.RED + "プレーヤー " + ChatColor.YELLOW + ins.getName() + ChatColor.RED + " はテレポートをパブリックにしていません";
                        if (player.hasPermission(Rank.Moderator)) {
                            ms += ChatColor.YELLOW + " GM権限でスキップしました";
                            player.teleport(cp.getLocation());
                            player.setTp();
                        }
                        players.sendMessage(ms);
                    } else if (ins.getTpPublic() == 1) {
                        player.setTp();
                        player.sendProcessing(ins.getName() + " にテレポート中");
                        if (player.teleport(cp.getLocation())) {
                            if (!message.hasFlag('s') && !plugin.getInstanceManager().getHideManager().isHidePlayer(players)) {
                                ins.sendProcessing(players.getName() + " がテレポートしてきています...");
                            }
                        }
                    } else if (ins.getTpPublic() == 2) {
                        if (player.hasPermission(Rank.GM)) {
                            player.setTp();
                            player.sendProcessing(ins.getName() + " にテレポート中");
                            if (player.teleport(cp.getLocation())) {
                                if (!message.hasFlag('s') && !plugin.getInstanceManager().getHideManager().isHidePlayer(players)) {
                                    ins.sendProcessing(players.getName() + " がテレポートしてきています...");
                                }
                            }
                        } else {
                            TeleportYesNo(plugin, player, ins, message, players);
                        }
                    }
                    break;
            }
        } else {
            player.sendAttention("/tp location   ロケーションに移動します(/save locationで設定)");
            player.sendAttention("/tp world      メインワールドに移動します");
            player.sendAttention("/tp newworld   通常ワールドに移動します");
            player.sendAttention("/tp harvest    ハーベストに移動します");
            player.sendAttention("/tp superflat  スーパーフラットに移動します");
            player.sendAttention("/tp public     tpを許可するかどうかを設定します");
            player.sendAttention("/tp PlayerID   プレーヤーにtpします");
        }
    }

    @Command(aliases = {"tphere", "s"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.teleport.here"})
    public static void tphereBrush(CommandContext message, MituyaProject plugin, final Player players, final PlayerInstance player) throws MituyaProjectException {
        if (player.hasPermission(Rank.Supporter)) {
            if (message.argsLength() > 0) {
                final PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));
                if (ins == null) {
                    throw new PlayerNotFoundException(message.getString(0));
                } else if (ins.getPlayer() == null || !ins.getPlayer().isOnline()) {
                    throw new PlayerOfflineException(ins.getName());
                } else if (ins.getTpPublic() == 0) {
                    String ms = ChatColor.RED + "Player " + ChatColor.YELLOW + ins.getName() + ChatColor.RED + " はパブリック設定をしていません.";
                    if (player.hasPermission(Rank.GM)) {
                        ms += ChatColor.YELLOW + " GM権限でスキップしました.";
                        ins.getPlayer().teleport(players.getLocation());
                        player.setTp();
                        player.sendProcessing(ins.getName() + "を呼んでいます");
                        ins.sendProcessing(players.getName() + " にテレポートしています");
                    }
                    players.sendMessage(ms);
                } else if (ins.getTpPublic() == 1) {
                    ins.getPlayer().teleport(players.getLocation());
                    player.setTp();
                    player.sendProcessing(ins.getName() + "を呼んでいます");
                    ins.sendProcessing(players.getName() + " にテレポートしています");
                } else {
                    if (player.hasPermission(Rank.GM)) {
                        ins.getPlayer().teleport(players.getLocation());
                        player.setTp();
                        ins.sendProcessing(players.getName() + " にテレポートしています");
                    } else {
                        if (ins.isCheck()) {
                            player.sendAttention(players.getName() + " は、現在応答できません。");
                        } else {
                            ins.sendYesNoFromPlayer(players.getName() + "さんに呼ばれました。テレポートしますか？", player, new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ins.getPlayer().teleport(players.getLocation());
                                        player.setTp();
                                        player.sendProcessing(ins.getName() + "を呼んでいます");
                                        ins.sendProcessing(players.getName() + " にテレポートしています");
                                    }
                                    catch (PlayerOfflineException ex) {
                                    }
                                }
                            });
                        }
                    }
                }
            } else {
                player.sendAttention("/tphere プレーヤーID");
            }
        }
    }

    @Command(aliases = {"spawn", "ｓぱｗｎ"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.teleport.spawn"})
    public static void spawnBrush(CommandContext message, MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            if (message.getString(0).equalsIgnoreCase("back")) {
                if (player.getDeath() == null) {//保存してないとき
                    player.sendAttention("死んだ直後のみ使えます");
                } else {
                    player.sendMineYesNo(Parameter328.spawnback, new Runnable() {
                        @Override
                        public void run() {
                            if (player.gainMine(-Parameter328.spawnback)) {
                                players.teleport(player.getDeath());
                                player.setTp();
                                player.sendInfo(ChatColor.YELLOW + "SpawnBack", ChatColor.YELLOW + "死んだ場所に移動しました");
                            }
                        }
                    });
                }
            } else if (message.getString(0).equalsIgnoreCase("set") || message.getString(0).equalsIgnoreCase("save")) {
                if (players.getWorld().equals(plugin.getWorldManager().getWorld("harvest")) || players.getWorld().equals(plugin.getWorldManager().getWorld("harvest_nether"))) {
                    player.sendAttention("採掘ワールドでは このコマンドは使用できません.");
                    return;
                }
                player.setSpawn(players.getLocation().clone());
                player.sendSuccess("スポーンを設定しました.");
            } else if (message.getString(0).equalsIgnoreCase("default")) {
                player.setTp();
                player.sendProcessing("デフォルトスポーンにテレポートしています");
                player.teleport(plugin.getWorldSpawn(players.getWorld()));
            } else if (message.getString(0).equalsIgnoreCase("reset") || message.getString(0).equalsIgnoreCase("re-set") || message.getString(0).equalsIgnoreCase("delete")) {
                player.removeCustomSpawn(players.getWorld());
                player.sendSuccess("デフォルトスポーンに設定しました.");
            } else {
                player.sendAttention("/spawn [back/set/default/reset]");
            }
        } else {
            players.teleport(player.getSpawn(players.getWorld()));
            player.setTp();
            player.sendProcessing("スポーンにテレポートしています");
        }
    }

    @Command(aliases = {"tpworld"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.teleport.world"})
    public static void tpworldBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            World wd = plugin.getServer().getWorld(message.getString(0));
            if (wd == null) {
                player.sendAttention("ワールド:" + message.getString(0) + " は見つかりませんでした。");
                return;
            }
            player.sendProcessing("ワールド:" + wd.getName() + " に移動しています");
            player.teleport(wd.getSpawnLocation());
        } else {
            player.sendAttention("/tpworld ワールド名");
        }
    }

    @Command(aliases = {"world", "wd", "ワールド", "をｒｌｄ", "ｗｄ"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    public static void worldBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.handleCommand(players, player, "tp world");
    }

    @Command(aliases = {"newworld", "new_world", "nwd", "ニューワールド", "ねっをｒｌｄ", "んｗｄ"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    public static void newworldBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.handleCommand(players, player, "tp newworld");
    }

    @Command(aliases = {"harvest", "ハーベスト", "ht", "ｈｔ"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    public static void harvestBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.handleCommand(players, player, "tp harvest");
    }

    @Command(aliases = {"flat", "sf", "superflat", "スーパーフラット", "ｆぁｔ", "ｓｆ"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    public static void flatBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.handleCommand(players, player, "tp flat");
    }

    public static void TeleportYesNo(final MituyaProject plugin, final PlayerInstance player, final PlayerInstance ins, final CommandContext message, final Player players) throws PlayerOfflineException {
        player.sendAttention(ins.getName() + "さんにテレポート申請を送りました。許諾され次第テレポートを試行します...");
        ins.sendYesNoFromPlayer(players.getName() + "さんがテレポートしようとしています。よろしいですか？", player, new Runnable() {
            @Override
            public void run() {
                try {
                    Player cp = ins.getPlayer();
                    player.setTp();
                    player.sendProcessing(ins.getName() + " にテレポート中");
                    if (player.teleport(cp.getLocation())) {
                        if (!message.hasFlag('s') && !plugin.getInstanceManager().getHideManager().isHidePlayer(players)) {
                            ins.sendProcessing(players.getName() + " がテレポートしてきています...");
                        }
                    }
                }
                catch (PlayerOfflineException ex) {
                    Logger.getLogger(TeleportCommands.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
