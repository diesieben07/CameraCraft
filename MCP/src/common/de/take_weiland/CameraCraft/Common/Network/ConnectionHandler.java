package de.take_weiland.CameraCraft.Common.Network;

import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.Packet1Login;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.ConfigurationManager;
import de.take_weiland.CameraCraft.Common.Recipes.CameraCraftRecipes;

public class ConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
		ByteArrayDataOutput data = PacketHelper.buildPacket(NetAction.CONFIRM_CAMERACRAFT);
		data.writeBoolean(ConfigurationManager.allowCraftTeleporter);
		PacketDispatcher.sendPacketToPlayer(PacketHelper.finishPacket(data), player);
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
		CameraCraftRecipes.disableRecipesAndItems();
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
		CameraCraftRecipes.disableRecipesAndItems();
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
		
	}
}