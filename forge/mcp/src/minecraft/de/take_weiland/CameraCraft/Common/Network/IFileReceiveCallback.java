package de.take_weiland.CameraCraft.Common.Network;

public interface IFileReceiveCallback {
	public void fileReceived(String fileID, byte[] bytes);
}
