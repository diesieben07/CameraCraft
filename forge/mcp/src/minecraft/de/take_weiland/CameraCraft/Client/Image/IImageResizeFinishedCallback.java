package de.take_weiland.CameraCraft.Client.Image;

import java.awt.image.BufferedImage;

public interface IImageResizeFinishedCallback {
	public void resizeFinished(BufferedImage resized);
	public void resizeFailed(Exception e);
}
