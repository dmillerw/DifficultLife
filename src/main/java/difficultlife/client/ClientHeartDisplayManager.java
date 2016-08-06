package difficultLife.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class ClientHeartDisplayManager {

    public static final int[] colors = new int[]
            {
                    0xb80000,
                    0xba400f,
                    0xff571d,
                    0xce9e00,
                    0xabc400,
                    0x3c8e00,
                    0x26c529,
                    0x08c762,
                    0x0dd2cf,
                    0x0094c6,
                    0x1363b2,
                    0x133bb2,
                    0x5b7fec,
                    0x6f6fef,
                    0x7155c6,
                    0x8e55c6,
                    0xbb33e8,
                    0xe833e8,
                    0xea4598,
                    0xea4598,
                    0xffffff
            };
    public static final ResourceLocation heartTextures = new ResourceLocation("difficultlife", "textures/health.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    public static final Random rand = new Random();
    public static int updateCounter = 0;
    double zLevel = 0;

    @SubscribeEvent
    public void renderHealthbar(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.HEALTH) {
            updateCounter++;
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution scaledresolution = new ScaledResolution(mc);
            int scaledWidth = scaledresolution.getScaledWidth();
            int scaledHeight = scaledresolution.getScaledHeight();
            int xBasePos = scaledWidth / 2 - 91;
            int yBasePos = scaledHeight - 39;

            boolean highlight = mc.thePlayer.hurtResistantTime / 3 % 2 == 1;

            if (mc.thePlayer.hurtResistantTime < 10) {
                highlight = false;
            }
            IAttributeInstance attrMaxHealth = mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            int health = MathHelper.ceiling_float_int(mc.thePlayer.getHealth());
            int healthLast = health;
            //TODO - dmillerw: heatlh last?
//			int healthLast = MathHelper.ceiling_float_int(mc.thePlayer.prevHealth);
            float healthMax = (float) attrMaxHealth.getAttributeValue();
            if (healthMax > 20)
                healthMax = 20;
            float absorb = mc.thePlayer.getAbsorptionAmount();
            int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);

            rand.setSeed((long) (updateCounter * 312871));
            int left = scaledWidth / 2 - 91;
            int top = scaledHeight - GuiIngameForge.left_height;

            if (!GuiIngameForge.renderExperiance) {
                top += 7;
                yBasePos += 7;
            }
            int regen = -1;
            if (mc.thePlayer.isPotionActive(Potion.REGISTRY.getObject(PotionTypes.REGENERATION.getRegistryName()))) {
                regen = updateCounter % 25;
            }

            float absorbRemaining = absorb;
            final int TOP = 9 * (mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
            final int BACKGROUND = (highlight ? 25 : 16);
            int MARGIN = 16;
            if (mc.thePlayer.isPotionActive(Potion.REGISTRY.getObject(PotionTypes.POISON.getRegistryName())))
                MARGIN += 36;
            else if (mc.thePlayer.isPotionActive(MobEffects.WITHER))
                MARGIN += 72;

            for (int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F) - 1;
                 i >= 0; --i) {
                int b0 = (highlight ? 1 : 0);
                int row = MathHelper.ceiling_float_int((float) (i + 1) / 10.0F) - 1;
                int x = left + i % 10 * 8;
                int y = top - row * rowHeight;

                if (health <= 4)
                    y += rand.nextInt(2);
                if (i == regen)
                    y -= 2;

                drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9, 0xffffff);

                if (highlight) {
                    if (i * 2 + 1 < healthLast)
                        drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9, 0xffffff); // 6
                    else if (i * 2 + 1 == healthLast)
                        drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9, 0xffffff); // 7
                }

                if (absorbRemaining > 0.0F) {
                    if (absorbRemaining == absorb && absorb % 2.0F == 1.0F)
                        drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9, 0xffffff); // 17
                    else
                        drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9, 0xffffff); // 16
                    absorbRemaining -= 2.0F;
                } else {
                    if (i * 2 + 1 < health)
                        drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9, 0xffffff); // 4
                    else if (i * 2 + 1 == health)
                        drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9, 0xffffff); // 5
                }
            }

            int potionOffset = 0;
            PotionEffect potion = mc.thePlayer.getActivePotionEffect(MobEffects.WITHER);
            if (potion != null)
                potionOffset = 18;
            potion = mc.thePlayer.getActivePotionEffect(Potion.REGISTRY.getObject(PotionTypes.POISON.getRegistryName()));
            if (potion != null)
                potionOffset = 9;
            if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                potionOffset += 27;

            mc.getTextureManager().bindTexture(heartTextures);

            int hp = MathHelper.ceiling_float_int(mc.thePlayer.getHealth());
            for (int iter = 0; iter < hp / 20; iter++) {
                int renderHearts = (hp - 20 * (iter + 1)) / 2;
                if (renderHearts > 10)
                    renderHearts = 10;
                for (int i = 0; i < renderHearts; i++) {
                    int y = 0;
                    if (i == regen)
                        y -= 2;
                    this.drawTexturedModalRect(xBasePos + 8 * i, yBasePos + y, 0,
                            potionOffset, 9, 9, getColorBasedOnRow(iter));
                }
                if (hp % 2 == 1 && renderHearts < 10) {
                    this.drawTexturedModalRect(xBasePos + 8 * renderHearts, yBasePos,
                            9, potionOffset, 9, 9, getColorBasedOnRow(iter));
                }
            }

            FontRenderer renderer = mc.fontRendererObj;
            String renderedString = health + "/" + MathHelper.ceiling_double_int(attrMaxHealth.getAttributeValue());
            GL11.glPushMatrix();
            float scale = 0.5F;
            GL11.glScalef(scale, scale, scale);
            renderer.drawStringWithShadow(renderedString, (int) ((xBasePos - renderedString.length() * (6) + 20) / scale), (int) ((yBasePos + 3) / scale), 0xaa0000);
            GL11.glPopMatrix();
            GL11.glColor3f(1, 1, 1);
            mc.getTextureManager().bindTexture(icons);
            GuiIngameForge.left_height += 10;
            if (absorb > 0)
                GuiIngameForge.left_height += 10;

            event.setCanceled(true);
        }
    }

    public int getColorBasedOnRow(int rowNumber) {
        if (rowNumber >= colors.length)
            return colors[colors.length - 1];
        else
            return colors[rowNumber];
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int color) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.putColor4(color);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}
