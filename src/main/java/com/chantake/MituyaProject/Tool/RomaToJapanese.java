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

import java.util.Objects;
import java.util.TreeMap;

/**
 *
 * @author chantake
 */
public class RomaToJapanese {

    private static RomaToJapanese instance = null;
    private final TreeMap<String, String> map = new TreeMap<>(new DataComparator());

    public RomaToJapanese() {
        //etc
        put("//?", "？");
        put("!", "！");
        put(",", "、");
        put("/.", "。");
        put("-", "ー");
        put("~", "～");
        //あ行
        put("a", "あ");
        put("i", "い");
        put("u", "う");
        put("e", "え");
        put("o", "お");
        //きゃ
        put("kya", "きゃ");
        put("kyu", "きゅ");
        put("kyo", "きょ");
        //しゃ
        put("sya", "しゃ");
        put("syi", "しぃ");
        put("syu", "しゅ");
        put("sye", "しぇ");
        put("syo", "しょ");
        put("sha", "しゃ");
        put("shi", "し");
        put("shu", "しゅ");
        put("she", "しぇ");
        put("sho", "しょ");
        //じょ
        put("zya", "じゃ");
        put("zyi", "じぃ");
        put("zyu", "じゅ");
        put("zye", "じぇ");
        put("zyo", "じょ");
        put("jya", "じゃ");
        put("jyi", "じぃ");
        put("jyu", "じゅ");
        put("jye", "じぇ");
        put("jyo", "じょ");
        put("ja", "じゃ");
        put("ju", "じゅ");
        put("je", "じぇ");
        put("jo", "じょ");
        //ちゃ
        put("tya", "ちゃ");
        put("tyi", "ちぃ");
        put("tyu", "ちゅ");
        put("tye", "ちぇ");
        put("tyo", "ちょ");
        put("cha", "ちゃ");
        put("chi", "ち");
        put("chu", "ちゅ");
        put("che", "ちぇ");
        put("cho", "ちょ");
        //てゃ
        put("tha", "てゃ");
        put("thu", "てゅ");
        put("tho", "てょ");
        //にゃ
        put("nya", "にゃ");
        put("nyu", "にゅ");
        put("nyo", "にょ");
        //ひゃ
        put("hya", "ひゃ");
        put("hyu", "ひゅ");
        put("hyo", "ひょ");
        //みゃ
        put("mya", "みゃ");
        put("myu", "みゅ");
        put("myo", "みょ");
        //りゃ
        put("rya", "りゃ");
        put("ryu", "りゅ");
        put("ryo", "りょ");
        //ぎゃ
        put("gya", "ぎゃ");
        put("gyu", "ぎゅ");
        put("gyo", "ぎょ");
        //ぢゃ
        put("dya", "ぢゃ");
        put("dyu", "ぢゅ");
        put("dyo", "ぢょ");
        //びゃ
        put("bya", "びゃ");
        put("byu", "びゅ");
        put("byo", "びょ");
        //ぴゃ
        put("pya", "ぴゃ");
        put("pyu", "ぴゅ");
        put("pyo", "ぴょ");
        //ふゃ
        put("fya", "ふゃ");
        put("fyu", "ふゅ");
        put("fyo", "ふょ");
        //か行
        put("ka", "か");
        put("ki", "き");
        put("ku", "く");
        put("ke", "け");
        put("ko", "こ");
        //さ行
        put("sa", "さ");
        put("si", "し");
        put("su", "す");
        put("se", "せ");
        put("so", "そ");
        //た行
        put("ta", "た");
        put("ti", "ち");
        put("tu", "つ");
        put("te", "て");
        put("to", "と");
        //な行
        put("nn", "ん");
        put("na", "な");
        put("ni", "に");
        put("nu", "ぬ");
        put("ne", "ね");
        put("no", "の");
        //は行
        put("ha", "は");
        put("hi", "ひ");
        put("hu", "ふ");
        put("he", "へ");
        put("ho", "ほ");
        //ま行
        put("ma", "ま");
        put("mi", "み");
        put("mu", "む");
        put("me", "め");
        put("mo", "も");
        //や行
        put("ya", "や");
        put("yi", "い");
        put("yu", "ゆ");
        //put("ye", "え");
        put("yo", "よ");
        //ら行
        put("ra", "ら");
        put("ri", "り");
        put("ru", "る");
        put("re", "れ");
        put("ro", "ろ");
        //わ行
        put("wa", "わ");
        put("wo", "を");
        put("n", "ん");
        //が行
        put("ga", "が");
        put("gi", "ぎ");
        put("gu", "ぐ");
        put("ge", "げ");
        put("go", "ご");
        //ざ行
        put("za", "ざ");
        put("zi", "じ");
        put("zu", "ず");
        put("ze", "ぜ");
        put("zo", "ぞ");
        put("ji", "じ");
        //だ行
        put("da", "だ");
        put("di", "ぢ");
        put("du", "づ");
        put("de", "で");
        put("do", "ど");
        //ぱ行
        put("ba", "ば");
        put("bi", "び");
        put("bu", "ぶ");
        put("be", "べ");
        put("bo", "ぼ");
        //ぱ行
        put("pa", "ぱ");
        put("pi", "ぴ");
        put("pu", "ぷ");
        put("pe", "ぺ");
        put("po", "ぽ");
        //ふぁ
        put("fa", "ふぁ");
        put("fi", "ふぃ");
        put("fu", "ふ");
        put("fe", "ふぇ");
        put("fo", "ふぉ");
        //ぁ
        put("xa", "ぁ");
        put("xi", "ぃ");
        put("xu", "ぅ");
        put("xe", "ぇ");
        put("xo", "ぉ");
        //ぁ
        put("la", "ぁ");
        put("li", "ぃ");
        put("lu", "ぅ");
        put("le", "ぇ");
        put("lo", "ぉ");
    }

    public static RomaToJapanese getInstance() {
        if (instance == null) {
            instance = new RomaToJapanese();
        }
        return instance;
    }

    private void put(String oldtext, String newtext) {
        map.put(oldtext, newtext);
        if (oldtext.length() > 1 && oldtext.charAt(0) != oldtext.charAt(1)) {
            map.put(oldtext.charAt(0) + oldtext, "っ" + newtext);
        }
    }

    public String Conversion(String roma) {
        //StringBuilder sb = new StringBuilder();
        String ret = "";
        for (String t : roma.split(" ")) {
            if (Objects.equals(t.substring(0, 1).toUpperCase(), t.substring(0, 1))) {
                ret += " " + t;
                continue;
            }
            for (String mp : map.keySet()) {
                t = t.replaceAll(mp, map.get(mp));
            }
            t = HiraganaToKanji.IME(t);
            ret += " " + t;
        }
        return ret.trim();
    }
}
