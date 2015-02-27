/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.Util;

/**
 *
 * @author chantake
 */
public class MathHelper {

    private static float[] a = new float[65536];

    public MathHelper() {
    }

    public static float sin(float f) {
        return a[(int) (f * 10430.378F) & '\uffff'];
    }

    public static float cos(float f) {
        return a[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float sqrt_float(float f) {
        return (float) Math.sqrt((double) f);
    }

    public static float sqrt_double(double d0) {
        return (float) Math.sqrt(d0);
    }

    public static int floor_float(float f) {
        int i = (int) f;

        return f < (float) i ? i - 1 : i;
    }

    public static int floor_double(double d0) {
        int i = (int) d0;

        return d0 < (double) i ? i - 1 : i;
    }

    public static float e(float f) {
        return f >= 0.0F ? f : -f;
    }

    public static double a(double d0, double d1) {
        if (d0 < 0.0D) {
            d0 = -d0;
        }

        if (d1 < 0.0D) {
            d1 = -d1;
        }

        return d0 > d1 ? d0 : d1;
    }

    static {
        for (int i = 0; i < 65536; ++i) {
            a[i] = (float) Math.sin((double) i * 3.141592653589793D * 2.0D / 65536.0D);
        }
    }
}
