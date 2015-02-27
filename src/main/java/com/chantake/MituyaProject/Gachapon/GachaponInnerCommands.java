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
package com.chantake.MituyaProject.Gachapon;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Util.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ezura573
 */
public class GachaponInnerCommands {

    // <editor-fold defaultstate="collapsed" desc="test">
    @Command(aliases = {"test"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void test(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        //初期化チェック
        if (!InitCheck(p, true)) {
            return;
        }

        //ID取得
        int id;
        try {
            if (message.argsLength() > 0) {
                id = message.getInteger(0);
            } else {
                pi.sendAttention("中身をチェックしたいカプセルのIDを指定してください。");
                return;
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("カプセルIDは、半角数字で指定してください。");
            return;
        }

        //インベントリチェック
        if (!pi.InventryCheck(1, (short)0, 64)) {
            pi.sendAttention("インベントリに空きが有りません。");
            return;
        }

        GachaponCapsuleData gcd = GachaponDataManager.GetCapsule(id);
        if (gcd == null) {
            pi.sendAttention("ID[" + id + "]のカプセルは存在しません。");
        } else {
            if (gcd.GetItems().isEmpty()) {
                pi.sendAttention("ID[" + id + "][" + gcd.GetName() + "]のカプセルは空っぽです。");
            } else {
                PlayerInventoryAddGachaponItem(plugin, pi, gcd);
                pi.sendAttention("ID[" + id + "][" + gcd.GetName() + "]の中身を取り出しました。");

            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="reload">
    @Command(aliases = {"reload"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void reload(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        GachaponDataManager gdm = new GachaponDataManager(plugin);
        if (gdm.reloadData()) {
            p.sendMessage(ChatColor.AQUA + "328ガチャのリロードが完了しました。");
        } else {
            pi.sendAttention("328ガチャのリロードに失敗しました。");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setinterval">
    @Command(aliases = {"setinterval", "si"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void setinterval(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        int interval;

        //インターバル取得
        try {
            if (message.argsLength() > 0) {
                interval = message.getInteger(0);
            } else {
                pi.sendAttention("インターバルを指定して下さい。");
                return;
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("インターバルは、半角数字で指定してください。");
            return;
        }

        //インターバルセット
        GachaponDataManager.SetBuyInterval(interval);

        //結果メッセージ送信
        pi.sendMessage(ChatColor.LIGHT_PURPLE + "328ガチャの購入間隔を" + interval + "msec にしました。");
        plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");
        plugin.broadcastMessage(ChatColor.AQUA + "328ガチャの購入間隔が" + interval + "msec になりました。");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="settps">
    @Command(aliases = {"settps", "st"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void settps(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        int tps;

        //tps取得
        try {
            if (message.argsLength() > 0) {
                tps = message.getInteger(0);
            } else {
                pi.sendAttention("必要TPSを指定して下さい。");
                return;
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("必要TPSは、半角数字で指定してください。");
            return;
        }

        //tpsセット
        GachaponDataManager.SetMinBuyTPS((double)tps);

        //結果メッセージ送信
        pi.sendMessage(ChatColor.LIGHT_PURPLE + "328ガチャの購入の必要TPSを" + tps + "に設定しました。");
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="buy">
    @Command(aliases = {"buy"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.buy"})
    public static void buy(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {

        //購入間隔制限
        if (System.currentTimeMillis() - pi.GetGachaponBuyTime() < GachaponDataManager.GetBuyInterval()) {
            pi.sendAttention("ガチャの購入間隔が短すぎます。");
            return;
        }

        //高負荷対策
        if (plugin.getPerformanceMonitor().getTPS() < GachaponDataManager.GetMinBuyTPS()) {
            pi.sendAttention("現在サーバの負荷が高くなっているため購入出来ません。");
            pi.sendAttention("時間を置いてお試し下さい。");
            return;
        }

        //初期化チェック
        if (!InitCheck(p, false)) {
            return;
        }

        //ユーザデータチェック
        try {
            CheckAndLoadUserData(plugin, p, pi);
        }
        catch (SQLException ex) {
            pi.GetGachaponData().clear();
            pi.sendAttention("ガチャデータのロードに失敗しました。");
            Logger.getLogger(GachaponInnerCommands.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        //Phase取得
        int phase = GachaponDataManager.GetDefaultPhase();
        try {
            if (message.argsLength() > 0) {
                phase = message.getInteger(0);
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("フェーズ番号は、半角数字で指定してください。");
            return;
        }

        //フェーズデータ取得
        final GachaponPhaseData gpd = GachaponDataManager.GetPhaseData(phase);
        if (gpd == null) {
            pi.sendAttention("指定されたフェーズは存在しません。");
            return;
        }

        //販売可否チェック
        if (!CheakSale(p, gpd)) {
            return;
        }

        // 破産防止処理
        if (pi.getMine() < 100000 + gpd.GetPrice()) {
            pi.sendAttention("328ガチャの購入で所持金が10万Mineを下回る事は出来ません。");
            return;
        }

        //インベントリチェック
        if (!pi.InventryCheck(1, (short)0, 64)) {
            pi.sendAttention("インベントリに空きが有りません。");
            return;
        }

        pi.sendMessage(ChatColor.LIGHT_PURPLE + "328ガチャ・フェーズ[" + gpd.GetPhase() + "]を購入します。");
        pi.sendMineYesNo(gpd.GetPrice(), () -> {
            if (pi.gainMine(-gpd.GetPrice())) {
                //ガチャの音っぽいピストンの音を再生
                p.playSound(p.getLocation(), Sound.PISTON_EXTEND, 1.0f, 1.0f);

                //抽選処理
                GachaponCapsuleData gcd = gpd.GetLotteryCapsule();

                //カプセルの中身をプレイヤーに追加
                try {
                    PlayerInventoryAddGachaponItem(plugin, pi, gcd);
                } catch (PlayerOfflineException ex) {
                    Logger.getLogger(GachaponInnerCommands.class.getName()).log(Level.SEVERE, null, ex);
                }

                //プレイヤーインスタンスの取得済みリストにIDを追加
                boolean ck = false;
                for (int i = 0; i < pi.GetGachaponData().size(); i++) {
                    if (pi.GetGachaponData().get(i).GetPhase() == gpd.GetPhase()) {
                        pi.GetGachaponData().get(i).IncBuyNum();
                        if (!pi.GetGachaponData().get(i).GetList().contains(gcd.GetId())) {
                            pi.GetGachaponData().get(i).GetList().add(gcd.GetId());
                            if (pi.GetGachaponData().get(i).GetList().size() == gpd.GetCapsules().size()) {
                                ck = true;
                            }
                        }
                        break;
                    }
                }
                //フェーズデータの販売数加算
                gpd.IncSaleNum();

                //ガチャポンログに結果を記録
                try {

                    GachaponDataManager.InsertGachaponLog(p.getName(), p.getUniqueId().toString(), gcd.GetId());
                } catch (SQLException ex) {
                    Logger.getLogger(GachaponInnerCommands.class.getName()).log(Level.SEVERE, null, ex);
                }

                //結果メッセージ送信
                String name = gcd.GetName();
                if (gcd.GetPublishFlg()) {
                    //大当たり用
                    pi.sendAttention(ChatColor.LIGHT_PURPLE + "[No." + String.format("%1$03d", gcd.GetId()) + "]" + ChatColor.AQUA + "  「" + ChatColor.GOLD + name + ChatColor.AQUA + "」が当たりました。");
                    plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");

                    plugin.broadcastMessage(ChatColor.AQUA + "328ガチャ・フェーズ[" + gpd.GetPhase() + "]で、"
                            + ChatColor.YELLOW + pi.getName()
                            + ChatColor.AQUA + "さんに"
                            + "[No." + String.format("%1$03d", gcd.GetId())
                            + "]  " + ChatColor.GOLD + "「"
                            + name
                            + "」" + ChatColor.AQUA + "が当たりました。");
                    plugin.broadcastMessage(ChatColor.AQUA + "おめでとうございます！！");

                } else {
                    //小当り用
                    pi.sendAttention(ChatColor.LIGHT_PURPLE + "[No." + String.format("%1$03d", gcd.GetId()) + "]" + ChatColor.AQUA + "  「" + ChatColor.GOLD + name + ChatColor.AQUA + "」が当たりました。");
                }

                //コンプリート処理
                if (ck) {
                    plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");
                    plugin.broadcastMessage(
                            ChatColor.YELLOW + pi.getName()
                                    + ChatColor.AQUA + "さんが"
                                    + "328ガチャ フェーズ[" + gpd.GetPhase() + "]"
                                    + "を" + ChatColor.GOLD + "コンプリート" + ChatColor.AQUA + "しました。");
                    plugin.broadcastMessage(ChatColor.AQUA + "おめでとうございます！！");
                }

                //シークレット解禁処理
                if (gcd.GetSecret()) {
                    gcd.SetSecret(false);
                    plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");
                    plugin.broadcastMessage(ChatColor.AQUA
                            + "328ガチャ・フェーズ[" + gpd.GetPhase() + "]リストで[No."
                            + String.format("%1$03d", gcd.GetId()) + "]  "
                            + ChatColor.GOLD + "「" + gcd.GetName() + "」"
                            + ChatColor.AQUA + "の表示が解禁されました。");
                }

                pi.SetGachaponBuyTime(System.currentTimeMillis());
            } else {
                pi.sendAttention("Mineが不足しているため３２８ガチャを購入出来ませんでした。");
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="price">
    @Command(aliases = {"price"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.price"})
    public static void price(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        //初期化チェック
        if (!InitCheck(p, false)) {
            return;
        }

        //価格リスト表示
        pi.sendMessage(ChatColor.YELLOW + "===============328ガチャ価格リスト=================");
        for (int i = 0; i < GachaponDataManager.GetPhaseData().size(); i++) {
            GachaponPhaseData gpd = GachaponDataManager.GetPhaseData().get(i);
            if (GachaponDataManager.GetDefaultPhase() == gpd.GetPhase()) {
                if (gpd.GetSale()) {
                    pi.sendMessage(ChatColor.YELLOW + "　フェーズ[" + gpd.GetPhase() + "]:" + Tools.FormatMine(gpd.GetPrice()) + "Mine [" + ChatColor.AQUA + "販売中" + ChatColor.YELLOW + "] ★現行フェーズ★");
                } else {
                    pi.sendMessage(ChatColor.YELLOW + "　フェーズ[" + gpd.GetPhase() + "]:" + Tools.FormatMine(gpd.GetPrice()) + "Mine [" + ChatColor.RED + "販売停止中" + ChatColor.YELLOW + "] ★現行フェーズ★");
                }
            } else {
                if (gpd.GetSale()) {
                    pi.sendMessage(ChatColor.YELLOW + "　フェーズ[" + gpd.GetPhase() + "]:" + Tools.FormatMine(gpd.GetPrice()) + "Mine [" + ChatColor.AQUA + "販売中" + ChatColor.YELLOW + "]");
                } else {
                    pi.sendMessage(ChatColor.YELLOW + "　フェーズ[" + gpd.GetPhase() + "]:" + Tools.FormatMine(gpd.GetPrice()) + "Mine [" + ChatColor.RED + "販売停止中" + ChatColor.YELLOW + "]");
                }
            }
            long num = gpd.GetSaleNum();
            pi.sendMessage("   ⇒　全体購入数：" + Tools.FormatMine(num));
        }
        pi.sendMessage(ChatColor.YELLOW + "===================================================");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="list">
    @Command(aliases = {"list"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.list"})
    public static void list(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        //初期化チェック
        if (!InitCheck(p, false)) {
            return;
        }

        //Phase取得
        int phase = GachaponDataManager.GetDefaultPhase();
        try {
            if (message.argsLength() > 0) {
                phase = message.getInteger(0);
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("フェーズ番号は、半角数字で指定してください。");
            return;
        }

        //フェーズデータ取得
        final GachaponPhaseData gpd = GachaponDataManager.GetPhaseData(phase);
        if (gpd == null) {
            pi.sendAttention("指定されたフェーズは存在しません。");
            return;
        }

        //リスト表示
        pi.sendMessage(ChatColor.YELLOW + "============328ガチャ・フェーズ[" + gpd.GetPhase() + "] 景品目録============");
        for (int i = 0; i < gpd.GetCapsuleVarietyNum(); i++) {
            if (pi.hasPermission(Rank.Admin) || pi.hasPermission(Rank.SubAdmin)) {
                if (gpd.GetCapsules().get(i).GetSecret()) {
                    pi.sendMessage("[No." + String.format("%1$03d", gpd.GetCapsules().get(i).GetId()) + "] [？] 「" + gpd.GetCapsules().get(i).GetName() + "」");
                } else {
                    pi.sendMessage("[No." + String.format("%1$03d", gpd.GetCapsules().get(i).GetId()) + "] [○] 「" + gpd.GetCapsules().get(i).GetName() + "」");
                }
            } else {
                if (gpd.GetCapsules().get(i).GetSecret()) {
                    pi.sendMessage("[No." + String.format("%1$03d", gpd.GetCapsules().get(i).GetId()) + "]  「?????????????????????」");
                } else {
                    pi.sendMessage("[No." + String.format("%1$03d", gpd.GetCapsules().get(i).GetId()) + "]  「" + gpd.GetCapsules().get(i).GetName() + "」");
                }
            }
        }
        pi.sendMessage(ChatColor.YELLOW + "===================================================");
        long num = gpd.GetSaleNum();
        pi.sendMessage("全体購入数：" + Tools.FormatMine(num));
        pi.sendMessage(ChatColor.YELLOW + "===================================================");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="mylist">
    @Command(aliases = {"mylist"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.mylist"})
    public static void mylist(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        //初期化チェック
        if (!InitCheck(p, false)) {
            return;
        }

        //ユーザデータチェック
        try {
            CheckAndLoadUserData(plugin, p, pi);
        }
        catch (SQLException ex) {
            pi.GetGachaponData().clear();
            pi.sendAttention("ガチャデータのロードに失敗しました。");
            Logger.getLogger(GachaponInnerCommands.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        //Phase取得
        int phase = GachaponDataManager.GetDefaultPhase();
        try {
            if (message.argsLength() > 0) {
                phase = message.getInteger(0);
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("フェーズ番号は、半角数字で指定してください。");
            return;
        }

        //フェーズデータ取得
        final GachaponPhaseData gpd = GachaponDataManager.GetPhaseData(phase);
        if (gpd == null) {
            pi.sendAttention("指定されたフェーズは存在しません。");
            return;
        }

        //リスト表示
        long num = 0;
        List lt = new ArrayList<>();
        for (int i = 0; i < pi.GetGachaponData().size(); i++) {
            if (pi.GetGachaponData().get(i).GetPhase() == gpd.GetPhase()) {
                lt = pi.GetGachaponData().get(i).GetList();
                num = pi.GetGachaponData().get(i).GetBuyNum();
                break;
            }
        }

        pi.sendMessage(ChatColor.YELLOW + "==========328ガチャ・フェーズ[" + gpd.GetPhase() + "] マイリスト==========");
        for (int i = 0; i < gpd.GetCapsuleVarietyNum(); i++) {
            if (gpd.GetCapsules().get(i).GetSecret()) {
                pi.sendMessage("[No." + String.format("%1$03d", gpd.GetCapsules().get(i).GetId()) + "]" + ChatColor.DARK_GRAY + "  「?????????????????????」");
            } else {
                boolean ck = lt.contains(gpd.GetCapsules().get(i).GetId());
                if (ck) {
                    pi.sendMessage("[No." + String.format("%1$03d", gpd.GetCapsules().get(i).GetId()) + "]  「" + gpd.GetCapsules().get(i).GetName() + "」");

                } else {
                    pi.sendMessage("[No." + String.format("%1$03d", gpd.GetCapsules().get(i).GetId()) + "]" + ChatColor.DARK_GRAY + "  「" + gpd.GetCapsules().get(i).GetName() + "」");
                }
            }
        }
        pi.sendMessage(ChatColor.YELLOW + "===================================================");
        pi.sendMessage("個人購入数：" + Tools.FormatMine(num));
        pi.sendMessage(ChatColor.YELLOW + "===================================================");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setprice">
    @Command(aliases = {"setprice"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void setprice(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        int phase;
        int price = 0;

        //Phase取得
        try {
            if (message.argsLength() > 0) {
                phase = message.getInteger(0);
            } else {
                pi.sendAttention("価格を設定するフェーズを指定してください。");
                return;
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("フェーズ番号は、半角数字で指定してください。");
            return;
        }

        //価格取得
        try {
            if (message.argsLength() > 1) {
                price = message.getInteger(1);
            } else {
                pi.sendAttention("設定する価格を指定してください。");
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("価格は半角数字で指定してください。");
            return;
        }

        //フェーズデータ取得
        final GachaponPhaseData gpd = GachaponDataManager.GetPhaseData(phase);
        if (gpd == null) {
            pi.sendAttention("指定されたフェーズは存在しません。");
            return;
        }

        //価格を設定
        gpd.SetPrice(price);

        //結果メッセージ送信
        if (price == gpd.GetPrice()) {
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "328ガチャ・フェーズ[" + gpd.GetPhase() + "] の価格を" + ChatColor.GOLD + Tools.FormatMine(gpd.GetPrice()) + "Mine" + ChatColor.LIGHT_PURPLE + "に設定しました。");
            plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");
            plugin.broadcastMessage(ChatColor.AQUA + "328ガチャ・フェーズ[" + gpd.GetPhase() + "] の価格が" + ChatColor.GOLD + Tools.FormatMine(gpd.GetPrice()) + "Mine" + ChatColor.AQUA + "になりました。");
        } else {
            pi.sendMessage(ChatColor.RED + "価格設定中にエラーが発生しました。");
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "現在328ガチャ・フェーズ[" + gpd.GetPhase() + "] の価格は" + ChatColor.GOLD + Tools.FormatMine(gpd.GetPrice()) + "Mine" + ChatColor.AQUA + "です。");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="start">
    @Command(aliases = {"start"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void start(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        int phase;

        //Phase取得
        try {
            if (message.argsLength() > 0) {
                phase = message.getInteger(0);
            } else {
                pi.sendAttention("販売を開始するフェーズを指定してください。");
                return;
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("フェーズ番号は、半角数字で指定してください。");
            return;
        }

        //フェーズデータ取得
        final GachaponPhaseData gpd = GachaponDataManager.GetPhaseData(phase);
        if (gpd == null) {
            pi.sendAttention("指定されたフェーズは存在しません。");
            return;
        }

        //販売開始
        gpd.SetSale(true);

        //結果メッセージ送信
        if (gpd.GetSale()) {
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "328ガチャ・フェーズ[" + gpd.GetPhase() + "] の販売を開始しました。");
            plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");
            plugin.broadcastMessage(ChatColor.AQUA + "328ガチャ・フェーズ[" + gpd.GetPhase() + "] の販売が開始されました。");
        } else {
            pi.sendMessage(ChatColor.RED + "販売可否設定中にエラーが発生しました。");
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "現在328ガチャ・フェーズ[" + gpd.GetPhase() + "] のは販売停止中です。");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="stop">
    @Command(aliases = {"stop"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void stop(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        int phase;

        //Phase取得
        try {
            if (message.argsLength() > 0) {
                phase = message.getInteger(0);
            } else {
                pi.sendAttention("販売を停止するフェーズを指定してください。");
                return;
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("フェーズ番号は、半角数字で指定してください。");
            return;
        }

        //フェーズデータ取得
        final GachaponPhaseData gpd = GachaponDataManager.GetPhaseData(phase);
        if (gpd == null) {
            pi.sendAttention("指定されたフェーズは存在しません。");
            return;
        }

        //販売停止
        gpd.SetSale(false);

        //結果メッセージ送信
        if (!gpd.GetSale()) {
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "328ガチャ・フェーズ[" + gpd.GetPhase() + "] の販売を停止しました。");
            plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");
            plugin.broadcastMessage(ChatColor.RED + "328ガチャ・フェーズ[" + gpd.GetPhase() + "] の販売が中止されました。");
        } else {
            pi.sendMessage(ChatColor.RED + "販売可否設定中にエラーが発生しました。");
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "現在328ガチャ・フェーズ[" + gpd.GetPhase() + "] のは販売中です。");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setdefault">
    @Command(aliases = {"setdefault", "sd"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.gachapon.admin"})
    public static void setdefault(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
        int phase;

        //Phase取得
        try {
            if (message.argsLength() > 0) {
                phase = message.getInteger(0);
            } else {
                pi.sendAttention("デフォルトに設定するフェーズを指定して下さい。");
                return;
            }
        }
        catch (NumberFormatException ex) {
            pi.sendAttention("フェーズ番号は、半角数字で指定してください。");
            return;
        }

        //フェーズデータ取得
        final GachaponPhaseData gpd = GachaponDataManager.GetPhaseData(phase);
        if (gpd == null) {
            pi.sendAttention("指定されたフェーズは存在しません。");
            return;
        }

        //デフォルトフェーズ設定
        GachaponDataManager.SetDefaultPhase(phase);

        //結果メッセージ送信
        if (GachaponDataManager.GetDefaultPhase() == phase) {
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "328ガチャのデフォルトフェーズをフェーズ[" + phase + "]に設定しました。");
        } else {
            pi.sendMessage(ChatColor.RED + "デフォルトフェーズ設定中にエラーが発生しました。");
            pi.sendMessage(ChatColor.LIGHT_PURPLE + "現在の328ガチャのデフォルトフェーズはフェーズ[" + phase + "]です。");
        }
    }
    // </editor-fold>

    /*
     // <editor-fold defaultstate="collapsed" desc="base">
     @Command(aliases = {"base"}, usage = "", desc = "", help = "",
     flags = "v", min = 0, max = -1)
     @CommandPermissions({"mituya.gachapon.admin"})
     public static void base(CommandContext message, final MituyaProject plugin, final Player p, final PlayerInstance pi) throws CommandException, PlayerOfflineException {
     }
     // </editor-fold>  
     */
    public static boolean InitCheck(final Player p, boolean admin) {
        if (!GachaponDataManager.GetInit()) {
            if (admin) {
                p.sendMessage(ChatColor.RED + "328ガチャの初期化が完了していません。");
                p.sendMessage(ChatColor.RED + "ガチャデータが正しいか確認してください。");
            } else {
                p.sendMessage(ChatColor.RED + "現在328ガチャは利用出来ません。");
            }
            return false;
        }
        return true;
    }

    public static boolean CheakSale(final Player p, GachaponPhaseData gpd) {
        if (gpd.GetSale()) {
            return true;
        } else {
            p.sendMessage(ChatColor.RED + "現在328ガチャ・フェーズ" + gpd.GetPhase() + " は取り扱っていません。");
            return false;
        }
    }

    // プレイヤーインベントリーにガチャの中身を付与する
    private static int PlayerInventoryAddGachaponItem(MituyaProject plugin, PlayerInstance pi, GachaponCapsuleData CapsuleData) throws PlayerOfflineException {
        int ret = 0;
        if (CapsuleData.GetUseInventory() > 1) {
            pi.getPlayer().getInventory().addItem(CapsuleData.GetBookData());
        } else {
            for (int i2 = 0; i2 < CapsuleData.GetItems().size(); i2++) {
                ItemStack is = CapsuleData.GetItems().get(i2).clone();
                pi.getPlayer().getInventory().addItem(is);
                ret++;
            }
        }

        //ハズレ本の付与
        if (CapsuleData.GetItems().isEmpty()) {
            pi.gainItem(GachaponDataManager.GetFailureBook());
        }

        //インベントリを強制アップデート
        //pi.getPlayer().updateInventory();
        return ret;
    }

    private static boolean CheckAndLoadUserData(final MituyaProject plugin, final Player p, final PlayerInstance pi) throws SQLException {
        if (pi.GetGachaponData().size() != GachaponDataManager.GetPhaseData().size()) {
            pi.GetGachaponData().clear();
        }
        if (pi.GetGachaponData().isEmpty()) {
            for (int i = 0; i < GachaponDataManager.GetPhaseData().size(); i++) {
                GachaponUserPhaseData gupd = new GachaponUserPhaseData();
                gupd.SetPhase(GachaponDataManager.GetPhaseData().get(i).GetPhase());
                pi.GetGachaponData().add(gupd);
            }
            String sql = "SELECT "
                    + "gachapon_log.capsule_id,"
                    + "gachapon_capsule.phase "
                    + "FROM "
                    + "gachapon_log "
                    + "INNER JOIN gachapon_capsule ON gachapon_log.capsule_id = gachapon_capsule.id "
                    + "WHERE "
                    + "gachapon_log.uuid = ? "
                    + "GROUP BY "
                    + "gachapon_log.capsule_id "
                    + "ORDER BY "
                    + "gachapon_log.capsule_id ASC";
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getUniqueId().toString());

                // レコードセットの取得
                try ( // SQL実行
                        ResultSet rs = ps.executeQuery()) {
                    // レコードセットの取得
                    rs.getRow();

                    // データ取得
                    while (rs.next()) {
                        int cid = rs.getInt("capsule_id");
                        int phase = rs.getInt("phase");
                        for (int i = 0; i < pi.GetGachaponData().size(); i++) {
                            if (pi.GetGachaponData().get(i).GetPhase() == phase) {
                                if (!pi.GetGachaponData().get(i).GetList().contains(cid)) {
                                    pi.GetGachaponData().get(i).GetList().add(cid);
                                }
                                break;
                            }
                        }
                    }
                }
            }

            sql = "SELECT "
                    + "gachapon_capsule.phase,"
                    + "count(gachapon_log.id) as cnt "
                    + "FROM "
                    + "gachapon_capsule "
                    + "INNER JOIN gachapon_log ON gachapon_log.capsule_id = gachapon_capsule.id "
                    + "WHERE "
                    + "gachapon_log.uuid = ? "
                    + "GROUP BY gachapon_capsule.phase "
                    + "ORDER BY "
                    + "gachapon_capsule.phase ASC";
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getUniqueId().toString());

                // レコードセットの取得
                try ( // SQL実行
                        ResultSet rs = ps.executeQuery()) {
                    // レコードセットの取得
                    rs.getRow();

                    // データ取得
                    while (rs.next()) {
                        int phase = rs.getInt("phase");
                        long cnt = rs.getLong("cnt");
                        for (int i = 0; i < pi.GetGachaponData().size(); i++) {
                            if (pi.GetGachaponData().get(i).GetPhase() == phase) {
                                pi.GetGachaponData().get(i).SetBuyNum(cnt);
                                break;
                            }
                        }
                    }
                }
            }

        }
        return true;
    }
}
