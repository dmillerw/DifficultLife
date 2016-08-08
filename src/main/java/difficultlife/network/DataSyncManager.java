package difficultLife.network;

import difficultLife.DLCore;
import difficultLife.init.DLConfigSetup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.Hashtable;

public class DataSyncManager {

    public static final Hashtable<String, Integer> packetSyncManager = new Hashtable<String, Integer>();

    public static void requestGuiOpenPacket(EntityPlayer player, int id) {
        if (player.worldObj.isRemote) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("username", player.getDisplayNameString());
            tag.setInteger("guiid", id);
            DLCore.networkManager.sendToServer(new DLPacket().setID("guiRequest").setNBT(tag));
        } else {
            FMLLog.log(Level.DEBUG, "GuiOpen packet can only be requested from CLIENT side, aborting!", DLCore.nObj());
        }
    }

    public static void requestServerToClientMessage(String messageID, EntityPlayerMP client, NBTTagCompound message, boolean requirePacket) {
        if (requirePacket) {
            packetSyncManager.put(messageID + "_" + client.getDisplayNameString(), 0);
            DLCore.networkManager.sendTo(new DLPacket().setID(messageID).setNBT(message), client);
        }
        if (packetSyncManager.containsKey(messageID + "_" + client.getDisplayNameString())) {
            packetSyncManager.put(messageID + "_" + client.getDisplayNameString(), packetSyncManager.get(messageID + "_" + client.getDisplayNameString()) + 1);
            if (packetSyncManager.get(messageID + "_" + client.getDisplayNameString()) >= DLConfigSetup.PACKET_DELAY) {
                packetSyncManager.put(messageID + "_" + client.getDisplayNameString(), 0);
                DLCore.networkManager.sendTo(new DLPacket().setID(messageID).setNBT(message), client);
            }
        } else {
            packetSyncManager.put(messageID, 0);
        }
    }

}
