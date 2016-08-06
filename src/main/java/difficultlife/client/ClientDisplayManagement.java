package difficultLife.client;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import difficultLife.DLCore;
import difficultLife.init.DLConfigSetup;
import difficultLife.utils.DLSaveStorage;

public class ClientDisplayManagement {
	
	public static final ResourceLocation diffManager = new ResourceLocation(DLCore.modid,"textures/diffMeter.png");
	double zLevel = 0D;
	
	@SubscribeEvent
	public void renderTick(RenderGameOverlayEvent.Post event)
	{
		if(Minecraft.getMinecraft().theWorld != null && event.getType() == ElementType.ALL)
		{
			ScaledResolution res = event.getResolution();
	        int width = res.getScaledWidth();
	        int height = res.getScaledHeight();
			GL11.glPushMatrix();
			
				Minecraft.getMinecraft().renderEngine.bindTexture(diffManager);
	        	int left = width-DLConfigSetup.DIFFICULTY_GUI_HORISONTAL;
	        	int top = height/3 - DLConfigSetup.DIFFICULTY_GUI_VERTICAL;
	        	
	        	if(!DLConfigSetup.DIFFICULTY_GUI_RIGHT)
	        		left = DLConfigSetup.DIFFICULTY_GUI_HORISONTAL;
	        	if(!DLConfigSetup.DIFFICULTY_GUI_TOP)
	        		top = DLConfigSetup.DIFFICULTY_GUI_VERTICAL;
	        	
	        	this.drawTexturedModalRect(left, top, 0, 0, 8, 64);
	        	float currentDif = DLSaveStorage.commonGenericTag.getFloat("difficulty");
	        	float percentage = currentDif / DLConfigSetup.DIFFICULTY_MAX;
	        	int textureRect = MathHelper.floor_float(percentage * 64);
	        	if(textureRect > 64)textureRect = 64;
	        	this.drawTexturedModalRect(left, top+(64-textureRect), 8, 64-textureRect, 8, textureRect);
	        	
	        	String str = "dl.hahaha+";
	        	
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[9])
	        		str = "dl.hahaha";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[8])
	        		str = "dl.imcomingforyou";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[7])
	        		str = "dl.iseeyou";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[6])
	        		str = "dl.impossible";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[5])
	        		str = "dl.insane";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[4])
	        		str = "dl.veryHard";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[3])
	        		str = "dl.hard";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[2])
	        		str = "dl.medium";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[1])
	        		str = "dl.easy";
	        	if(currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[0])
	        		str = "dl.veryEasy";
	        	
	        	float s = 0.5F;
	        	GL11.glScalef(s, s, s);
			    //TODO - dmillerw: fix localization
//	        	Minecraft.getMinecraft().fontRendererObj.drawString(StatCollector.translateToLocal(str), (int)((left-str.length()*2.5F + 8)/s), (int)((top+64)/s)+1, 0xffffff);
	        	
			GL11.glPopMatrix();
		}
	}

    // I've found this in like, 3 different places now
    //TODO - dmillerw: UTILITIY CLASS PLZ
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

}
