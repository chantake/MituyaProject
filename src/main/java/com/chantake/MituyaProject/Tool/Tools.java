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
package com.chantake.MituyaProject.Tool;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.World.Jackpot;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * ツール
 *
 * @author chantake
 * @version 1.0.0
 */
public class Tools {

    public static Tools instance = new Tools();
    public static HexTool hextool;
    public static ConsoleCommandSender ccs;
    public static ChatColor color;
    /**
     * 冗長性のある改行
     */
    public static final String BR = System.getProperty("line.separator");

    public static ConsoleCommandSender getConsoleCommandSender() {
        return Tools.ccs;
    }

    public static HexTool getHex() {
        return Tools.hextool;
    }

    // <editor-fold defaultstate="collapsed" desc="ReturnColorOnOff(boolean boolean) Booleanの値に対して on off を返します">
    /**
     * Booleanの値に対して<b><font color="#0000ff">on</font> <font color="#ff0000">off</font></b> を返します
     *
     * @param b 値
     * @return 色つきメッセージ
     * @author chantake
     * @since 1.0.0
     */
    public static String ReturnColorOnOff(boolean b) {
        if (b) {
            return org.bukkit.ChatColor.BLUE + "on" + org.bukkit.ChatColor.WHITE;
        } else {
            return org.bukkit.ChatColor.RED + "off" + org.bukkit.ChatColor.WHITE;
        }
    }

    // </editor-fold>
    /**
     * 看板に色つきで挿入します
     *
     * @param plugin
     * @param ins
     * @param sign
     * @param line
     * @param test
     */
    public static void setSignLine(MituyaProject plugin, PlayerInstance ins, Sign sign, int line, String test) {
        sign.setLine(line, Tools.ChangeText(test, plugin, ins));
        sign.update(true);
    }

    /**
     * 看板用に&で指定された色に変換します
     *
     * @param text
     * @return 看板用変換済み文字
     */
    public static String SignColor(String text) {
        final String[] splitLine = text.split("&");
        String newLine = splitLine[0];
        //System.out.println("splitLine:" + splitLine[0]);
        for (int j = 1; j < splitLine.length; j++) {
            if (splitLine[j].length() == 0 || "0123456789abcdef".indexOf(splitLine[j].toLowerCase().charAt(0)) == -1 || splitLine[j].length() <= 1) {
                newLine += "#";
            } else {
                newLine += "\u00A7";
            }
            newLine += splitLine[j];
        }
        return newLine;
    }

    public static String[] SignColor(String[] text) {
        String[] txt = text;
        for (int i = 0; i < txt.length; i++) {
            if (txt[i].isEmpty()) {
                continue;
            }
            txt[i] = SignColor(txt[i]);
        }
        return txt;
    }

    /**
     * Stringの長さ（全角2文字、半角1文字として考える）
     *
     * @param text
     * @return
     */
    public static int getStringLength(String text) {
        try {
            return text.getBytes("Shift_JIS").length;
        }
        catch (UnsupportedEncodingException ex) {
            return text.length();
            //Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="StringArrayToString(String[] message, int line) StringArrayをStringに変換します">
    /**
     * StringArrayをStringに変換します
     *
     * @param me メッセージ(配列)
     * @param line 変換を開始する行数を指定
     * @return String
     * @since 1.0.0
     */
    public static String StringArrayToString(String[] me, int line) {
        String text = me[line];
        if (me.length > line) {
            for (int i = line + 1; i < me.length; i++) {
                text += " " + me[i];
            }
        }

        return text;
    }

    /**
     * 正規表現を利用し、StringからIntegerに変換します
     *
     * @param t
     * @return 数字以外の場合は 0 を返します
     */
    public static int StringToInteger(String t) {
        if (t.matches("[0-9]+")) {
            return Integer.valueOf(t);
        } else {
            return 0;
        }
    }

    /**
     * 正規表現を利用し、StringからLongに変換します
     *
     * @param t
     * @return 数字以外の場合は 0 を返します
     */
    public static long StringToLong(String t) {
        if (t.matches("[0-9]+")) {
            return Long.valueOf(t);
        } else {
            return 0;
        }
    }

    public static boolean CheckInteger(String t) {
        return t.matches("[0-9]+");
    }

    public String ChatColor(String[] t) {
        String text = "";
        for (final String element : t) {
            text += element;
        }
        return text;
    }

    public boolean CheckChar(String s, char c) {
        boolean b = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean CheckPass(String s) {
        final String hankaku = "\\p{Punct}"; // 半角記号
        return Pattern.compile("[" + hankaku + "]+").matcher(s).matches();
    }

    public String Color(String t) {
        final String text = "";
        if (t.matches("[0-9]+")) {
            switch (Integer.valueOf(t)) {
                case 1:
            }
        }
        return text;
    }

    public static boolean Help(String text) {
        return text.equalsIgnoreCase("help") || text.equalsIgnoreCase("?");
    }

    // </editor-fold>
    public String PassWord(String s) {
        final int l = s.length();
        String t = "";
        for (int i = 0; i < l; i++) {
            t += "*";
        }
        return t;
    }

    public static String getWorldTime(World world) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(world.getFullTime());
        int todayHH = calendar.get(Calendar.HOUR_OF_DAY);
        int todayMM = calendar.get(Calendar.MINUTE);
        int todaySS = calendar.get(Calendar.SECOND);

        String hh;
        String mm;
        String ss;

        if (todayHH < 10) {
            hh = "0" + Integer.toString(todayHH);
        } else {
            hh = Integer.toString(todayHH);
        }

        if (todayMM < 10) {
            mm = "0" + Integer.toString(todayMM);
        } else {
            mm = Integer.toString(todayMM);
        }
        if (todaySS < 10) {
            ss = "0" + Integer.toString(todaySS);
        } else {
            ss = Integer.toString(todaySS);
        }
        StringBuilder da = new StringBuilder();
        da.append(hh).append(":").append(mm).append(":").append(ss);
        return da.toString();
    }

    public static String getRealTime() {
        Calendar calendar = Calendar.getInstance();
        Date trialtime = new Date();
        calendar.setTime(trialtime);
        int todayHH = calendar.get(Calendar.HOUR_OF_DAY);
        int todayMM = calendar.get(Calendar.MINUTE);
        int todaySS = calendar.get(Calendar.SECOND);

        String hh;
        String mm;
        String ss;

        if (todayHH < 10) {
            hh = "0" + Integer.toString(todayHH);
        } else {
            hh = Integer.toString(todayHH);
        }

        if (todayMM < 10) {
            mm = "0" + Integer.toString(todayMM);
        } else {
            mm = Integer.toString(todayMM);
        }
        if (todaySS < 10) {
            ss = "0" + Integer.toString(todaySS);
        } else {
            ss = Integer.toString(todaySS);
        }
        StringBuilder da = new StringBuilder();
        da.append(hh).append(":").append(mm).append(":").append(ss);
        return da.toString();
    }

    /**
     * プレーヤー情報取得できないっす
     *
     * @param message
     * @param plugin
     * @return
     */
    public static String Pronoun(String message, MituyaProject plugin) {
        return Pronoun(message, plugin, null);
    }

    /**
     * 代名詞実装
     *
     * @param message
     * @param plugin
     * @param ins
     * @return
     */
    public static String Pronoun(String message, MituyaProject plugin, PlayerInstance ins) {
        String txt = message;
        if (ins != null) {
            try {
                if (txt.contains("<name>")) {
                    txt = txt.replaceAll("<name>", ins.getName());
                }
                if (txt.contains("<disname>")) {
                    txt = txt.replaceAll("<disname>", ins.getDisplayName());
                }
                if (txt.contains("<pos>")) {
                    Location lo = ins.getPlayer().getLocation();
                    String pos = "[X:" + (int)lo.getX() + " Y:" + (int)lo.getY() + " Z:" + (int)lo.getZ() + "]";
                    txt = txt.replaceAll("<pos>", pos);
                }
                if (txt.contains("<target>") || txt.contains("<tnm>") || txt.contains("<t>")) {
                    Block bl = ins.getPlayer().getTargetBlock(null, 100);
                    String name = "null";
                    if (bl != null) {
                        name = bl.getState().getType().toString();
                    }
                    if (txt.contains("<target>")) {
                        txt = txt.replaceAll("<target>", name);
                    } else if (txt.contains("<tnm>")) {
                        txt = txt.replaceAll("<tnm>", name);
                    } else {
                        txt = txt.replaceAll("<t>", name);
                    }
                }
                if (txt.contains("<cry>")) {
                    txt = txt.replaceAll("<cry>", String.valueOf(ins.getPlayer().getItemInHand().getAmount()));
                }
                if (txt.contains("<msm>")) {
                    txt = txt.replaceAll("<msm>", "Player");
                }
                if (txt.contains("<rank>")) {
                    txt = txt.replaceAll("<rank>", ins.getRank().getName());
                }
                if (txt.contains("<hp>")) {
                    txt = txt.replaceAll("<hp>", String.valueOf(ins.getPlayer().getHealth()));
                }
                if (txt.contains("<iteminhand>")) {
                    txt = txt.replaceAll("<iteminhand>", ins.getPlayer().getItemInHand().getType().toString());
                }
                if (txt.contains("<mine>")) {
                    txt = txt.replaceAll("<mine>", String.valueOf(ins.getMine()));
                }
                if (txt.contains("<world>")) {
                    txt = txt.replaceAll("<world>", ins.getPlayer().getWorld().getName());
                }
                if (txt.contains("<lv>")) {
                    txt = txt.replaceAll("<lv>", String.valueOf(ins.getPlayer().getLevel()));
                }
                if (txt.contains("<level>")) {
                    txt = txt.replaceAll("<lv>", String.valueOf(ins.getPlayer().getLevel()));
                }
            }
            catch (PlayerOfflineException ex) {
            }
        }
        if (txt.contains("<jpnowparsentYear>")) {
            txt = txt.replaceAll("<jpnowparsentYear>", Jackpot.checkJP());
        }
        if (txt.contains("<jp>")) {
            txt = txt.replaceAll("<jp>", String.valueOf(Parameter328.jackpot));
        }
        if (txt.contains("<clock>")) {
            txt = txt.replaceAll("<clock>", Tools.getWorldTime(plugin.getWorldManager().getWorld("world")));
        }
        if (txt.contains("<time>")) {
            txt = txt.replaceAll("<time>", Tools.getWorldTime(plugin.getWorldManager().getWorld("world")));
        }
        if (txt.contains("<realtime>")) {
            txt = txt.replaceAll("<realtime>", getRealTime());
        }
        if (txt.contains("<cpu>") || txt.contains("<tps>")) {
            String text;
            double tps = plugin.getPerformanceMonitor().getTPS();
            if (tps >= 17 && tps <= 23) {
                text = ChatColor.AQUA + "ひじょうにかいてき";
            } else if (tps >= 14 && tps <= 26) {
                text = ChatColor.YELLOW + "かいてき";
            } else {
                text = ChatColor.RED + "こんざつ";
            }
            if (txt.contains("<cpu>")) {
                txt = txt.replaceAll("<cpu>", text);
            } else {
                txt = txt.replaceAll("<tps>", text);
            }
        }
        if (txt.contains("<help>")) {
            txt = txt.replaceAll("<help>", "<name/pos/target/rank/hp/iteminhand/mine/jp/world/clock/cpu/lv/realtime/help>");
        }
        return txt;
    }

    public static String ChangeText(String text, MituyaProject plugin, PlayerInstance ins) {
        return Pronoun(SignColor(text), plugin, ins);
    }

    /**
     * CreatureTypeを取得します
     *
     * @param monstername モンスター名(CreatureType)
     * @return CreatureType
     */
    /*public static CreatureType getCreatureType(String monstername) {
     monstername = monstername.replaceAll("_", "");
     for (CreatureType type : CreatureType.values()) {
     if(type.toString().replaceAll("_", "").equalsIgnoreCase(monstername)) {
     return type;
     }
     }
     return null;
     }*/
    /**
     * intをkやmを使った単位に直して短くします
     *
     * @param number
     * @return
     */
    public static String IntegerToShortString(int number) {
        if (number >= 1000000) {
            return String.valueOf((float)number / 1000000) + "M";
        } else if (number >= 1000) {
            return String.valueOf((float)number / 1000) + "K";
        } else {
            return String.valueOf(number);
        }
    }

    public static ArrayList<String> mutchString(String pattern, String text) {
        Matcher m = Pattern.compile(pattern).matcher(text);
        ArrayList<String> list = new ArrayList<>();
        m.reset();
        while (m.find()) {
            list.add(text.substring(m.start() + 1, m.end() - 1));
        }
        return list;
    }

    /**
     * ItemStackから表示名を取得します。
     *
     * @param is アイテムスタック
     * @return 表示名
     */
    public static String GetItemName(ItemStack is) {
        String ret = "";
        if (is != null) {
            ItemMeta im = is.getItemMeta();

            if (im != null) {
                ret = im.getDisplayName();
            }

            if (ret == null || ret.equals("")) {
                MaterialData md = new MaterialData(is.getTypeId());
                ret = md.getItemType().name();
            }
        }

        return ret;
    }

    /**
     * get
     *
     * @param url
     * @return
     */
    public static StringBuilder get(String url) {
        StringBuilder builder = new StringBuilder();
        try {
            URL q = new URL(url);
            URLConnection urlc = q.openConnection();
            try (BufferedInputStream buffer = new BufferedInputStream(urlc.getInputStream())) {
                int byteRead;
                while ((byteRead = buffer.read()) != -1) {
                    builder.append((char)byteRead);
                }
            }
        }
        catch (MalformedURLException ex) {
            return null;
        }
        catch (IOException ex) {
            return null;
        }
        return builder;
    }

    /**
     * 金額を３桁のカンマ区切りの文字列として返します。
     *
     * @param amount
     * @return String
     */
    public static String FormatMine(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String result = formatter.format(amount);
        return result;
    }

    /**
     * 文字列が数字かどうか判断します
     *
     * @param num
     * @return
     */
    public static boolean isInteger(String num) {
        try {
            int n = Integer.parseInt(num);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static int Rand(int ran) {
        return (int)(Math.random() * ran);
    }
}
