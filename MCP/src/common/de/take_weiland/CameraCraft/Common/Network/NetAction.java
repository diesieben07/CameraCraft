package de.take_weiland.CameraCraft.Common.Network;

public enum NetAction {
	UNKNOWN, 
	REQUEST_PHOTO, // the clients informs the server that the user has pressed the "take photo" key on their keyboard
	SERVER_REQUEST_PHOTO, // The server wants the client to take a photo now
	PHOTO_TAKEN, // the client informs the server that the processed photo is on its way
	/**
	 * same as PHOTO_TAKEN but when the player is "inside" a camera
	 */
	PHOTO_TAKEN_NON_PLAYER,
	REQUEST_IMAGEDATA, // the client requests the an image from the server
	IMAGEDATA, // the server informs the client that the requested imagedata is on its way
	/**
	 * the client informs the server that the user has clicked on the "view photos" button in a gui
	 */
	REQUEST_VIEW_PHOTOS,
	/**
	 * the client informs the server that the user has renamed a photo in the "view photos" gui
	 */
	REQUEST_RENAME,
	/**
	 * the client informs the server that the user has chosen to print photos from the "view photos" gui
	 */
	REQUEST_PRINT,
	REQUEST_TELEPORT,
	REQUEST_DELETE,
	/**
	 * the server tells the client to set the viewport to the specified camera entity and take a photo
	 */
	SCREENSHOT_CAMERA_ENTITY,
	/**
	 * a partial file packet
	 */
	FILEPART,
	/**
	 * used to tell the client that the server has CameraCraft
	 */
	CONFIRM_CAMERACRAFT
}
