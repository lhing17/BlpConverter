package cn.gsein.blpconverter.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author G. Seinfeld
 * @date 2019/08/26
 */
public final class ImageUtil {

    private static int[] passiveBorderPart = {
            0, 0, 0, 0, 31, 68, 112, 157,
            195, 221, 238, 248, 253, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255
    };

    private ImageUtil() {

    }

    public static BufferedImage copy(BufferedImage bImage) {
        int w = bImage.getWidth(null);
        int h = bImage.getHeight(null);
        BufferedImage bImage2 = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bImage2.createGraphics();
        g2.drawImage(bImage, 0, 0, null);
        return bImage2;
    }

    public static BufferedImage resizeTo64(BufferedImage origin) {
        BufferedImage tag = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(origin.getScaledInstance(64, 64, Image.SCALE_SMOOTH), 0, 0, null);
        return tag;
    }

    public static BufferedImage addActiveBorder(BufferedImage origin) throws IOException {
        BufferedImage result = copy(origin);
        BufferedImage border = ImageIO.read(Objects.requireNonNull(
                ImageUtil.class.getClassLoader().getResourceAsStream("active_border.png")));
        Graphics graphics = result.getGraphics();
        graphics.drawImage(border, 0, 0, null);
        return result;
    }

    public static int mixPixels(int pixel0, double portion0, int pixel1, double portion1) {
        int r0 = (pixel0 >> 16) & 0xFF;
        int r1 = (pixel1 >> 16) & 0xFF;
        int g0 = (pixel0 >> 8) & 0xFF;
        int g1 = (pixel1 >> 8) & 0xFF;
        int b0 = pixel0 & 0xFF;
        int b1 = pixel1 & 0xFF;
        int r = (int) (r0 * portion0 + r1 * portion1);
        int g = (int) (g0 * portion0 + g1 * portion1);
        int b = (int) (b0 * portion0 + b1 * portion1);
        int pixel = (pixel0 >> 24 << 24) | (r << 16) | (g << 8) | b;
        return pixel;
    }

    public static BufferedImage adjustBrightness(BufferedImage origin, int brightness) {
        BufferedImage result = copy(origin);
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                int pixel = result.getRGB(j, i);
                Color color = new Color(pixel);
                int red = color.getRed() + brightness;
                if (red > 255) red = 255;
                if (red < 0) red = 0;
                int green = color.getGreen() + brightness;
                if (green > 255) green = 255;
                if (green < 0) green = 0;
                int blue = color.getBlue() + brightness;
                if (blue > 255) blue = 255;
                if (blue < 0) blue = 0;
                color = new Color(red, green, blue);
                int x = color.getRGB();
                result.setRGB(j, i, x);
            }
        }
        return result;
    }

    public static BufferedImage addPassiveBorder(BufferedImage origin) throws IOException {
        BufferedImage result = copy(origin);
        InputStream is = ImageUtil.class.getClassLoader().getResourceAsStream("passive_border.png");
        assert is != null;
        BufferedImage border = ImageIO.read(is);
        int start = 0, end = 63;
        while (start < end) {
            for (int i = start; i < end + 1; i++) {
                mixPixelsForBorder(result, border, start, i, passiveBorderPart[start]);
                mixPixelsForBorder(result, border, end, i, passiveBorderPart[start]);
                mixPixelsForBorder(result, border, i, start, passiveBorderPart[start]);
                mixPixelsForBorder(result, border, i, end, passiveBorderPart[start]);
            }
            start++;
            end--;
        }
        return result;
    }

    private static void mixPixelsForBorder(BufferedImage result, BufferedImage border, int x, int y, int portion) {
        result.setRGB(x, y, mixPixels(result.getRGB(x, y), 1.0 * portion / 255,
                border.getRGB(x, y), 1 - 1.0 * portion / 255));
    }

}
