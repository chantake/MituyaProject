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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

public class HexTool {

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /*
     * public static String toStringFromAscii(byte[] bytes) { char[] ret = new
     * char[bytes.length]; for (int x = 0; x < bytes.length; x++) { if (bytes[x]
     * < 32 && bytes[x] >= 0) { ret[x] = '.'; } else { int chr = ((short)
     * bytes[x]) & 0xFF; ret[x] = (char) chr; } } return String.valueOf(ret); }
     * public static String toPaddedStringFromAscii(byte[] bytes) { String str =
     * toStringFromAscii(bytes); StringBuilder ret = new
     * StringBuilder(str.length() * 3); for (int i = 0; i <
     * DoubleByteLanguage.getlength(str); i++) { ret.append(str.charAt(i));
     * ret.append("  "); } return ret.toString(); }
     */
    public static byte[] getByteArrayFromHexString(String hex) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nexti = 0;
        int nextb = 0;
        boolean highoc = true;
        outer:
        for (;;) {
            int number = -1;
            while (number == -1) {
                if (nexti == hex.length()) {
                    break outer;
                }
                final char chr = hex.charAt(nexti);
                if (chr >= '0' && chr <= '9') {
                    number = chr - '0';
                } else if (chr >= 'a' && chr <= 'f') {
                    number = chr - 'a' + 10;
                } else if (chr >= 'A' && chr <= 'F') {
                    number = chr - 'A' + 10;
                } else {
                    number = -1;
                }
                nexti++;
            }
            if (highoc) {
                nextb = number << 4;
                highoc = false;
            } else {
                nextb |= number;
                highoc = true;
                baos.write(nextb);
            }
        }
        return baos.toByteArray();
    }

    private static String hashWithDigest(String in, String digest) {
        try {
            final MessageDigest Digester = MessageDigest.getInstance(digest);
            Digester.update(in.getBytes("UTF-8"), 0, in.length());
            final byte[] sha1Hash = Digester.digest();
            return HexTool.toSimpleHexString(sha1Hash);
        }
        catch (final NoSuchAlgorithmException ex) {
            throw new RuntimeException("Hashing the password failed", ex);
        }
        catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding the string failed", e);
        }
    }

    /**
     * 文字列をSHA-1で暗号化します
     *
     * @param text 暗号化する文字列
     * @return String 暗号化された文字列
     * @author chantake
     * @since 2011/03/23
     */
    public static String hexSha1(String text) {
        return HexTool.hashWithDigest(text, "SHA-1");
    }

    /**
     * 文字列をSHA-512で暗号化します
     *
     * @param in 暗号化する文字列
     * @return String 暗号化された文字列
     * @author chantake
     * @since 2011/03/23
     */
    public static String hexSha512(String in) {
        return HexTool.hashWithDigest(in, "SHA-512");
    }

    public static int Object_to_Integer(Object ob) {
        return ((Integer)ob);
    }

    /**
     * 指定文字数で右詰めした文字列を返す。
     *
     * @param source 右詰めする文字列
     * @param length 文字数
     * @param padding 詰める文字
     * @return 右詰めした文字列。sourceの文字数がlengthより大きいときはsourceをそのまま返す。
     */
    public static String rightJust(String source, int length, char padding) {
        if (source.length() >= length) {
            return source;
        }
        final int count = length - source.length();
        final StringBuilder buff = new StringBuilder();
        for (int i = 0; i < count; i++) {
            buff.append(padding);
        }
        return buff.append(source).toString();
    }

    private static String toSimpleHexString(byte[] bytes) {
        return HexTool.toString(bytes).replace(" ", "").toLowerCase();
    }

    private static String toString(byte byteValue) {
        final int tmp = byteValue << 8;
        final char[] retstr = new char[]{HexTool.HEX[tmp >> 12 & 0x0F],
            HexTool.HEX[tmp >> 8 & 0x0F]};
        return String.valueOf(retstr);
    }

    public static String toString(byte[] bytes) {
        final StringBuilder hexed = new StringBuilder();
        for (final byte b : bytes) {
            hexed.append(HexTool.toString(b));
            hexed.append(' ');
        }
        return hexed.substring(0, hexed.length() - 1);
    }

    public ChatColor color;

    /**
     *
     * @param co
     * @return
     * @author chantake
     * @since 2011/03/23
     */
    public ChatColor getColor(int co) {
        /*
         * 0 AQUA 1 BLACK 2 GOLD 3 GRAY 4 GREEN 5 LIGHT_PURPLE 6 RED 7 WHITE 8
         * YELLOW 90 DARK_AQUA 91 DARK_BLUE 92 DARK_GRAY 93 DARK_GREEN 94
         * DARK_PURPLE 95 DARK_RED
         */
        switch (co) {
            case 0:
                return ChatColor.AQUA;
            case 1:
                return ChatColor.BLACK;
            case 2:
                return ChatColor.GOLD;
            case 3:
                return ChatColor.GRAY;
            case 4:
                return ChatColor.GREEN;
            case 5:
                return ChatColor.LIGHT_PURPLE;
            case 6:
                return ChatColor.RED;
            case 7:
                return ChatColor.WHITE;
            case 8:
                return ChatColor.YELLOW;
            case 90:
                return ChatColor.DARK_AQUA;
            case 91:
                return ChatColor.DARK_BLUE;
            case 92:
                return ChatColor.DARK_GRAY;
            case 93:
                return ChatColor.DARK_GREEN;
            case 94:
                return ChatColor.DARK_PURPLE;
            case 95:
                return ChatColor.DARK_RED;
        }
        return ChatColor.WHITE;
    }

    /**
     * StringからChatColorを取り出します
     *
     * @param co
     * @return
     * @author chantake
     * @since 2011/03/23
     */
    public ChatColor getColor(String co) {
        boolean dark = false;
        co = StringUtils.stripStart(co, "_");
        if (co.startsWith("dark")) {
            co = StringUtils.stripStart(co, "dark");
            dark = true;
        } else if (co.startsWith("d")) {
            dark = true;
        }
        if (dark) {
            if (co.equalsIgnoreCase("aqua") || co.equalsIgnoreCase("aq")) {
                return ChatColor.DARK_AQUA;
            } else if (co.equalsIgnoreCase("blue") || co.equalsIgnoreCase("be")) {
                return ChatColor.DARK_BLUE;
            } else if (co.equalsIgnoreCase("gold") || co.equalsIgnoreCase("go")) {
                return ChatColor.GOLD;
            } else if (co.equalsIgnoreCase("gray") || co.equalsIgnoreCase("gr")) {
                return ChatColor.DARK_GRAY;
            } else if (co.equalsIgnoreCase("green") || co.equalsIgnoreCase("gr")) {
                return ChatColor.DARK_GREEN;
            } else if (co.equalsIgnoreCase("purple") || co.equalsIgnoreCase("pe")) {
                return ChatColor.DARK_PURPLE;
            } else if (co.equalsIgnoreCase("red") || co.equalsIgnoreCase("rd")) {
                return ChatColor.DARK_RED;
            } else {
                return ChatColor.WHITE;
            }
        }
        if (co.equalsIgnoreCase("aqua") || co.equalsIgnoreCase("aq")) {
            return ChatColor.AQUA;
        } else if (co.equalsIgnoreCase("black") || co.equalsIgnoreCase("bk")) {
            return ChatColor.BLACK;
        } else if (co.equalsIgnoreCase("blue") || co.equalsIgnoreCase("be")) {
            return ChatColor.BLUE;
        } else if (co.equalsIgnoreCase("gold") || co.equalsIgnoreCase("go")) {
            return ChatColor.GOLD;
        } else if (co.equalsIgnoreCase("gray") || co.equalsIgnoreCase("gr")) {
            return ChatColor.GRAY;
        } else if (co.equalsIgnoreCase("green") || co.equalsIgnoreCase("gr")) {
            return ChatColor.GREEN;
        } else if (co.equalsIgnoreCase("lightpurple") || co.equalsIgnoreCase("lp")) {
            return ChatColor.LIGHT_PURPLE;
        } else if (co.equalsIgnoreCase("red") || co.equalsIgnoreCase("rd")) {
            return ChatColor.RED;
        } else if (co.equalsIgnoreCase("yellow") || co.equalsIgnoreCase("ye")) {
            return ChatColor.YELLOW;
        } else if (co.equalsIgnoreCase("white") || co.equalsIgnoreCase("wh")) {
            return ChatColor.WHITE;
        } else {
            return ChatColor.WHITE;
        }
    }
}
