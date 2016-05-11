/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.Bukkit;

import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionAPI {
    private static final HashMap<Class<? extends Entity>, Method> handles = new HashMap<>();
    private static Field player_connection = null;
    private static Method player_sendPacket = null;
    public static String getVersion(){
        String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
        if (array.length == 4)
            return array[3] + ".";
        return "";
    }
    public static Class<?> getNmsClass(String name){
        String version = getVersion();
        String className = "net.minecraft.server." + version + name;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException ex){
            MituyaProject.getInstance().ErrLog("ReflectionAPI Err:"+ex);
        }
        return clazz;
    }
    public static Field getFirstFieldByType(Class<?> clazz, Class<?> type){
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == type) {
                return field;
            }
        }
        return null;
    }
    public static Object getHandle(Entity entity){
        try {
            if (handles.get(entity.getClass()) != null)
                return handles.get(entity.getClass()).invoke(entity);
            else {
                Method entity_getHandle = entity.getClass().getMethod("getHandle");
                handles.put(entity.getClass(), entity_getHandle);
                return entity_getHandle.invoke(entity);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            MituyaProject.getInstance().ErrLog("ReflectionAPI Err:"+ex);
            return null;
        }
    }
    public static void sendPacket(Player p, Object packet) throws IllegalArgumentException {
        try {
            if (player_connection == null){
                player_connection = ReflectionAPI.getHandle(p).getClass().getField("playerConnection");
                for (Method m : player_connection.get(ReflectionAPI.getHandle(p)).getClass().getMethods()){
                    if (m.getName().equalsIgnoreCase("sendPacket")){
                        player_sendPacket = m;
                    }
                }
            }
            player_sendPacket.invoke(player_connection.get(ReflectionAPI.getHandle(p)), packet);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException ex){
            MituyaProject.getInstance().ErrLog("ReflectionAPI Err:"+ex);
        }
    }
}
