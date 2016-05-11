/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.Bukkit;

import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 *
 * @author chantake
 */
public class TitleAPI {
    private static Class<?> packetClass = null;
    private static Class<?> componentClass = null;
    private static Class<?> packetTabClass = null;
    private static Class<?> serializerClass = null;
    private static Constructor<?> packetConstructor = null;
    private static Constructor<?> packetTabConstructor = null;
    private static Class<Enum> enumTitleAction = null;

    public static void sendTitle(Player p, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        packetClass = ReflectionAPI.getNmsClass("PacketPlayOutTitle");
        componentClass = ReflectionAPI.getNmsClass("IChatBaseComponent");
        serializerClass = ReflectionAPI.getNmsClass("IChatBaseComponent$ChatSerializer");
        enumTitleAction = (Class<Enum>) ReflectionAPI.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
        try {
            packetConstructor = packetClass.getConstructor(enumTitleAction, componentClass, int.class, int.class, int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
        }
        if (subtitle != null) {
            Object subTitleSer;
            Object subTitlePacket;
            try {
                subTitleSer = serializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
                subTitlePacket = packetConstructor.newInstance(enumTitleAction.getEnumConstants()[1], subTitleSer, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
                ReflectionAPI.sendPacket(p, subTitlePacket);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
            } catch (IllegalArgumentException e) {
                System.out.println(Arrays.toString(enumTitleAction.getEnumConstants()));
                MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
                System.out.println(Arrays.toString(enumTitleAction.getEnumConstants()));
            } catch (InstantiationException e) {
                MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
                System.out.println(Arrays.toString(enumTitleAction.getEnumConstants()));
            }
        }
        packetClass = ReflectionAPI.getNmsClass("PacketPlayOutTitle");
        componentClass = ReflectionAPI.getNmsClass("IChatBaseComponent");
        serializerClass = ReflectionAPI.getNmsClass("IChatBaseComponent$ChatSerializer");
        enumTitleAction = (Class<Enum>) ReflectionAPI.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
        try {
            packetConstructor = packetClass.getConstructor(enumTitleAction, componentClass, int.class, int.class, int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
        }
        if (subtitle != null) {
            Object subTitleSer;
            Object subTitlePacket;
            try {
                subTitleSer = serializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
                subTitlePacket = packetConstructor.newInstance(enumTitleAction.getEnumConstants()[1], subTitleSer, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
                ReflectionAPI.sendPacket(p, subTitlePacket);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
            } catch (IllegalArgumentException e) {
                System.out.println(Arrays.toString(enumTitleAction.getEnumConstants()));
                MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
                System.out.println(Arrays.toString(enumTitleAction.getEnumConstants()));
            } catch (InstantiationException e) {
                MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
                System.out.println(Arrays.toString(enumTitleAction.getEnumConstants()));
            }
        }
        if (title != null) {
            Object titleSer;
            Object titlePacket;
            try {
                titleSer = serializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
                titlePacket = packetConstructor.newInstance(enumTitleAction.getEnumConstants()[0], titleSer, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
                ReflectionAPI.sendPacket(p, titlePacket);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
                MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
            }
        }
    }
    public static void sendTabTitle(Player p, String header, String footer) {
        packetTabClass = ReflectionAPI.getNmsClass("PacketPlayOutPlayerListHeaderFooter");
        componentClass = ReflectionAPI.getNmsClass("IChatBaseComponent");
        serializerClass = ReflectionAPI.getNmsClass("IChatBaseComponent$ChatSerializer");
        try {
            packetTabConstructor = packetTabClass.getConstructor(componentClass);
        } catch (NoSuchMethodException | SecurityException e1) {
            MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e1);
        }
        if (header == null) header = "";
        if (footer == null) footer = "";
        Object tabTitle;
        Object tabFoot;
        Object headerPacket;
        try {
            tabTitle = serializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + header + "\"}");
            tabFoot = serializerClass.getMethod("a", String.class).invoke(null, "{\"text\": \"" + footer + "\"}");
            headerPacket = packetTabConstructor.newInstance(tabTitle);
            Field field = headerPacket.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(headerPacket, tabFoot);
            ReflectionAPI.sendPacket(p, headerPacket);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | NoSuchFieldException e) {
            MituyaProject.getInstance().ErrLog("TitleAPI Err:"+e);
        }
    }
}
