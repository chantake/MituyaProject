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
package com.chantake.MituyaProject.Util.MituyaModPacket;

/**
 *
 * @author いんく
 */
public class PacketParameter {
    //あとでenumに変える

	// S サーバー
    // C クライアント
    /**
     * Boolean *
     */
    public static final byte[] True = new byte[]{0x01};
    public static final byte[] False = new byte[]{0x02};
    public static final byte[] Null = new byte[]{0x03};

    /**
     * RegisterMod *
     */
    public static final byte[] register = new byte[]{0x00, 0x00};// S <=> C

    /**
     * YesNo *
     */
    public static final byte[] YesNoSend = new byte[]{0x00, 0x01};// S => C

    /**
     * SystemChat *
     */
    public static final byte[] SystemChat = new byte[]{0x00, 0x02};// S => C

    /**
     * ServerConnect *
     */
    public static final byte[] ConnectServer = new byte[]{0x00, 0x03};// S => C

    /**
     * DownLoadFiles *
     */
    public static final byte[] DownLoadFile = new byte[]{0x00, 0x04};// S=>C

    /**
     * DownLoad&Sound*
     */
    public static final byte[] PlayDownloadSound = new byte[]{0x00, 0x05};//S=>C
}
