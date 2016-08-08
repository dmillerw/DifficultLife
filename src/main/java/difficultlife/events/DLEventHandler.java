package difficultLife.events;

import difficultLife.init.DLConfigSetup;
import difficultLife.init.DLItems;
import difficultLife.network.DataSyncManager;
import difficultLife.utils.DLPotionHelper;
import difficultLife.utils.DLSaveStorage;
import difficultLife.utils.DLUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DLEventHandler {

    public static List<DLPotionEntry> potions = new ArrayList<DLPotionEntry>();

    public static void initPotions() {
        potions.add(new DLPotionEntry(PotionTypes.STRENGTH, 30));
        potions.add(new DLPotionEntry(PotionTypes.SWIFTNESS, 10));
        potions.add(new DLPotionEntry(PotionTypes.FIRE_RESISTANCE, 10));
        potions.add(new DLPotionEntry(PotionTypes.INVISIBILITY, 20));
//        potions.add(new DLPotionEntry(Potion.resistance.id, 30));
    }

    public static DLPotionEntry findRandPotion(Random rnd, float maxWeight) {
        List<DLPotionEntry> lst = new ArrayList<DLPotionEntry>();

        for (int i = 0; i < potions.size(); ++i) {
            DLPotionEntry e = potions.get(i);
            if (e.weight <= maxWeight) {
                lst.add(e);
            }
        }

        if (!lst.isEmpty())
            return lst.get(rnd.nextInt(lst.size()));

        return null;
    }

    @SubscribeEvent
    public void livingDropsEvent(LivingDropsEvent event) {
        if (!event.getEntityLiving().worldObj.isRemote) {
            if (event.isRecentlyHit()) {
                if (event.getEntityLiving().worldObj.rand.nextFloat() <= DLConfigSetup.HEART_DROP_CHANCE) {
                    event.getEntityLiving().dropItem(DLItems.heart, 1);
                }

            }

            if (event.getEntityLiving() instanceof EntityWither) {
                if (DLConfigSetup.GAIN_HEARTS_FROM_WITHER)
                    event.getEntityLiving().dropItem(DLItems.heart, 3 + event.getEntityLiving().worldObj.rand.nextInt(3));
            }

            if (event.getEntityLiving() instanceof EntityDragon) {
                if (DLConfigSetup.GAIN_HEARTS_FROM_DRAGON)
                    event.getEntityLiving().dropItem(DLItems.heart, 9 + event.getEntityLiving().worldObj.rand.nextInt(12));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void livingSpawnEvent(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntityLiving().worldObj.isRemote && !(event.getEntityLiving() instanceof EntityPlayer)) {
            if (EntityList.getEntityString(event.getEntityLiving()) != null && !EntityList.getEntityString(event.getEntityLiving()).isEmpty() && !DLConfigSetup.PERMITTED_FROM_HP_INCREASEMENT.contains(EntityList.getEntityString(event.getEntityLiving())))
                if (event.getEntityLiving().getAttributeMap() != null) {
                    if (event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifier(DLUtils.modifierID) == null) {
                        float difficulty = DLSaveStorage.commonGenericTag.getFloat("difficulty");

                        if (!DLConfigSetup.PERMITTED_FROM_BLIGHT.contains(EntityList.getEntityString(event.getEntityLiving())))
                            if (event.getEntityLiving().worldObj.rand.nextFloat() < DLSaveStorage.commonGenericTag.getFloat("difficulty") / DLConfigSetup.DIFFICULTY_MAX * DLConfigSetup.BLIGHT_CHANCE_MULTIPLIER) {
                                event.getEntityLiving().addPotionEffect(DLPotionHelper.potionEffect(PotionTypes.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));
                                event.getEntityLiving().addPotionEffect(DLPotionHelper.potionEffect(PotionTypes.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
                                event.getEntityLiving().addPotionEffect(DLPotionHelper.potionEffect(PotionTypes.SWIFTNESS, Integer.MAX_VALUE, 0, true, false));
                                event.getEntityLiving().addPotionEffect(DLPotionHelper.potionEffect(PotionTypes.STRENGTH, Integer.MAX_VALUE, 0, true, false));

                                final World world = event.getEntity().worldObj;
                                final Random random = world.rand;

                                if (event.getEntityLiving() instanceof EntityLiving) {
                                    final EntityLiving entity = (EntityLiving) event.getEntityLiving();

                                    int i = random.nextInt(2);

                                    for (int k = 0; k < 3; k++) {
                                        if (random.nextFloat() < 0.095F) {
                                            ++i;
                                        }
                                    }

                                    for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                                        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND)
                                            continue;

                                        if (random.nextFloat() < 0.5F)
                                            break;

                                        final ItemStack itemStack = entity.getItemStackFromSlot(slot);
                                        if (itemStack == null) {
                                            entity.setItemStackToSlot(slot, new ItemStack(EntityLiving.getArmorByChance(slot, i)));
                                        }
                                    }
                                }

                                for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                                    if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND)
                                        continue;

                                    ItemStack itemStack = event.getEntityLiving().getItemStackFromSlot(slot);
                                    if (itemStack != null) {
                                        event.getEntityLiving().setItemStackToSlot(slot, EnchantmentHelper.addRandomEnchantment(random, itemStack, 30, false));
                                    }
                                }

                                event.getEntityLiving().setFire(Integer.MAX_VALUE / 20);
                                if (event.getEntityLiving() instanceof EntityCreeper) {
                                    event.getEntityLiving().onStruckByLightning(new EntityLightningBolt(event.getEntityLiving().worldObj, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, false));
                                }
                                difficulty *= 3;
                            }

                        float genAddedHealth = difficulty;


                        if (event.getEntityLiving() instanceof IMob) {
                            genAddedHealth *= DLConfigSetup.DIFFICULTY_GENERIC_HEALTH_MULTIPLIER;
                        } else {
                            genAddedHealth *= DLConfigSetup.DIFFICULTY_PEACEFULL_HEALTH_MULTIPLIER;
                        }

                        difficulty -= genAddedHealth;

                        if (difficulty > 0) {
                            float randomFlt = event.getEntityLiving().worldObj.rand.nextFloat();
                            float diffIncrease = difficulty * randomFlt;
                            difficulty -= diffIncrease;
                            genAddedHealth += diffIncrease;
                        }

                        if (event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
                            if (difficulty > 0) {
                                float randomFlt = event.getEntityLiving().worldObj.rand.nextFloat();
                                float diffIncrease = difficulty * randomFlt;
                                difficulty -= diffIncrease;
                                if (event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(DLUtils.modifierID) == null)
                                    event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.damageMod", diffIncrease / 10, 0));
                            }

                        if (difficulty > 0) {
                            DLPotionEntry e = findRandPotion(event.getEntityLiving().worldObj.rand, difficulty);
                            if (e != null) {
                                difficulty -= e.weight;
                            }
                        }
                        if (event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifier(DLUtils.modifierID) == null)
                            event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.healthMod", genAddedHealth, 0));
                        event.getEntityLiving().setHealth(event.getEntityLiving().getMaxHealth());
                    }
                }
        }
        if (!event.getEntityLiving().worldObj.isRemote && event.getEntityLiving() instanceof EntityPlayer && DLConfigSetup.ENABLE_CUSTOM_HEALTH_REGEN) {
            if (event.getEntityLiving().worldObj.getWorldTime() % 240 == 0 && ((EntityPlayer) event.getEntityLiving()).getFoodStats().getFoodLevel() >= 10) {
                event.getEntityLiving().heal(1);
            }
        }
        if (!event.getEntityLiving().worldObj.isRemote && event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().ticksExisted % 20 == 0) {
            EntityPlayer p = (EntityPlayer) event.getEntityLiving();
            List<EntityPlayer> players = p.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(p.posX - 0.5D, p.posY - 0.5D, p.posZ - 0.5D, p.posX + 0.5D, p.posY + 0.5D, p.posZ + 0.5D).expand(16, 8, 16));
            for (EntityPlayer pl : players) {
                NBTTagCompound tag = DLSaveStorage.playerData.get(p.getDisplayNameString());
                if (tag.hasKey("username")) {
                    //...
                } else {
                    tag.setString("username", p.getDisplayNameString());
                }
                DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) pl, tag, true);
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == Side.SERVER && event.world != null && event.world.provider != null && event.world.provider.getDimension() == 0 && event.phase == TickEvent.Phase.START) {
            float dIncrease = 0;
            if (event.world.getWorldTime() % 20 == 0) {
                dIncrease = DLConfigSetup.DIFFICULTY_EACH_TICK;
            }
            DLSaveStorage.manageDifficultyIncrease(dIncrease);
        }
        if (DLConfigSetup.ENABLE_CUSTOM_HEALTH_REGEN) {
            event.world.getGameRules().setOrCreateGameRule("naturalRegeneration", "false");
        }
    }

    @SubscribeEvent
    public void onPlayerConstruction(PlayerEvent.PlayerRespawnEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            float maxHealth = player.getMaxHealth();
            if (DLConfigSetup.LOOSE_HEALTH_ON_DEATH) {
                NBTTagCompound tag = DLSaveStorage.playerData.get(player.getDisplayNameString());
                if (tag != null && !tag.hasNoTags()) {
                    tag.setInteger("health", DLConfigSetup.PLAYER_HEARTS_GENERIC * 2);
                    DLSaveStorage.playerData.put(player.getDisplayNameString(), tag);
                    DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) player, DLSaveStorage.playerData.get(player.getDisplayNameString()), true);
                }
            }
            float shouldHave = DLSaveStorage.getSuggestedAmmoundOfHealthForPlayer(player);
            float difference = maxHealth - shouldHave;

            AttributeModifier mod = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifier(DLUtils.modifierID);
            if (mod == null) {
                player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.playerhealthDifference", -difference, 0));
            }
            maxHealth = player.getMaxHealth();
            if (player.getHealth() > maxHealth) {
                player.setHealth(maxHealth);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerReadedFromFile(net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile event) {
        DLSaveStorage.generatePlayerSaveFile(event);
    }

    @SubscribeEvent
    public void onPlayerSavedToFile(net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile event) {
        DLSaveStorage.saveServerPlayerFile(event);
    }

    @SubscribeEvent
    public void onPlayerJoinedServerEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            DataSyncManager.requestServerToClientMessage("worldData", (EntityPlayerMP) event.player, DLSaveStorage.commonGenericTag, true);
            DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) event.player, DLSaveStorage.playerData.get(event.player.getDisplayNameString()), true);
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            float maxHealth = player.getMaxHealth();
            float shouldHave = DLSaveStorage.getSuggestedAmmoundOfHealthForPlayer(player);
            float difference = maxHealth - shouldHave;

            AttributeModifier mod = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifier(DLUtils.modifierID);
            if (mod == null) {
                player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.playerhealthDifference", -difference, 0));
            }
            maxHealth = player.getMaxHealth();
            if (player.getHealth() > maxHealth) {
                player.setHealth(maxHealth);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoadEvent(WorldEvent.Load event) {
        DLSaveStorage.generateServerWorldFile(event);
    }

    @SubscribeEvent
    public void onWorldSaveEvent(WorldEvent.Save event) {
        DLSaveStorage.saveServerWorldFile(event);
    }

    public static class DLPotionEntry {
        PotionType potion;
        float weight;

        public DLPotionEntry(PotionType potion, float weight) {
            this.potion = potion;
            this.weight = weight;
        }
    }

}
