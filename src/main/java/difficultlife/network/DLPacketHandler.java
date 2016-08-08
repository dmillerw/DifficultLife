package difficultLife.network;

import difficultLife.DLCore;
import difficultLife.utils.DLSaveStorage;
import difficultLife.utils.DLUtils;
import io.netty.channel.ChannelHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@ChannelHandler.Sharable
public class DLPacketHandler implements IMessageHandler<DLPacket, IMessage> {

    @Override
    public IMessage onMessage(DLPacket message, MessageContext ctx) {
        if (message.id.equalsIgnoreCase("worldData")) {
            DLSaveStorage.commonGenericTag = message.syncedTag;
        }
        if (message.id.equalsIgnoreCase("playerData")) {
            String tagName = message.syncedTag.getString("username");
            String username = DLCore.proxy.getClientPlayer().getDisplayNameString();
            if (username.equalsIgnoreCase(tagName))
                DLSaveStorage.clientPlayerData = message.syncedTag;
            else
                DLSaveStorage.playerData.put(tagName, message.syncedTag);
        }
        if (message.id.equalsIgnoreCase("guiRequest")) {
            DLUtils.handleGuiRequest(message);
        }
        return null; //Always return null, so the Network does not gets overfilled by packets!
    }

}
