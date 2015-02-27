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
package com.chantake.MituyaProject.Timer;

import com.chantake.MituyaProject.Player.PlayerInstance;

/**
 * @author chantake
 */
public class CancelCheckInstance implements Runnable {

    private final PlayerInstance ins;

    public CancelCheckInstance(PlayerInstance ins) {
        this.ins = ins;
    }

    @Override
    public void run() {
        //確認メッセージを削除
        ins.removeCheckInstance();
        //コマンドも削除
        ins.removeCommandTasks();
        //プレーヤーに通知
        ins.sendAttention("時間が経ったのでキャンセルされました。もう一度実行してください。");
    }

}
