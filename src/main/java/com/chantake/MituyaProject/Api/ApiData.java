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

/**
 *
 * @author chantake
 */
public class ApiData {

    private final String name;//apiの名前
    private final String ver;//バージョン
    private final String autor;//作者
    private final String web;//hp
    private final String description;//説明
    private String tokenkey;//tokenkey
    private String tokenSecret;//tokenSecret

    public ApiData(String name, String ver, String autor, String web, String description, String tokenkey, String tokenSecret) {
        this.name = name;
        this.ver = ver;
        this.autor = autor;
        this.web = web;
        this.description = description;
        this.tokenkey = tokenkey;
        this.tokenSecret = tokenSecret;
    }

    public void accessToken(String tokenkey, String tokenSecret) {
        this.tokenkey = tokenkey;
        this.tokenSecret = tokenSecret;
    }

    public String getName() {
        return name;
    }

    public String getVer() {
        return ver;
    }

    public String getAutor() {
        return autor;
    }

    public String getDescription() {
        return description;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public String getTokenKey() {
        return tokenkey;
    }

    public String getWeb() {
        return web;
    }

}
