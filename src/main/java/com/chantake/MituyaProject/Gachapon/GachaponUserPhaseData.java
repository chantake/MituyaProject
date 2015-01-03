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
package com.chantake.MituyaProject.Gachapon;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ezura573
 */
public class GachaponUserPhaseData {

    private int phase = 0;
    private final List IDList = new ArrayList<>(); // IDリスト
    private long buynum = 0;                  // 購入数

    public void SetPhase(int phase) {
        this.phase = phase;
    }

    public int GetPhase() {
        return this.phase;
    }

    public List GetList() {
        return this.IDList;
    }

    public void SetBuyNum(long buynum) {
        this.buynum = buynum;
    }

    public long GetBuyNum() {
        return this.buynum;
    }

    public void IncBuyNum() {
        this.buynum++;
    }
}
