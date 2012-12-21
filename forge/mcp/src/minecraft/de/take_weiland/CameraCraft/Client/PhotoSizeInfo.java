package de.take_weiland.CameraCraft.Client;

import de.take_weiland.CameraCraft.Common.PhotoInformation;

public class PhotoSizeInfo {
	private byte sizeX = 4;
	private byte sizeY = 4;
	private String photoID;
	
	public byte getSizeX() {
		return sizeX;
	}

	public PhotoSizeInfo setSizeX(int sizeX) {
		this.sizeX = (byte) sizeX;
		return this;
	}

	public byte getSizeY() {
		return sizeY;
	}

	public PhotoSizeInfo setSizeY(int sizeY) {
		this.sizeY = (byte) sizeY;
		return this;
	}

	public String getPhotoID() {
		return photoID;
	}

	public PhotoSizeInfo setPhotoID(String photoID) {
		this.photoID = photoID;
		return this;
	}

	public PhotoSizeInfo(String photoID, int sizeX, int sizeY) {
		setSizeX(sizeX).setSizeY(sizeY).setPhotoID(photoID);
	}
	
	public PhotoSizeInfo() { }
	
	public PhotoSizeInfo(PhotoInformation info) {
		setSizeX(info.getSizeX()).setSizeY(info.getSizeY()).setPhotoID(info.getPhotoId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((photoID == null) ? 0 : photoID.hashCode());
		result = prime * result + sizeX;
		result = prime * result + sizeY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhotoSizeInfo other = (PhotoSizeInfo) obj;
		if (photoID == null) {
			if (other.photoID != null)
				return false;
		} else if (!photoID.equals(other.photoID))
			return false;
		if (sizeX != other.sizeX)
			return false;
		if (sizeY != other.sizeY)
			return false;
		return true;
	}
}
