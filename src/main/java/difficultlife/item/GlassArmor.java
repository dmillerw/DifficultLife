package difficultLife.item;

import difficultLife.DLCore;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class GlassArmor extends ItemArmor {

    public GlassArmor(ArmorMaterial material, int renderType, EntityEquipmentSlot slot) {
        super(material, renderType, slot);
    }

    public Item setTextureName(String string) {
        //NOOP
        //TODO Actually do proper item rendering
        return this;
    }
}
