package cn.gsein.blpconverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author G. Seinfeld
 * @date 2019/08/26
 */
public class Test {
    public static void main(String[] args) throws IOException {
        File blpFile = new File("src\\main\\resources\\BTNWudu.blp");
        BufferedImage img = ImageIO.read(blpFile);
        System.out.println(img);
    }
}
