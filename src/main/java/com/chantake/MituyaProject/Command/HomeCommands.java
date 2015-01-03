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
import com.chantake.MituyaProject.Player.LocationData;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class HomeCommands {

// <editor-fold defaultstate="collapsed" desc="helpMessage">
    private enum Help {

        Noth("ホームを設定していません"),
        Main("/home [set/public/here/info/help]"),
        HomeSet("/home set ['home_id'/message]"),
        Here("/home [here/invite] 'プレーヤーID' 'ホームID'"),
        Public("/home public 'ホームID'"),
        HomeInfo(ChatColor.AQUA + "Home"),
        Rank("このコマンドをつかうには 権限がありません");
        private final String help;

        private Help(String txt) {
            this.help = txt;
        }

        public String Help() {
            return this.help;
        }

        @Override
        public String toString() {
            return this.help;
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="home">
    @Command(aliases = {"home", "ほめ"}, usage = "", desc = "Home",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.home"})
    public static void homeBrush(final CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws MituyaProjectException {
        World world = players.getWorld();
        if (world.equals(plugin.getWorldManager().getWorld("harvest"))) {
            player.sendAttention("このワールドではこのコマンドは使用できません。");
            return;
        }
        if (message.argsLength() > 0 && message.getString(0).equals("set")) {
            final int subid;
            if (message.argsLength() == 2) {
                subid = Tools.StringToInteger(message.getString(1));
            } else {
                subid = 0;
            }
            int home_size = 0;
            //Home既に使用済み
            if (player.getWorldHome(world) != null) {
                home_size = player.getWorldHome(world).size();
            }
            LocationData home = player.getHome(world, subid);
            if (home == null) {//Worldでhomeを使用してない or homeが無い場合
                int max_subid = 3;
                //player.hasPermission(Rank.Supporter) から　player.hasPermission(Rank.Niconama) に変更 byいんく
                if (player.hasPermission(Rank.Niconama)) {
                    max_subid = 6;
                }
                if (subid > max_subid || (subid > home_size)) {
                    player.sendAttention("ホームIDは " + max_subid + " 以下で下から順番に作成してください。");
                } else {
                    final int mine = Parameter328.Home_Mine[home_size];
                    player.sendMineYesNo(mine, new Runnable() {
                        @Override
                        public void run() {
                            player.gainMine(-mine);
                            player.SaveHome(players.getLocation().clone(), subid, false, null);
                            player.sendSuccess("ホームを設定しました。 ホームID:" + subid);
                        }
                    });
                }
            } else { //ある場合
                home.setLocation(players.getLocation().clone());
                player.SaveHome(home);
                player.sendSuccess("ホームを設定しました。 ホームID:" + subid);
            }
        } else if (message.argsLength() > 0 && (message.getString(0).equalsIgnoreCase("invite") || message.getString(0).equalsIgnoreCase("here"))) {
            if (message.argsLength() < 2 && Tools.Help(message.getString(0))) {
                player.sendAttention(Help.Here.Help());
            } else if (!player.hasPermission(Rank.LiveReporter)) {
                player.sendAttention(Help.Rank.Help());
            } else {
                PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(1));
                int subid = 0;
                if (message.argsLength() > 2) {
                    subid = Tools.StringToInteger(message.getString(2));
                }
                if (ins == null) {
                    //エラーメッセージ
                    throw new PlayerNotFoundException(message.getString(1));
                }
                Player cp = ins.getPlayer();
                if (cp == null) {
                    throw new PlayerOfflineException(ins.getName());
                }
                LocationData ld = player.getHome(players.getWorld(), subid);
                if (ld == null) {
                    player.sendAttention("あなたの ホーム(" + subid + ChatColor.RED + ") は見つかりませんでした。");
                } else if (!cp.getWorld().equals(players.getWorld()) && !player.hasPermission(Rank.GM)) {
                    player.sendAttention("プレーヤー " + ChatColor.YELLOW + cp.getName() + ChatColor.RED + " は、他のワールドにいません。");
                } else {
                    cp.teleport(ld.getLocation());
                    player.sendAttention(ChatColor.LIGHT_PURPLE + "ホーム(" + subid + ") に " + cp.getName() + " を呼んでいます ....");
                    ins.sendAttention(ChatColor.LIGHT_PURPLE + players.getName() + "(" + subid + ") のホームに移動しています....");
                    cp.sendMessage(ChatColor.LIGHT_PURPLE + ld.getMessage());
                }
            }
        } else if (message.argsLength() > 0 && message.getString(0).equals("public")) {
            int subid = 0;
            if (message.argsLength() > 1) {
                if (Tools.Help(message.getString(1))) {
                    player.sendAttention(Help.Public.Help());
                    return;
                }
                subid = Tools.StringToInteger(message.getString(1));
            }
            LocationData ld = player.getHome(players.getWorld(), subid);
            int wid = plugin.getWorldManager().getWorldData(players.getWorld()).getId();
            if (ld == null) {
                player.sendAttention(Help.Noth.Help());
            } else {
                player.setHomePublic(wid, subid);
                player.sendInfo(Help.HomeInfo.Help(), ChatColor.YELLOW + "あなたのホーム(" + subid + ") のパブリック状況は " + Tools.ReturnColorOnOff(player.getHomePublic(wid, subid)) + ChatColor.YELLOW + "です。");
            }
        } else if (message.argsLength() > 0 && Tools.Help(message.getString(0))) {
            player.sendAttention(Help.Main.Help());
        } else {
            if (message.argsLength() > 0 && !Tools.CheckInteger(message.getString(0))) {//他のhomeに移動
                PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));
                int subid = 0;
                if (message.argsLength() > 1) {
                    subid = Tools.StringToInteger(message.getString(1));
                }
                if (ins == null) {
                    throw new PlayerNotFoundException(message.getString(0));
                }
                Player cp = plugin.getServer().getPlayer(ins.getName());

                LocationData ld = ins.getHome(players.getWorld(), subid);
                if (ld == null) {
                    player.sendAttention(ChatColor.YELLOW + ins.getName() + "(" + subid + ChatColor.YELLOW + ")" + ChatColor.RED + " のホームは見つかりませんでした。");
                } else if (!ld.getPublic() && !player.hasPermission(Rank.GM)) {
                    player.sendAttention(ins.getName() + "(" + subid + ")" + ChatColor.DARK_GRAY + " のホームはパブリックではありません。");
                } else {
                    if (player.hasPermission(Rank.Moderator) && !ld.getPublic()) {
                        player.sendAttention(ins.getName() + "(" + subid + ")" + ChatColor.DARK_GRAY + " のホームはパブリックではありません。" + ChatColor.YELLOW + " GM権限でスキップしました。");
                    }
                    players.teleport(ld.getLocation());
                    player.setTp();
                    players.sendMessage(ChatColor.AQUA + ld.getMessage());
                    if (cp != null) {// オンラインの場合
                        ins.sendInfo(Help.HomeInfo.Help(), ChatColor.YELLOW + players.getName() + ChatColor.LIGHT_PURPLE + " がホーム(" + ld.getLocation().getWorld().getName() + "," + ld.getId() + ")を訪れました。");
                    }
                }
            } else {//自宅
                int subid = 0;
                if (message.argsLength() > 0) {
                    subid = Tools.StringToInteger(message.getString(0));
                }
                LocationData ld = player.getHome(players.getWorld(), subid);
                if (ld == null) {
                    player.sendAttention("ホーム(" + subid + ") を設定してません。");
                } else {
                    players.teleport(ld.getLocation());
                    player.setTp();
                    players.sendMessage(ChatColor.AQUA + ld.getMessage());
                }
            }
        }
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="homeset">
    @Command(aliases = {"sethome", "homeset"}, usage = "", desc = "home set command",
            flags = "", min = 0, max = -1)
    public static void homesetBrush(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        StringBuilder sb = new StringBuilder();
        sb.append("home set");
        if (args.argsLength() > 0) {
            sb.append(" ").append(args.getString(0));
        }
        plugin.handleCommand(players, player, sb.toString());
    }
// </editor-fold>
}
