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
package com.chantake.MituyaProject.Tool.Timer;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Tool.Tools;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author chantake
 */
public final class Tips extends BukkitRunnable {

    public MituyaProject plugin;
    public ArrayList<String> tip = new ArrayList<>();
    public int index;

    public Tips(MituyaProject mp) {
        plugin = mp;
        tip.add("放置はnew_worldのデフォルトリスポで！(ダメージ無効＆餓死しないよ！)");
        tip.add("死んだ時は /spawn back で取りにいこう!!");
        tip.add("チェストショップを有効活用しよう！");
        //tip.add("動的マップを活用しよう！ (http://map.328mss.com/)");
        tip.add("ホームを設定しましょう (/home set)");
        tip.add("不具合報告などはフォーラムへ　http://forum.328mss.com/forums/");
        tip.add("Wikiの編集大歓迎！自分の町の情報を載せちゃったり！？　http://wiki.328mss.com/");
        tip.add("ブロックを掘って ジャックポットを当てよう！！ (/jackpot)");
        tip.add("不具合等ありましたらこちらに登録してチケットを発行してください http://redmine.328mss.com/");
        //tip.add("毎週月曜 24:00 ~ 26:00 は定期メンテナンスです");
        //tip.add("328一周年企画のアンケート実施中！328HPﾌｫｰﾗﾑの[ｲﾍﾞﾝﾄ企画,宣伝]のｲﾍﾞﾝﾄﾄﾋﾟｯｸまで！");
        //tip.add("みつやプラグイン漢字完全対応！！");
        tip.add("只今の時刻は <realtime> です");
        tip.add("<jpnowparsentYear>");
    }

    @Override
    public void run() {
        String tt = "[" + ChatColor.YELLOW + "Tip" + ChatColor.WHITE + "] " + ChatColor.YELLOW;
        plugin.broadcastMessage(tt + Tools.Pronoun(tip.get(index), plugin));
        if (index >= tip.size() - 1) {
            index = 0;
        } else {
            index++;
        }
    }
}
