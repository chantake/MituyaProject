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
package com.chantake.MituyaProject.Parameter;

import com.chantake.MituyaProject.Tool.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 設定用のクラスです
 *
 * @author chantake
 * @version 2.0.0
 */
public class Parameter328 {

    // 看板
    public static final String Sign_Command = "[Command]";
    public static final String Sign_Command_Japanese = "[こまんど]";
    //Icon
    public static final Material Icon_Login = Material.GOLDEN_APPLE;
    public static final Material Icon_Quit = Material.APPLE;
    public static final String MainWorld = "world";
    public static final String SecondWorld = "new_world";
    public static final String The_EndWorld = "world_the_end";
    public static final String SuperFlat = "superflat";
    public static Tools tool;
    public static String AdminKey;
    /**
     * TempLocationにとる料金 ※現在半額3500→1750
     */
    public static int savelocation = 1750;
    /**
     * 個別Spawnセーブにとる料金　※現在半額7500→3750
     */
    public static int savespawn = 3750;
    /**
     * 死亡位置に戻る料金
     */
    public static int spawnback = 4743;
    // Mine関係
    /**
     * 税金
     */
    public static long taxmine;
    /**
     * コマンドSayに使う料金
     */
    public static int say = 10;
    public static String border = "これ以上先に進む権利がありません。(エリア制限)";
    public static HashMap<Player, String> gmcall = new HashMap<>();
    public static HashMap<Player, String> gm = new HashMap<>();
    public static String LoginMessage;
    public static ChatColor color;
    public static int kicktime = 25;// Mine
    /**
     * 初期Mine
     */
    public static int Default_Mine = 100000;
    public static int allowRank = 3;
    public static char command = '$';
    public static int api_port = 25561;
    public static int slime = 4;// スライム
    // ペット
    public static int Pet_WOLF = 10000;
    public static int Pet_Ocelot = 50000;
    public static int Village = 150000;
    public static Map<Entity, Player> lgnpr = new HashMap<>();
    public static int maxonline;
    public static long jackpot;// ジャックポット
    public static int removeMobtime = 50;//分
    public static boolean Mentenance = false;
    public static double FlyHeight = 130;
    public static int MAX_Party_Member = 7;
    public static boolean Login_Bonus = false;
    /**
     * Home作成料金
     */
    public static int[] Home_Mine = {0, 2000, 5000, 10000, 17500, 25000, 35000, 50000};
    //チャット範囲
    public static int ChatRange = 300;
    //削除アイテム（ID）
    public static int[] RemoveItem = {6, 81, 287, 288, 289, 295, 344, 367};
    public static int[] 許可ブロック = {64, 69, 70, 71, 72, 77, 96};
    public static float Exp = 1.5f;
    public static int DropRate = 900;
    public static int Mob_Limit_Spawner = 500;//スポーンから湧いたmobの最大数
    public static int Mod_Limit_OtherEntity = 500;//その他から湧いたmobの最大数
    public static int teleportticks = 100;//未実装
    public static int canteleportnodmgticks = 100;//5秒間ダメージを喰らってなかったらTPOKにするいやらしいシステム
    public static Timestamp delete_world;
    /**
     * 確認メッセージ
     */
    public static String Check_Message;
    public static int Check_Limit = 30;//単位:秒
    public static int Chat_Sleep_Time = 30;//30秒
    public static int Cmd_Sleep_Time = 3;//3秒
    //event
    //public static ConcurrentHashMap<String,Boolean> ValentineEvent = new ConcurrentHashMap<>();
    //Mineを即セーブする金額
    public static long SaveMineValue = 5000;

    /**
     * サーバー起動時に変数に代入します。
     *
     * @author chantake
     * @since 2.0.0
     */
    public static void run() {
        Parameter328.LoginMessage = ChatColor.BLUE + "328mssMinecraftServer" + ChatColor.AQUA + " へようこそ！";
        Parameter328.Check_Message = ChatColor.AQUA + "YES" + ChatColor.YELLOW + " or " + ChatColor.RED + "NO" + ChatColor.YELLOW + " を入力してください。";
        try {
            Parameter328.delete_world = new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2013/01/21").getTime());
        }
        catch (ParseException ex) {
            Logger.getLogger(Parameter328.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
