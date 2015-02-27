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

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PassBookExt<T> implements PackBookImpl {

    private final BookType type;
    private String title;
    private List<String> data;

    protected PassBookExt(BookType type, String title, List<String> data) {
        this.type = type;
        this.title = title;
        this.data = data;
    }

    @SafeVarargs
    protected PassBookExt(BookType type, String title, String key, T... datas) {
        this.type = type;
        this.title = title;
        YamlConfiguration config = new YamlConfiguration();
        this.data = new ArrayList<>(datas.length);
        for (T dt : datas) {
            config.set(key, dt);
            data.add(config.saveToString());
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public BookType getType() {
        return this.type;
    }

    @Override
    public List<String> getData() {
        return this.data;
    }

    @Override
    public void setData(List<String> data) {
        this.data = data;
    }
}
