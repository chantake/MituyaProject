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
package com.chantake.MituyaProject.Api;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author chantake
 */
public class DataConText {

    private final JSONObject data;

    public DataConText(JSONObject data) {
        this.data = data;
    }

    public DataConText(Object data) {
        this.data = (JSONObject)data;
    }

    public DataConText(String data) {
        this.data = (JSONObject)JSONValue.parse(data);
    }

    public DataConText() {
        this.data = new JSONObject();
    }

    public String getHead() {
        return (String)this.data.get("head");
    }

    public Object getRowData() {
        return this.data.get("data");
    }

    public String getStringData() {
        return this.getRowData().toString();
    }

    public int getIntegerData() {
        return new Integer(this.getStringData());
    }

    public void setData(Object data) {
        this.data.put("data", data);
    }

    public Object getObject(String key) {
        return this.data.get(key);
    }

    public JSONObject getJSONObject() {
        return this.data;
    }

    @Override
    public String toString() {
        return this.data.toJSONString();
    }

}
