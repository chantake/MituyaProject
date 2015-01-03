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

import com.chantake.MituyaProject.Tool.PerformanceMonitor;

/**
 *
 * @author chantake
 */
public class PerformanceMonitorThread implements Runnable {

    private final PerformanceMonitor pm;

    public PerformanceMonitorThread(PerformanceMonitor pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        pm.elapsedtime = time - pm.prevtime;
        pm.elapsedtimesec = (double)pm.elapsedtime / 1000;
        double tps = pm.monitorInterval / pm.elapsedtimesec;
        pm.tps = tps;
        pm.prevtime = time;
        pm.getPlugin().broadcastDebugMessage("tps:" + tps);
    }

}
