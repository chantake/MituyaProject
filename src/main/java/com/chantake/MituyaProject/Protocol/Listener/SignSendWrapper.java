package com.chantake.MituyaProject.Protocol.Listener;

import com.chantake.MituyaProject.MituyaProject;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

/**
 * Created by fumitti on 2015/02/10.
 */
public class SignSendWrapper extends PacketAdapter {
    private static final PacketType type = PacketType.Play.Server.UPDATE_SIGN;
    private final MituyaProject plugin;

    public SignSendWrapper(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, Arrays.asList(new PacketType[]{type}), ListenerOptions.ASYNC);
        this.plugin = (MituyaProject)plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() == type) {
            PacketContainer signUpdatePacket = event.getPacket();
            Player player = event.getPlayer();
            String[] lines = getLinesAsStrings(signUpdatePacket);
            for (int i = 0; i < 4; ++i) {
                String line = lines[i];
                line = line.replace("\\u003cPLAYER\\u003e", player.getName());
                line = line.replace("\\u003cDISPLAY\\u003e", ChatColor.WHITE + player.getDisplayName()+ChatColor.RESET);
                line = line.replace("\\u003cMINE\\u003e", Long.toString(plugin.getInstanceManager().getInstance(player).getMine()));
                lines[i] = line;
            }
            setLinesFromStrings(signUpdatePacket, lines);
        }
    }

    public static BlockPosition getLocation(PacketContainer updateSignPacket) {
        return (BlockPosition) updateSignPacket.getBlockPositionModifier().read(0);
    }

    public static void setLocation(PacketContainer updateSignPacket, BlockPosition blockPosition) {
        updateSignPacket.getBlockPositionModifier().write(0, blockPosition);
    }

    public static WrappedChatComponent[] getLines(PacketContainer updateSignPacket) {
        return (WrappedChatComponent[]) updateSignPacket.getChatComponentArrays().read(0);
    }

    public static void setLines(PacketContainer updateSignPacket, WrappedChatComponent[] lines) {
        assert lines != null;

        assert lines.length == 4;

        updateSignPacket.getChatComponentArrays().write(0, lines);
    }

    public static String[] getLinesAsStrings(PacketContainer updateSignPacket) {
        String[] lines = new String[4];
        WrappedChatComponent[] rawLines = getLines(updateSignPacket);

        for (int i = 0; i < 4; ++i) {
            lines[i] = rawLines[i] == null ? "" : rawLines[i].getJson();
        }

        return lines;
    }

    public static void setLinesFromStrings(PacketContainer updateSignPacket, String[] lines) {
        assert lines != null;

        assert lines.length == 4;

        WrappedChatComponent[] rawLines = new WrappedChatComponent[4];

        for (int i = 0; i < 4; ++i) {
            rawLines[i] = lines[i].isEmpty() ? null : WrappedChatComponent.fromJson(lines[i]);
        }

        setLines(updateSignPacket, rawLines);
    }
}
