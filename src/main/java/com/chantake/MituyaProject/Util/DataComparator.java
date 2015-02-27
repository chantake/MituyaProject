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
package com.chantake.MituyaProject.Util;

import java.util.Comparator;

/**
 *
 * @author chantake
 */
public class DataComparator implements Comparator<String> {

    public DataComparator() {
    }

    /**
     * key 2つが与えられたときに、その value で比較
     *
     * @param key1
     * @param key2
     * @return
     */
    @Override
    public int compare(String key1, String key2) {
        // value を取得
        int value1 = key1.length();
        int value2 = key2.length();
        // value の降順, valueが等しいときは key の辞書順
        if (value1 == value2) {
            return key1.toLowerCase().compareTo(key2.toLowerCase());
        } else if (value1 < value2) {
            return 1;
        } else {
            return -1;
        }
    }

}
