package utilities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageHandler {

    public static BufferedImage loadImage(String fileName) {
        BufferedImage image = null;
        try {
            URL source = ImageHandler.class.getResource(fileName);
            image = ImageIO.read(source);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to LOAD IMAGE: " + fileName);
        }
        return image;
    }
    
    public static Image loadScaleImage(String fileName, int width, int height) {
        Image img = loadImage(fileName);
        Image scaleCut = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return scaleCut;
    }

    public static Image cutScaleImage(BufferedImage originalImage, int col, int row, int sW, int sH, int tW, int tH) {
        BufferedImage subImage = originalImage.getSubimage(col * sW, row * sH, sW, sH);
        Image cut = Toolkit.getDefaultToolkit().createImage(subImage.getSource());
        Image scaleCut = cut.getScaledInstance(tW, tH, Image.SCALE_SMOOTH);
        return scaleCut;
    }

    public static Image cutScaleImageByPixels(BufferedImage originalImage, int x, int y, int width, int height, int sW, int sH, int tW, int tH) {
        BufferedImage subImage = originalImage.getSubimage(x, y, width, height);
        Image cut = Toolkit.getDefaultToolkit().createImage(subImage.getSource());
        double dW = tW / sW;
        double dH = tH / sH;
        Image scaleCut = cut.getScaledInstance((int) (width * dW), (int) (height * dH), Image.SCALE_SMOOTH);
        return scaleCut;
    }

    public static Image cutScaleImage(BufferedImage originalImage, int col, int row, int sW, int sH, int structW, int structH, int tW, int tH) {
        BufferedImage subImage = originalImage.getSubimage(col * sW, row * sH, structW, structH);
        Image cut = Toolkit.getDefaultToolkit().createImage(subImage.getSource());
        Image scaleCut = cut.getScaledInstance(tW, tH, Image.SCALE_SMOOTH);
        return scaleCut;
    }

    public static Image mergeImages(Image[] images) {
        int w = images[0].getWidth(null);
        int h = images[0].getHeight(null);
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        for (int i = 0; i < images.length; i++) {
            g.drawImage(images[i], 0, 0, null);
        }
        return newImage;
    }

}
