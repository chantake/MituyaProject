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
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Util.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandPermissions;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ezura573
 */
public class LotteryCommands {

    private static final int lottery_price = 1000;//宝くじの価格
    private static final String lottery_name = "328クジ"; // 宝くじの名前

    @Command(aliases = {"lottery", "クジ"}, usage = "", desc = "Lottery",
            flags = "", min = 0, max = -1)
    @CommandPermissions("mituya.lottery")
    public static void lotteryBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws MituyaProjectException {
        // まとめ買い
        if (message.argsLength() > 1 && message.getString(0).toLowerCase().equals("buy")) {
            final int leaves = Integer.valueOf(message.getString(1));
            if (leaves > 100) {
                player.sendMessage(ChatColor.RED + lottery_name + "の複数購入は、一回の購入につき１００枚以内となっております。");
                return;
            }

            // 破産防止処理
            if (player.getMine() < 100000 + lottery_price * leaves) {
                player.sendMessage(ChatColor.RED + lottery_name + "の購入で所持金が10万Mineを下回る事は出来ません。");
                return;
            }
            player.sendMessage(ChatColor.AQUA + lottery_name + "を" + leaves + "枚購入します。");
            player.sendMineYesNo(lottery_price * leaves, new Runnable() {
                @Override
                public void run() {
                    //購入処理
                    if (player.gainMine(-lottery_price * leaves)) {
                        int[] windata = new int[7];
                        long winprice = 0;
                        for (int i = 0; i < 7; i++) {
                            windata[i] = 0;
                        }
                        for (int i = 0; i < leaves; i++) {
                            windata[Lottery()]++;
                        }
                        for (int i = 1; i < 7; i++) {
                            if (windata[i] > 0) {
                                player.sendMessage(ChatColor.AQUA + "" + i + "等　×　" + windata[i] + "本");
                                winprice += GetLotteryWinPrice(i) * windata[i];
                            }
                        }

                        if (winprice == 0) {
                            player.sendMessage(ChatColor.AQUA + "残念！！　次回に期待しましょう。");
                        } else {
                            player.sendMessage(ChatColor.AQUA + "合計" + ChatColor.GOLD + Tools.FormatMine(winprice) + ChatColor.AQUA + "Mine が当たりました。");
                            player.gainMine(winprice);
                            if (winprice >= 1000000) {
                                plugin.broadcastMessage(ChatColor.AQUA + "<<<" + lottery_name + "インフォメーション>>>");
                                plugin.broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.AQUA + "さんが" + lottery_name + "を"
                                        + leaves + "枚購入して"
                                        + ChatColor.GOLD + Tools.FormatMine(winprice) + ChatColor.AQUA + "Mine"
                                        + "当たりました。");
                                plugin.broadcastMessage(ChatColor.AQUA + "おめでとうございます！！");

                            }
                        }

                        //購入ログを記録
                        try {
                            InsertLotteryLog(plugin, players.getName(), leaves, lottery_price * leaves, winprice, windata);
                        }
                        catch (SQLException ex) {
                            Logger.getLogger(LotteryCommands.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Mineが不足しているため" + lottery_name + "を購入出来ませんでした。");
                    }
                }
            });
            return;
        }

        // 単品購入
        if (message.argsLength() > 0 && message.getString(0).toLowerCase().equals("buy")) {

            // 破産防止処理
            if (player.getMine() < 100000 + lottery_price) {
                player.sendMessage(ChatColor.RED + lottery_name + "の購入で所持金が10万Mineを下回る事は出来ません。");
                return;
            }

            player.sendMessage(ChatColor.AQUA + lottery_name + "を購入します。");
            player.sendMineYesNo(lottery_price, new Runnable() {
                @Override
                public void run() {
                    //購入処理
                    if (player.gainMine(-lottery_price)) {
                        //抽選処理
                        int num = Lottery();
                        if (num == 0) {
                            player.sendMessage(ChatColor.AQUA + "残念！！　次回に期待しましょう。");
                        } else {
                            player.gainMine(GetLotteryWinPrice(num));
                            player.sendMessage(ChatColor.AQUA + "" + num + "等 " + ChatColor.GOLD + Tools.FormatMine(GetLotteryWinPrice(num)) + ChatColor.GOLD + "Mine が当たりました。");
                        }
                        if (num > 0 && num < 4) {
                            plugin.broadcastMessage(ChatColor.AQUA + "<<<" + lottery_name + "インフォメーション>>>");

                            plugin.broadcastMessage(ChatColor.AQUA + "" + lottery_name + "で、"
                                    + ChatColor.YELLOW + player.getName()
                                    + ChatColor.AQUA + "さんに"
                                    + num + "等 "
                                    + ChatColor.GOLD + Tools.FormatMine(GetLotteryWinPrice(num))
                                    + ChatColor.AQUA + "Mine"
                                    + "が当たりました。");
                            plugin.broadcastMessage(ChatColor.AQUA + "おめでとうございます！！");
                        }

                        //購入ログを記録
                        try {
                            InsertLotteryLog(plugin, players.getName(), num);
                        }
                        catch (SQLException ex) {
                            Logger.getLogger(LotteryCommands.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Mineが不足しているため" + lottery_name + "を購入出来ませんでした。");
                    }
                }
            });
            return;
        }

        if (message.argsLength() > 0 && message.getString(0).toLowerCase().equals("list")) {
            player.sendMessage(ChatColor.YELLOW + "===================================================");
            player.sendMessage(ChatColor.YELLOW + "                " + lottery_name + " 当選金一覧  ");
            player.sendMessage(ChatColor.YELLOW + "===================================================");
            player.sendMessage(ChatColor.YELLOW + "１等     3億Mine");
            player.sendMessage(ChatColor.YELLOW + "２等  3000万Mine");
            player.sendMessage(ChatColor.YELLOW + "３等   100万Mine");
            player.sendMessage(ChatColor.YELLOW + "４等    10万Mine");
            player.sendMessage(ChatColor.YELLOW + "５等    5000Mine");
            player.sendMessage(ChatColor.YELLOW + "６等 　 1000Mine");
            player.sendMessage(ChatColor.YELLOW + "===================================================");

            return;
        }

        player.sendMessage(ChatColor.YELLOW + "===================================================");
        player.sendMessage(ChatColor.YELLOW + "                  " + lottery_name + " HELP                  ");
        player.sendMessage(ChatColor.YELLOW + "                                Presented by えずら");
        player.sendMessage(ChatColor.YELLOW + "===================================================");
        player.sendMessage(ChatColor.YELLOW + "[エイリアス]");
        player.sendMessage(ChatColor.YELLOW + "lottery,クジ");
        player.sendMessage(ChatColor.YELLOW + "");
        player.sendMessage(ChatColor.YELLOW + "[使用方法]");
        player.sendMessage(ChatColor.YELLOW + "lottery buy　　　　　　　　１枚購入します。　　");
        player.sendMessage(ChatColor.YELLOW + "lottery buy 枚数  　　　　 指定枚数購入します。");
        player.sendMessage(ChatColor.YELLOW + "===================================================");
    }

    private static int GetLotteryWinPrice(int num) {
        switch (num) {
            case 1:
                return 300000000;
            case 2:
                return 30000000;
            case 3:
                return 1000000;
            case 4:
                return 100000;
            case 5:
                return 5000;
            case 6:
                return 1000;
        }
        return 0;
    }

    private static int Lottery() {
        int rnd = (int)(Math.random() * 2000000);

        //１等
        if (rnd == 1) {
            return 1;
        }

        //２等
        if ((rnd >= 2000) && (rnd < 2002)) {
            return 2;
        }

        //３等
        if ((rnd >= 3000) && (rnd < 3200)) {
            return 3;
        }

        //４等
        if ((rnd >= 4000) && (rnd < 6000)) {
            return 4;
        }

        //５等
        if ((rnd >= 10000) && (rnd < 50000)) {
            return 5;
        }

        //６等
        if ((rnd >= 100000) && (rnd < 300000)) {
            return 6;
        }

        return 0;
    }

    private static boolean InsertLotteryLog(MituyaProject plugin, String Player, int win) throws SQLException {
        int[] windata = new int[7];
        long winprice = 0;
        windata[win]++;

        InsertLotteryLog(plugin, Player, 1, lottery_price, GetLotteryWinPrice(win), windata);

        return true;
    }

    private static boolean InsertLotteryLog(MituyaProject plugin, String Player, int leaves, long buy_price, long win_price, int[] windata) throws SQLException {
        String sql = "insert into lottery_log (player,`leaves`,buy_price,win_price,win1,win2,win3,win4,win5,win6,DTM) values (?,?,?,?,?,?,?,?,?,?,now())";
        try (JDCConnection con = plugin.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, Player);
            ps.setInt(2, leaves);
            ps.setLong(3, buy_price);
            ps.setLong(4, win_price);
            ps.setInt(5, windata[1]);
            ps.setInt(6, windata[2]);
            ps.setInt(7, windata[3]);
            ps.setInt(8, windata[4]);
            ps.setInt(9, windata[5]);
            ps.setInt(10, windata[6]);

            //INSERT実行       
            ps.executeUpdate();
        }
        return true;
    }
}
