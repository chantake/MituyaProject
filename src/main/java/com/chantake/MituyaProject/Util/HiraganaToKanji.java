package com.chantake.MituyaProject.Util;

/**
 * Created by fumitti on 2015/01/30.
 */
public class HiraganaToKanji {
    private static boolean loadlib = false;

    static {
        try {
            System.loadLibrary("IMEforJava");
            HiraganaToKanji HTK = new HiraganaToKanji();
            HTK.init();
            loadlib = true;
        } catch (UnsatisfiedLinkError e) {
        }
    }

    public static String IME(String s) {
        if (loadlib) {
            HiraganaToKanji HTK = new HiraganaToKanji();
            try {
                return HTK.GetConversion(s);
            } catch (Exception e) {
                return s;
            }
        }
        return s;
    }

    public static void unload() {
        if (loadlib) {
            HiraganaToKanji HTK = new HiraganaToKanji();
            HTK.uninit();
            loadlib = false;
        }
    }

    private native String GetConversion(String s);

    private native void init();

    private native void uninit();
}
