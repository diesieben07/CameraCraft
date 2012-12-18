package de.take_weiland.CameraCraft.Common;

import net.minecraft.src.EntityPlayer;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;

import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;

/**
 * An interface to be implemented by all things that provide access to photos (e.g. the Digital Camera)
 * will be used by the GuiViewPhotos
 * @author diesieben07
 *
 */
public interface IPhotoSource {
	
	/**
	 * perform a name change of a photo
	 * @param photoIndex the Index of the photo whose name changed
	 * @param newName the new id
	 */	
	public void nameChanged(int photoIndex, String newName);

	/**
	 * check if the player can view the photos of this source
	 * @param player the player to check
	 * @return if the player can interact with this photo source
	 */	
	public boolean canViewPhotos(EntityPlayer player);

	/**
	 * gets the PhotoInformation from this PhotoSource
	 * @param photoId the id of the PhotoInformation to get
	 * @return the PhotoInformation
	 */
	public PhotoInformation getPhotoInformation(String photoId);
	
	/**
	 * gets the PhotoInformation at the specified position in this photoSource
	 * @param index the position of the photo
	 * @return the PhotoInformation
	 */
	public PhotoInformation getPhotoInformation(int index);
	
	/**
	 * gets the number of Photos this PhotoSource has currently stored
	 * @return the number of Photos available
	 */
	public int numPhotos();
	
	/**
	 * deletes the specified photo from this PhotoSource
	 * canDelete is checked prior to this. Only called when canDelete returns true
	 * @param photoIndex the index of the photo to delete
	 */
	public void deletePhoto(int photoIndex);
	
	/**
	 * determines if the user can delete from this photoSource
	 * @return if the user can delete photos from this photoSource
	 */
	public boolean canDelete();
	
	/**
	 * determines if this PhotoSource can print photos
	 * @return if the PhotoSource can print photos
	 */
	public boolean canPrint();
	
	/**
	 * adds a list of PhotoSizeAmountInfos to the print queue of this PhotoSource
	 * canPrint is checked prior to this. Only called when canPrint returns true
	 * @param photos the photos to add to the print queue
	 */
	public void addToPrintQueue(List<PhotoSizeAmountInfo> photos);
	
	/**
	 * gets the {@link GuiScreens GuiScreen} that this IPhotoSource uses to view its photos
	 * @return the GuiScreen
	 */
	public GuiScreens getScreenTypeViewPhotos();
	
	/**
	 * the x-Coordinate that should be used in {@link net.minecraft.src.EntityPlayer#openGui EntityPlayer.openGui}
	 * @return the x-Coordinate
	 */
	public int getX();
	
	/**
	 * the y-Coordinate that should be used in {@link net.minecraft.src.EntityPlayer#openGui EntityPlayer.openGui}
	 * @return the y-Coordinate
	 */
	public int getY();
	
	/**
	 * the z-Coordinate that should be used in {@link net.minecraft.src.EntityPlayer#openGui EntityPlayer.openGui}
	 * @return the z-Coordinate
	 */
	public int getZ();
	
	/**
	 * the given player starts viewing the photos of this source
	 * @param player the player
	 */
	public void startViewing(EntityPlayer player);
	
	/**
	 * the given player stops viewing the photos of this source
	 * @param player the player
	 */
	public void stopViewing(EntityPlayer player);
}
