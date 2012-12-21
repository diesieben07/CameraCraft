package de.take_weiland.CameraCraft.Common.Network;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHelper {
	
	private static HashMap<String,byte[]> partialFiles = new HashMap<String,byte[]>();
	private static HashMap<String,Integer> numBytesReceived = new HashMap<String,Integer>(); 
	private static HashMap<String,IFileReceiveCallback> callbacks = new HashMap<String,IFileReceiveCallback>();
	
	public static ByteArrayDataOutput buildPacket(NetAction action) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeByte(action.ordinal());
		return output;
	}
	
	public static Packet250CustomPayload finishPacket(ByteArrayDataOutput data) {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "CameraCraft";
		packet.data = data.toByteArray();
		packet.length = packet.data.length;
		return packet;
	}
	
	public static Packet250CustomPayload buildAndFinishPacket(NetAction action) {
		return finishPacket(buildPacket(action));
	}

	public static NetAction readAction(ByteArrayDataInput data) {
		int offset = data.readByte();
		if (offset < NetAction.values().length) {
			return NetAction.values()[offset];
		} else {
			return NetAction.UNKNOWN;
		}
	}

	public static ByteArrayDataInput openPacket(Packet250CustomPayload packet) {
		return ByteStreams.newDataInput(packet.data);
	}
	
	public static void readFile(File file, byte[] bytes) throws Exception {
		FileOutputStream outputstream = null;
		Exception exception = null;
		try {
			outputstream = new FileOutputStream(file);
			for (int i = 0; i < bytes.length; i++) {
				outputstream.write(bytes[i]);
			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (outputstream != null) {
				outputstream.close();
			}
		}
	}
	
	public static BufferedImage readFileToImage(byte[] bytes) {
		try {
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Packet250CustomPayload[] writeFile(String fileID, File file) throws Exception {
		FileInputStream inputstream = null;
		Exception exception = null;
		Packet250CustomPayload[] packets = null;
		try {
			int fileLength = (int)file.length();
			
			byte[] bytes = new byte[fileLength];
			inputstream = new FileInputStream(file);
			inputstream.read(bytes);
			
			packets = writeByteArray(fileID, bytes);			
		} catch (Exception e) {
			exception = e;
		} finally {
			inputstream.close();
		}
		if (exception != null) {
			throw exception;
		}
		return packets;
	}

	public static Packet250CustomPayload[] writeFileFromImage(String fileID, BufferedImage image) throws Exception {
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG", outputstream);
		byte[] bytes = outputstream.toByteArray();
		return writeByteArray(fileID, bytes);
	}
	
	private static Packet250CustomPayload[] writeByteArray(String fileID, byte[] array) {
		Packet250CustomPayload[] packets = new Packet250CustomPayload[(int)Math.ceil(array.length / 30000F)];
		int packetIndex = 0;
		int bytesWritten = 0;
		int numBytesToWriteNext;
		
		while (bytesWritten < array.length) {
			if (30000 > array.length - bytesWritten) {
				numBytesToWriteNext = array.length - bytesWritten;
			} else {
				numBytesToWriteNext = 30000;
			}
			
			ByteArrayDataOutput data = buildPacket(NetAction.FILEPART);
			data.writeUTF(fileID);
			data.writeInt(array.length);
			data.writeInt(numBytesToWriteNext);
			data.writeInt(bytesWritten);
			data.write(Arrays.copyOfRange(array, bytesWritten, numBytesToWriteNext + bytesWritten));
			packets[packetIndex] = finishPacket(data);
			packetIndex++;
			bytesWritten += numBytesToWriteNext;
		}
		return packets;
	}
	
	public static void partialFileReceived(String fileID, int totalSize, int partBegin, byte[] bytes) {
		if (!partialFiles.containsKey(fileID)) {
			partialFiles.put(fileID, new byte[totalSize]);
			numBytesReceived.put(fileID, 0);
		}
		byte[] previousReceived = partialFiles.get(fileID);
		System.arraycopy(bytes, 0, previousReceived, partBegin, bytes.length);
		numBytesReceived.put(fileID, numBytesReceived.get(fileID) + bytes.length);
		checkForFileComplete(fileID);
	}
	
	public static void registerFileReceiveCallback(String fileID, IFileReceiveCallback callback) {
		callbacks.put(fileID, callback);
		checkForFileComplete(fileID);
	}
	
	private static void checkForFileComplete(String fileID) {
		if (!partialFiles.containsKey(fileID)) {
			return;
		}
		byte[] bytes = partialFiles.get(fileID);
		
		if (bytes.length == numBytesReceived.get(fileID).intValue()) {
			if (callbacks.containsKey(fileID)) {
				callbacks.get(fileID).fileReceived(fileID, bytes);
				callbacks.remove(fileID);
				partialFiles.remove(fileID);
				numBytesReceived.remove(fileID);
			}
		}
	}
	
	public static void sendPacketsToServer(Packet250CustomPayload[] packets) {
		for (Packet250CustomPayload packet : packets) {
			PacketDispatcher.sendPacketToServer(packet);
		}
	}
	
	public static void sendPacketsToPlayer(Packet250CustomPayload[] packets, Player player) {
		for (Packet250CustomPayload packet : packets) {
			PacketDispatcher.sendPacketToPlayer(packet, player);
		}
	}

	public static void writeItemStack(ItemStack stack, ByteArrayDataOutput data) throws IOException {
		if (stack == null) {
			data.writeShort(-1);
		} else {
			data.writeShort(stack.itemID);
			data.writeByte(stack.stackSize);
			data.writeShort(stack.getItemDamage());
			NBTTagCompound nbt = null;
			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				nbt = stack.stackTagCompound;
			}
			writeNBT(nbt, data);
		}
	}
	
	public static ItemStack readItemStack(ByteArrayDataInput data) throws IOException {
		ItemStack stack = null;
		short id = data.readShort();
		if (id > 0) {
			byte stackSize = data.readByte();
			short damage = data.readShort();
			stack = new ItemStack(id, stackSize, damage);
			stack.stackTagCompound = readNBT(data);
		}
		return stack;
	}

	public static NBTTagCompound readNBT(ByteArrayDataInput data) throws IOException {
		short length = data.readShort();
		if (length < 0) {
			return null;
		} else {
			byte[] bytes = new byte[length];
			data.readFully(bytes);
			return CompressedStreamTools.decompress(bytes);
		}
	}

	public static void writeNBT(NBTTagCompound nbt, ByteArrayDataOutput data) throws IOException {
		if (nbt == null) {
			data.writeShort(-1);
		} else {
			byte[] bytes = CompressedStreamTools.compress(nbt);
			data.writeShort(bytes.length);
			data.write(bytes);
		}
	}
}