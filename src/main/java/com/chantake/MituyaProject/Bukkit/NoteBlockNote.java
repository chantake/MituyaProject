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
package com.chantake.MituyaProject.Bukkit;

/**
 *
 * @author chantake
 */
public class NoteBlockNote {

    public static byte GetNoteData(String name) {
        String txt = name;
        byte data = 0;
        //半音
        if (name.endsWith("#") || name.endsWith("＃")) {
            txt = name.substring(0, name.length() - 1);
            data++;
        } else if (name.endsWith("シャープ")) {
            txt = name.substring(0, name.length() - 4);
            data++;
        } else if (name.endsWith("フラット")) {
            txt = name.substring(0, name.length() - 4);
            data--;
        } else if (name.endsWith("♭") || name.endsWith("f")) {
            txt = name.substring(0, name.length() - 1);
            data--;
        }

        if (name.charAt(0) == '8') {
            if (name.startsWith("8va")) {
                txt = txt.substring(3);
            } else {
                txt = txt.substring(1);
            }
            data += 12;
        }
        boolean found = false;

        if (txt.startsWith("ファ") || txt.startsWith("F") || txt.startsWith("f") || txt.startsWith("hula") || txt.startsWith("huxa") || txt.startsWith("ヘ")) {
            if ((txt.endsWith("1") || txt.endsWith("１")) && data == 1) {
                data = 12;
            } else if ((txt.endsWith("2") || txt.endsWith("２"))) {
                data += 23;
            } else if (data <= 0) {
                data += 11;
            } else {
                data = 0;
            }
            found = true;
        } else if (name.contains("2")) {
            txt = txt.replace("2", "");
            data += 12;
        }
        if (!found) {
            if (txt.equalsIgnoreCase("ソ") || txt.equalsIgnoreCase("G") || txt.equalsIgnoreCase("so") || txt.equalsIgnoreCase("ト")) {
                data += 1;
            } else if (txt.equalsIgnoreCase("ラ") || txt.equalsIgnoreCase("A") || txt.equalsIgnoreCase("ra") || txt.equalsIgnoreCase("イ")) {
                data += 3;
            } else if (txt.equalsIgnoreCase("シ") || txt.equalsIgnoreCase("B") || txt.equalsIgnoreCase("si") || txt.equalsIgnoreCase("ロ")) {
                data += 5;
            } else if (txt.equalsIgnoreCase("ド") || txt.equalsIgnoreCase("C") || txt.equalsIgnoreCase("do") || txt.equalsIgnoreCase("ハ")) {
                data += 6;
            } else if (txt.equalsIgnoreCase("レ") || txt.equalsIgnoreCase("D") || txt.equalsIgnoreCase("re") || txt.equalsIgnoreCase("ニ")) {
                data += 8;
            } else if (txt.equalsIgnoreCase("ミ") || txt.equalsIgnoreCase("E") || txt.equalsIgnoreCase("mi") || txt.equalsIgnoreCase("ホ")) {
                data += 10;
            } else {
                return -1;
            }
        }

        if (data == 25) {
            data = 0;
        } else if (data > 25) {
            data = (byte)(data - 12);
        }
        return data;
    }

    public static String GetNote(byte note, boolean rightclick) {
        byte val;
        if (rightclick) {
            val = (byte)(note + 1);
        } else {
            val = note;
        }

        if (val == 25) {
            val = 0;
        }
        switch (val) {
            case 0:
                return "ファ＃   F#  (" + val + ")";
            case 1:
                return "ソ　　   G    (" + val + ")";
            case 2:
                return "ソ＃　   G#  (" + val + ")";
            case 3:
                return "ラ　　   A    (" + val + ")";
            case 4:
                return "ラ＃　   A# (" + val + ")";
            case 5:
                return "シ　　   B    (" + val + ")";
            case 6:
                return "ド　　   C    (" + val + ")";
            case 7:
                return "ド＃　   C#  (" + val + ")";
            case 8:
                return "レ　　   D    (" + val + ")";
            case 9:
                return "レ＃　   D#  (" + val + ")";
            case 10:
                return "ミ　　   E    (" + val + ")";
            case 11:
                return "ファ　   F    (" + val + ")";
            case 12:
                return "ファ1＃ F1# (" + val + ")";
            case 13:
                return "ソ2　　 G2   (" + val + ")";
            case 14:
                return "ソ2＃　 G2# (" + val + ")";
            case 15:
                return "ラ2　　 A2   (" + val + ")";
            case 16:
                return "ラ2＃　 A2# (" + val + ")";
            case 17:
                return "シ2　　 B2   (" + val + ")";
            case 18:
                return "ド2　　 C2   (" + val + ")";
            case 19:
                return "ド2＃　 C2# (" + val + ")";
            case 20:
                return "レ2　　 D2   (" + val + ")";
            case 21:
                return "レ2＃　 D2# (" + val + ")";
            case 22:
                return "ミ2　　 E2   (" + val + ")";
            case 23:
                return "ファ2　 F2   (" + val + ")";
            case 24:
                return "ファ2＃ F2# (" + val + ")";
        }
        return "音階が見つかりません!!";
    }
}
