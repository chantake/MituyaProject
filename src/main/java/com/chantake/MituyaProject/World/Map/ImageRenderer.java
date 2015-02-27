package com.chantake.MituyaProject.World.Map;

/**
 * Created by fumitti on 2015/02/05.
 */

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageRenderer extends MapRenderer {

    private BufferedImage cacheImage;
    private boolean hasRendered = false;

    public ImageRenderer(URL url) throws IOException {
        this.cacheImage = this.getImage(url);
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        if (cacheImage != null && !hasRendered) {
            canvas.drawImage(0, 0, cacheImage);
            hasRendered = true;
        }
    }

    public BufferedImage getImage(URL url) throws IOException {
        BufferedImage image = ImageIO.read(url);
        RenderUtils.resizeImage(image);
        return image;
    }

}

