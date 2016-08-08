package difficultLife.init;

import difficultLife.DLCore;
import difficultLife.item.GlassArmor;
import difficultLife.item.HeartContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static difficultLife.init.DLConfigSetup.*;

public class DLItems {

    public static ArmorMaterial glass = EnumHelper.addArmorMaterial("DL:GLASS", DLCore.modid + ":textures/items/armor/null.png", 32, new int[]{1, 3, 2, 1}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

    public static Item heart;
    public static Item glassHelm;
    public static Item glassChest;
    public static Item glassLegs;
    public static Item glassBoots;

    public static void init() {
        heart = new HeartContainer().setTextureName(DLCore.modid + (OLD_TEXTURES ? ":heart_old" : ":heart")).setUnlocalizedName("dl.heart").setCreativeTab(CreativeTabs.MISC);
        glassHelm = new GlassArmor(glass, 0, EntityEquipmentSlot.HEAD).setTextureName(DLCore.modid + ":glass_helmet").setUnlocalizedName("dl.glassHelmet").setCreativeTab(CreativeTabs.COMBAT);
        glassChest = new GlassArmor(glass, 0, EntityEquipmentSlot.CHEST).setTextureName(DLCore.modid + ":glass_chestplate").setUnlocalizedName("dl.glassChestplate").setCreativeTab(CreativeTabs.COMBAT);
        glassLegs = new GlassArmor(glass, 0, EntityEquipmentSlot.LEGS).setTextureName(DLCore.modid + ":glass_leggings").setUnlocalizedName("dl.glassLeggings").setCreativeTab(CreativeTabs.COMBAT);
        glassBoots = new GlassArmor(glass, 0, EntityEquipmentSlot.FEET).setTextureName(DLCore.modid + ":glass_boots").setUnlocalizedName("dl.glassBoots").setCreativeTab(CreativeTabs.COMBAT);

        GameRegistry.registerItem(heart, "dl.heart");
        GameRegistry.registerItem(glassHelm, "dl.glassHelmet");
        GameRegistry.registerItem(glassChest, "dl.glassChestplate");
        GameRegistry.registerItem(glassLegs, "dl.glassLeggings");
        GameRegistry.registerItem(glassBoots, "dl.glassBoots");

        //TODO: Loot gen
//        if (DUNGEON_HEART_WEIGHT >= 0) {
//            ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//            ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//            ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
//        }

        OreDictionary.registerOre("heartCanister", heart);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassHelm),
                "GDG",
                "G G",
                'G', "blockGlass",
                'D', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassChest),
                "G G",
                "GDG",
                "GGG",
                'G', "blockGlass",
                'D', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassLegs),
                "GDG",
                "G G",
                "G G",
                'G', "blockGlass",
                'D', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassBoots),
                "G G",
                "GDG",
                'G', "blockGlass",
                'D', "gemDiamond"));
    }
}
