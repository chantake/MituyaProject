/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chantake.getuuid;

import java.util.HashMap;

/**
 *
 * @author chantake
 */
public class Player {
    private HashMap data;
    private String id;
    private String name;
    private String first_name;
    private String family_name;
    private String world;
    private String link;
    
    public Player(HashMap data) {
        this.data = data;
        this.id = this.getDataString("id");
        this.name = this.getDataString("title");
        this.link = this.getDataString("link");
        this.first_name = this.name.split(" ")[0];
        this.family_name = this.name.split(" ")[1];
    }

    public Player(String id, String first_name, String family_name, String world, String link) {
        this.id = id;
        this.first_name = first_name;
        this.family_name = family_name;
        this.world = world;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }
    
    private String getDataString(String key) {
        return (String) this.data.get(key);
    }
    
    private int getDataInteger(String key) {
        return Integer.valueOf(this.getDataString(key));
    }

    @Override
    public String toString() {
        return "id:"+id+" name:"+name+" link:"+link;
    }
}
