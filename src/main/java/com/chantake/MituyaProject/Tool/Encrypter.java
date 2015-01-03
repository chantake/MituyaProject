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
package com.chantake.MituyaProject.Tool;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * ハッシュ値生成機能を提供
 *
 * @auther chantake
 */
public class Encrypter {

    public static enum Algorithm {

        /**
         * メッセージダイジェスト：MD5
         */
        MD5("MD5"),
        /**
         * メッセージダイジェスト：SHA-1
         */
        Sha1("SHA-1"),
        /**
         * メッセージダイジェスト：SHA-256
         */
        SHA256("SHA-256"),
        /**
         * メッセージダイジェスト：SHA-384
         */
        SHA384("SHA-384"),
        /**
         * メッセージダイジェスト：SHA-512
         */
        SHA512("SHA-512"),
        SHA1PRNG("SHA1PRNG");
        public String algrithm;

        private Algorithm(String algorithm) {
            this.algrithm = algorithm;
        }

        @Override
        public String toString() {
            return this.algrithm;
        }
    }

    /**
     * ハッシュ値を返す
     *
     * @param org 計算元文字列
     * @param algorithm ハッシュアルゴリズム名(Encrypter.ALG_xxxで取得できる)
     * @return ハッシュ値
     */
    public static String getHash(String org, Algorithm algorithm) {
        // 引数・アルゴリズム指定が無い場合は計算しない
        if (org == null || algorithm == null) {
            return null;
        }

        // 初期化
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm.toString());
        }
        catch (final NoSuchAlgorithmException e) {
            return null;
        }

        md.reset();
        md.update(org.getBytes());
        final byte[] hash = md.digest();

        // ハッシュを16進数文字列に変換
        final StringBuilder sb = new StringBuilder();
        final int cnt = hash.length;
        for (int i = 0; i < cnt; i++) {
            sb.append(Integer.toHexString(hash[i] >> 4 & 0x0F));
            sb.append(Integer.toHexString(hash[i] & 0x0F));
        }
        return sb.toString();
    }

    /**
     * 暗号化します
     *
     * @param key 共通鍵
     * @param text 暗号化するテキスト
     * @return
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws java.security.InvalidKeyException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.NoSuchPaddingException
     */
    private static byte[] encrypt(String key, String text)
            throws javax.crypto.IllegalBlockSizeException,
            java.security.InvalidKeyException,
            java.security.NoSuchAlgorithmException,
            java.io.UnsupportedEncodingException,
            javax.crypto.BadPaddingException,
            javax.crypto.NoSuchPaddingException {
        javax.crypto.spec.SecretKeySpec sksSpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "Blowfish");
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("Blowfish");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sksSpec);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return encrypted;
    }

    /**
     * 復号化します。
     *
     * @param key 共通鍵
     * @param encrypted
     * @return
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws java.security.InvalidKeyException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.NoSuchPaddingException
     */
    private static String decrypt(String key, byte[] encrypted)
            throws javax.crypto.IllegalBlockSizeException,
            java.security.InvalidKeyException,
            java.security.NoSuchAlgorithmException,
            java.io.UnsupportedEncodingException,
            javax.crypto.BadPaddingException,
            javax.crypto.NoSuchPaddingException {
        javax.crypto.spec.SecretKeySpec sksSpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "Blowfish");
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("Blowfish");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, sksSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    /**
     * 公開鍵と秘密鍵を生成します。
     *
     * @return keyPair 公開鍵と秘密鍵
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getkey() throws NoSuchAlgorithmException {
        // キーペアの生成
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance(Algorithm.SHA1PRNG.toString());
        generator.initialize(1024, random);
        KeyPair keyPair = generator.generateKeyPair();

// 秘密鍵・公開鍵の取得
        //PrivateKey privateKey = keyPair.getPrivate();
        //PublicKey publicKey = keyPair.getPublic();
        return keyPair;
    }

    /**
     * byte型のkeyよりPublicKeyを復元します。
     *
     * @param key 保存しているbyte型のkey
     * @return PublicKey
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(byte[] key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * byte型のkeyよりPrivatekeyを復元します。
     *
     * @param key 保存しているbyte型のkey
     * @return PrivateKey
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivatekey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 公開鍵を使って暗号化します。
     *
     * @param text 暗号化するデータ
     * @param key PublicKey公開鍵
     * @return 暗号文 byte[]
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] Encrypt(byte[] text, PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 暗号化の実行
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(text);
        return encrypted;
    }

    /**
     * 秘密鍵を使って複合化します
     *
     * @param text 複合化するデータ
     * @param key PrivateKey 秘密鍵
     * @return 複合化したデータ
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] Decrypt(byte[] text, PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] binary = cipher.doFinal(text);
        return binary;
    }
}
