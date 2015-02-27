package com.chantake.MituyaProject.World.Map;

import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by fumitti on 2015/02/05.
 */
public class RenderUtils {

    public static void resizeImage(BufferedImage image) {
        Graphics2D resizer = image.createGraphics();
        // ??? Article said that it "increased image quality". Read: rip heap
        resizer.setComposite(AlphaComposite.Src);
        resizer.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        resizer.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        resizer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        resizer.drawImage(image, 0, 0, 128, 128, null);
        resizer.dispose();
    }

    public static void resizeImageNoEditing(BufferedImage image) {
        Graphics2D resizer = image.createGraphics();
        resizer.drawImage(image, 0, 0, 128, 128, null);
        resizer.dispose();
    }

    public static void removeRenderers(MapView view) {
        if (view == null) {
            return;
        }

        for (MapRenderer mr : view.getRenderers()) {
            view.removeRenderer(mr);

            if (mr instanceof StoppableRenderer) {
                ((StoppableRenderer) mr).stopRendering();
            }
        }
    }

}