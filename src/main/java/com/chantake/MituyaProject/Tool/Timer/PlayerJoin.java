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

import com.chantake.MituyaProject.MituyaProject;

/**
 * Spout用の遅延メッセージ
 *
 * @author chantake
 * @version 1.0.0
 */
public class PlayerJoin implements Runnable {

    public MituyaProject plugin;

    public PlayerJoin(MituyaProject mp) {
        plugin = mp;
        //this.sp = sp;
    }

    @Override
    public void run() {
        // PlayerInstance ins = Instance.getInstanceString(sp.getName());
        //sp.sendNotification(sp.getName()+" joined the game.", ins.getLoginBMessage(), Parameter328.Icon_Login);//LoginMessage
    }
}
