package difficultLife.client;

import java.lang.reflect.Method;

import difficultLife.network.DataSyncManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientGUIEventManager {
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
    public void guiPostInit(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post event)
    {
        final GuiScreen gui = event.getGui();
        final Minecraft mc = Minecraft.getMinecraft();
        if((gui instanceof GuiInventory) || (gui instanceof GuiVanityArmor))
        {
            int xSize = 176;
            int ySize = 166;
            int guiLeft = (gui.width - xSize) / 2;
            int guiTop = (gui.height - ySize) / 2;
            if(!mc.thePlayer.getActivePotionEffects().isEmpty() && isNeiHidden())
                guiLeft = 160 + (gui.width - xSize - 200) / 2;
            
            if(gui instanceof GuiVanityArmor)
            	guiLeft += 18;
            event.getButtonList().add(new ButtonOpenVanity(58, guiLeft + 26, guiTop + 68, 10, 10, I18n.format((event.getGui() instanceof GuiInventory) ? "button.vanity" : "button.normal", new Object[0])));
        }
    }
    
	@SubscribeEvent
    public void guiPostAction(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if((event.getGui() instanceof GuiInventory) && event.getButton().id == 58)
            DataSyncManager.requestGuiOpenPacket(Minecraft.getMinecraft().thePlayer, 0);
        if((event.getGui() instanceof GuiVanityArmor) && event.getButton().id == 58)
        {
            event.getGui().mc.displayGuiScreen(new GuiInventory(event.getGui().mc.thePlayer));
            DataSyncManager.requestGuiOpenPacket(Minecraft.getMinecraft().thePlayer, 1);
        }
    }
    
    boolean isNeiHidden()
    {
        boolean hidden = true;
        try
        {
            if(isNEIHidden == null)
            {
                Class<?> fake = Class.forName("codechicken.nei.NEIClientConfig");
                isNEIHidden = fake.getMethod("isHidden", new Class[0]);
            }
            hidden = ((Boolean)isNEIHidden.invoke(null, new Object[0])).booleanValue();
        }
        catch(Exception ex) { }
        return hidden;
    }

    static Method isNEIHidden;

}
