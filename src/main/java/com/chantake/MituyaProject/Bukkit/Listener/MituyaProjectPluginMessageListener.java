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
package com.chantake.MituyaProject.Bukkit.Listener;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.MituyaModPacket.PacketParameter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 *
 * @author いんく
 */
public class MituyaProjectPluginMessageListener implements PluginMessageListener {

    MituyaProject plugin;

    public MituyaProjectPluginMessageListener(MituyaProject aThis) {
        plugin = aThis;
    }

    @Override
    public void onPluginMessageReceived(String string, Player pl, byte[] bytes) {

        ByteArrayInputStream os = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(os);

        System.out.println(string);
        PlayerInstance player = plugin.getInstanceManager().getInstance(pl);
        if (bytes[0] == 0x00 && bytes[1] == 0x00 && bytes[2] == PacketParameter.True[0]) {
            try {
                player.sendMessage("MituyaMod Enable!");
                dis.skipBytes(3);
                String version = dis.readUTF();
                player.setMituyaModVersion(version);
                player.setMituyaMod(true);
            }
            catch (IOException ex) {
            }

        } else if (bytes[0] == 0x00 && bytes[1] == 0x00 && bytes[2] == PacketParameter.False[0]) {
            player.sendMessage("MituyaMod Disable!");
            player.setMituyaModVersion(null);
            player.setMituyaMod(false);
        }

        if (bytes[0] == 0x00 && bytes[1] == 0x01) {
            if (bytes[2] == PacketParameter.True[0]) {
                if (player.isCheck()) {
                    player.executionCheckInstance();
                }
            } else if (bytes[2] == PacketParameter.False[0]) {
                player.removeCheckInstance();
                player.sendSystem("***キャンセルしました。***");
            }
        }
    }
}
