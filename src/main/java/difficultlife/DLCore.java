package difficultLife;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import difficultLife.events.DLEventHandler;
import difficultLife.init.DLConfigSetup;
import difficultLife.init.DLItems;
import difficultLife.network.DLPacket;
import difficultLife.network.DLPacketHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
		modid = DLCore.modid,
		name = DLCore.modname,
		version = DLCore.version
	)
public class DLCore {
	
	public static final String modid = "difficultlife";
	public static final String modname = "Difficult Life";
	public static final String version = "1.1.1710.11";
	
	public static SimpleNetworkWrapper networkManager;
	
	public static Configuration modCFG;
	
	@SidedProxy(serverSide="difficultLife.DLServer",clientSide="difficultLife.client.DLClient")
	public static DLServer proxy;
	
	public static DLCore instance;
	
	@Mod.EventHandler
	public void modPreInit(FMLPreInitializationEvent event)
	{
		instance=this;
		FMLLog.info("[DifficultLife]Starting pre-initialization phase...", nObj());
		FMLLog.info("[DifficultLife]	*Generating configuration file...", nObj());
			modCFG = new Configuration(event.getSuggestedConfigurationFile());
			DLConfigSetup.setupCFG(modCFG);
		FMLLog.info("[DifficultLife]	*Finished generating configuration file!", nObj());
		
		networkManager = NetworkRegistry.INSTANCE.newSimpleChannel("DifficultLife");
		networkManager.registerMessage(DLPacketHandler.class, DLPacket.class, 0, Side.SERVER);
		networkManager.registerMessage(DLPacketHandler.class, DLPacket.class, 0, Side.CLIENT);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		
		FMLCommonHandler.instance().bus().register(new DLEventHandler());
		MinecraftForge.EVENT_BUS.register(new DLEventHandler());
		
		DLItems.init();
		DLEventHandler.initPotions();
		proxy.clientInfo();
	}
	
	
	
	public static Object[] nObj(Object... objects)
	{
		if(objects != null && objects.length > 0)
		{
			return objects;
		}
		return new Object[]{};
	}

}
