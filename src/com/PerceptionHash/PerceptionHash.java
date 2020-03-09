package com.PerceptionHash;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Copyright (C), 2020-2020.
 * FileName: PerceptionHash.java
 * 类的详细说明
 *
 * @author BaiBao
 * @Date    2020-3-9
 * @version 1.00

 */
public class PerceptionHash {
    private BufferedImage image(String filename) throws IOException {
        Image srcImg  = ImageIO.read(new FileInputStream(filename) );//取源图
        int  width  =  8;
        int  height =  8;
        return toBufferedImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH));//缩小
    }

    public String Perceptionhash(String filename) throws IOException {
        BufferedImage Image = image(filename);//缩小尺寸
        double RGB = 0;
        int width = Image.getWidth();
        int height = Image.getHeight();
        for (int i = 0;width > i;i++) {
            for(int j = 0;height > j;j++){
                RGB += filterRGB(i,j,Image.getRGB(i,j));
            }
        }
        RGB = RGB / (width * height);//取灰度平均值
        return ComparingAverage(Image,RGB);//比较像素灰度
    }

    /**
     * @param filename1 图片1
     * @param filename2 图片2
     * @return 相似度
     */
    public int Perceptionhash(String filename1,String filename2) throws IOException {
        char[] num1 = Perceptionhash(filename1).toCharArray();//取图片1哈希值
        char[] num2 = Perceptionhash(filename2).toCharArray();//取图片2哈希值
        int Different = 0;
        //汉明距离
        for (int i = 0;i < 64;i++) {
            if(num1[i] != num2[i]) {
                Different++;
            }
        }
        if(Different < 10)
        {
            return 100 - (Different * 10);
        }
        return 0;
    }

    private BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        //boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
	       /* if (hasAlpha) {
	         transparency = Transparency.BITMASK;
	         }*/

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
	        /*if (hasAlpha) {
	         type = BufferedImage.TYPE_INT_ARGB;
	         }*/
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    private int filterRGB(int x, int y, int rgb) {

        int a = rgb & 0xff000000;//将最高位（24-31）的信息（alpha通道）存储到a变量
        int r = (rgb >> 16) & 0xff;//取出次高位（16-23）红色分量的信息
        int g = (rgb >> 8) & 0xff;//取出中位（8-15）绿色分量的信息
        int b = rgb & 0xff;//取出低位（0-7）蓝色分量的信息
        rgb = (r * 77 + g * 151 + b * 28) >> 8;    // NTSC luma，算出灰度值
        return a | (rgb << 16) | (rgb << 8) | rgb;//将灰度值送入各个颜色分量
    }

    private String ComparingAverage(BufferedImage Image, double Average){
        int width = Image.getWidth();
        int height = Image.getHeight();
        String num = "";
        for (int i = 0;width > i;i++) {
            for(int j = 0;height > j;j++){
                if(filterRGB(i,j,Image.getRGB(i,j)) >= Average){
                    num += "1";
                }
                else {
                    num += "0";
                }
            }
        }
        return  num;
    }
}
