package difficultLife.utils;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

/**
 * Created by dmillerw
 */
public class DLPotionHelper {

    public static Potion getPotionFromType(PotionType type) {
        return Potion.REGISTRY.getObject(type.getRegistryName());
    }

    public static PotionEffect potionEffect(PotionType type) {
        return new PotionEffect(getPotionFromType(type));
    }

    public static PotionEffect potionEffect(PotionType potionIn, int durationIn) {
        return potionEffect(potionIn, durationIn, 0);
    }

    public static PotionEffect potionEffect(PotionType potionIn, int durationIn, int amplifierIn) {
        return potionEffect(potionIn, durationIn, amplifierIn, false, true);
    }

    public static PotionEffect potionEffect(PotionType potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn) {
        return new PotionEffect(getPotionFromType(potionIn), durationIn, amplifierIn, ambientIn, showParticlesIn);
    }
}
