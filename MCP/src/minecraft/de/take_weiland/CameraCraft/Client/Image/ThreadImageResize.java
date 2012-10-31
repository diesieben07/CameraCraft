package de.take_weiland.CameraCraft.Client.Image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;


public class ThreadImageResize extends Thread {
    	private BufferedImage original;
    	private int targetWidth;
    	private int targetHeight;
    	private IImageResizeFinishedCallback callback;
    	
    	private static final Random random = new Random();
    	
    	
		public ThreadImageResize(BufferedImage original, int targetWidth, int targetHeight, IImageResizeFinishedCallback callback) {
			this.original = original;
			this.targetWidth = targetWidth;
			this.targetHeight = targetHeight;
			this.callback = callback;
		}


		public void run() {
			try {
				float targetAspectRatio = (float)targetWidth / (float)targetHeight;
				float originalAspectRatio = (float)original.getWidth() / (float)original.getHeight();
				
				float resizeFactor;
				
				if (originalAspectRatio < targetAspectRatio) {
					resizeFactor = (float)targetWidth / (float)original.getWidth();
				} else {
					resizeFactor = (float)targetHeight / (float)original.getHeight();
				}
				
				int resizedWidth = (int)Math.ceil((float)original.getWidth() * resizeFactor);
				int resizedHeight = (int)Math.ceil((float)original.getHeight() * resizeFactor);
				
				BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight,  BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = resizedImage.createGraphics();
				g2d.drawImage(original, 0, 0, resizedWidth, resizedHeight, null);
				g2d.dispose();			
				
				int offsetX = 0;
				int offsetY = 0;
				if (resizedWidth > targetWidth) {
					offsetX = (int) ((float)(resizedWidth - targetWidth) / 2F);
				}
				
				if (resizedHeight > targetHeight) {
					offsetY = (int) ((float)(resizedHeight - targetHeight) / 2F);
				}
				
				/*System.out.println("resizing: from [" + original.getWidth() + "x" + original.getHeight() + "] to [" + targetWidth + "x" + targetHeight + "]");
				System.out.println("ratios: original: "+ originalAspectRatio + " / target: " + targetAspectRatio);
				System.out.println("resizeFactor: " + resizeFactor);
				System.out.println("resizedImage: [" + resizedImage.getWidth() + "x" + resizedImage.getHeight() + "]");
				System.out.println("offsetX: " + offsetX + " / offsetY: " + offsetY);*/
				
				callback.resizeFinished(resizedImage.getSubimage(offsetX, offsetY, targetWidth, targetHeight));
			} catch (Exception e) {
				callback.resizeFailed(e);
			}
    	}
    }