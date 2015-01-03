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
package com.chantake.MituyaProject.World.Pack;

/**
 *
 * @author chantake
 */
public enum BookType {

    Item("item", "アイテムパックを開きますか？"),
    Command("cmd", "コマンドを実行しますか？");

    private BookType(String type, String check_msg) {
        this.type = type;
        this.check_msg = check_msg;
    }

    private final String type;
    private final String check_msg;

    /**
     * check message
     *
     * @return
     */
    public String getCheckMessage() {
        return check_msg;
    }

    /**
     * book type
     *
     * @return
     */
    public String getType() {
        return type;
    }

    public static BookType getType(String type) {
        for (BookType bt : BookType.values()) {
            if (bt.getType().equals(type)) {
                return bt;
            }
        }
        return null;
    }
}
