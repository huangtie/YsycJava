package com.mobile.service.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {
	
	/**
	 * 生成原始和缩略图名称
	 * @param fileName 原始图片名称
	 * @return 原始和缩略图名称
	 * @throws Exception
	 */
	public static String[] getName(String fileName) throws Exception {
		String[] result = new String[2];
		Random ran = new Random();
		int number = ran.nextInt(1024);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateTime = sdf.format(new Date());
		result[0] = dateTime + number + getSuffix(fileName);
		result[1] = dateTime + number + "-thumbnail" + getSuffix(fileName);
		return result;
	}
	
	/**
	 * 取后缀
	 * @param fileName 原始图片名称
	 * @return 后缀
	 */
	private static String getSuffix(String fileName){
		String suffix = "";
		if(fileName != null){
			if(fileName.lastIndexOf(".") != -1){
				int index = fileName.lastIndexOf(".");
				suffix = fileName.substring(index);
				
			}
			if(fileName.lastIndexOf(".jpg") != -1){
				suffix = ".jpg";
			}
			if(fileName.lastIndexOf(".JPG") != -1){
				suffix = ".jpg";
			}
			if(fileName.lastIndexOf(".gif") != -1){
				suffix = ".gif";
			}
			if(fileName.lastIndexOf(".GIF") != -1){
				suffix = ".GIF";
			}
			if(fileName.lastIndexOf(".png") != -1){
				suffix = ".png";
			}
			if(fileName.lastIndexOf(".PNG") != -1){
				suffix = ".PNG";
			}
			if(fileName.lastIndexOf(".jpeg") != -1){
				suffix = ".jpeg";
			}
		}
		return suffix;
	}
	
	public static int imageScale(String fromFileName, String toFileName, Integer width){
		try {
			Image image = javax.imageio.ImageIO.read(new File(fromFileName));
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			float scale = getRatio(imageWidth, imageHeight, width);
			imageWidth = (int) (scale * imageWidth);
			imageHeight = (int) (scale * imageHeight);

			image = image.getScaledInstance(imageWidth, imageHeight,
					Image.SCALE_AREA_AVERAGING);

			BufferedImage mBufferedImage = new BufferedImage(imageWidth,
					imageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = mBufferedImage.createGraphics();

			g2.drawImage(image, 0, 0, imageWidth, imageHeight, Color.white,
					null);
			g2.dispose();

			float[] kernelData2 = { -0.125f, -0.125f, -0.125f, -0.125f, 2,
					-0.125f, -0.125f, -0.125f, -0.125f };
			Kernel kernel = new Kernel(3, 3, kernelData2);
			ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			mBufferedImage = cOp.filter(mBufferedImage, null);
			FileOutputStream out = new FileOutputStream(toFileName);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(mBufferedImage);
			out.close();
			return imageHeight;
		} catch (FileNotFoundException fnf) {
		} catch (IOException ioe) {
		} finally {

		}
		return 0;
	}
	
	 public static void imageScale(String fromFileName, String toFileName, Integer width, Integer height){
		try {
			Image image = javax.imageio.ImageIO.read(new File(fromFileName));
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
//			float scale = getRatio(imageWidth, imageHeight, 300, 240);
			imageWidth = width;//(int) (scale * imageWidth);
			imageHeight = height;//(int) (scale * imageHeight);

			image = image.getScaledInstance(imageWidth, imageHeight,
					Image.SCALE_AREA_AVERAGING);

			BufferedImage mBufferedImage = new BufferedImage(imageWidth,
					imageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = mBufferedImage.createGraphics();

			g2.drawImage(image, 0, 0, imageWidth, imageHeight, Color.white,
					null);
			g2.dispose();

			float[] kernelData2 = { -0.125f, -0.125f, -0.125f, -0.125f, 2,
					-0.125f, -0.125f, -0.125f, -0.125f };
			Kernel kernel = new Kernel(3, 3, kernelData2);
			ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			mBufferedImage = cOp.filter(mBufferedImage, null);
			FileOutputStream out = new FileOutputStream(toFileName);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(mBufferedImage);
			out.close();
		} catch (FileNotFoundException fnf) {
		} catch (IOException ioe) {
		} finally {

		}
	}
	
	 public static float getRatio(int width, int height, int maxWidth) {
		float ratio = 1.0f;
		float widthRatio;
		widthRatio = (float) maxWidth / width;
		if (widthRatio < 1.0) {
			ratio = widthRatio;
		}
		return ratio;
	}
	 
	public static float getRatio(int width, int height, int maxWidth,
			int maxHeight) {
		float ratio = 1.0f;
		float widthRatio;
		float heightRatio;
		widthRatio = (float) maxWidth / width;
		heightRatio = (float) maxHeight / height;
		if (widthRatio < 1.0 || heightRatio < 1.0) {
			ratio = widthRatio <= heightRatio ? widthRatio : heightRatio;
		}
		return ratio;
	}
}
