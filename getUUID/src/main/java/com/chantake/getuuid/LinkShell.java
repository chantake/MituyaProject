/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chantake.getuuid;

import java.util.HashMap;

/**
 * linkshell データクラス
 * @author chantake
 */
public class LinkShell {
    private String id;
    private String name;
    private int count;
    private String link;
    private HashMap data;
    
    public LinkShell(String id, String name, int count, String link) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.link = link;
    }

    public LinkShell(HashMap item) {
        this.data = item;
        this.id = this.getDataString("id");
        this.name = this.getDataString("title");
        this.count = this.getDataInteger("count");
        this.link = this.getDataString("link");
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public HashMap getData() {
        return data;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }
    
    private String getDataString(String key) {
        return (String) this.data.get(key);
    }
    
    private int getDataInteger(String key) {
        return Integer.valueOf(this.getDataString(key));
    }

    @Override
    public String toString() {
        return "id:"+id+" name:"+name+" count:"+count+" link:"+link;
    }
}
