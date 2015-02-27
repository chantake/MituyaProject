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
package com.chantake.MituyaProject.Util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/**
 * 便利なデバック向け機能をえずらが提供します！！(キリッ
 *
 * @author ezura573
 */
public class EzuraDebug {

	// <editor-fold defaultstate="collapsed"
    // desc="writeXML(String path, Object object) オブジェクトをXML形式で指定されたパスに保存します。">
	// <editor-fold defaultstate="collapsed"
    // desc="readXML(String path) 指定されたパスからXMLを読み込みObject形式で返します。">
    /**
     * 指定したパス[path]のXMLファイルから、オブジェクトを復元します。 ※XMLファイルはjava.beans.XMLEncoderで保存している必要があります。
     *
     * @param path オブジェクトが保存されているパス。
     * @return
     * @throws FileNotFoundException 指定されたパス名で示されるファイルが存在しない場合
     */
    public static Object readXML(String path) throws FileNotFoundException {
        try (XMLDecoder d = new XMLDecoder(new BufferedInputStream(
                new FileInputStream(path)))) {
            return d.readObject();
        }
    }

	// </editor-fold>
	// </editor-fold>
    /**
     * java.beans.XMLEncoderを使用し、オブジェクト[object]を、 指定したパス[path]に、XMLファイルとして保存します。 ※オブジェクト[object]がJavaBeansの慣例に適合している場合、 プライベートなフィールドのデータも保存できます。
     * ※保存したXMLファイルはjava.beans.XMLDecoderで復元する事ができます。
     *
     * @param path オブジェクトを保存するパス。 存在しない場合可能であれば作成します。
     * @param object 保存するオブジェクト。
     * @throws FileNotFoundException 指定されたパス名で示されるファイルが開けなかった場合
     */
    public static synchronized void writeXML(String path, Object object)
            throws IOException {
        XMLEncoder enc = null;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream fileStream = null;
        try {
            // 先にメモリ中にXMLデータを出力する
            enc = new XMLEncoder(out);
            enc.writeObject(object);
            enc.close(); // close()の中でflush()が呼ばれる。
            final byte[] xmlbuff = out.toByteArray();

            // バイトデータをファイルに出力
            fileStream = new FileOutputStream(path);
            fileStream.write(xmlbuff);
            fileStream.flush();
        }
        finally {
            if (enc != null) {
                enc.close();
            }
            out.close();
            if (fileStream != null) {
                fileStream.close();
            }
        }
    }

}
