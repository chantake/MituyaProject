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
import com.chantake.MituyaProject.Player.PlayerInstance;

/**
 * ボス戦(エンダードラゴン）用スレッド
 *
 * @author chantake
 */
public class EnderDragonEvent implements Runnable {

    private final PlayerInstance reader;
    private final MituyaProject plugin;

    public EnderDragonEvent(PlayerInstance reader, MituyaProject plugin) {
        this.reader = reader;
        this.plugin = plugin;
    }

    @Override
    public void run() {

    }

}
