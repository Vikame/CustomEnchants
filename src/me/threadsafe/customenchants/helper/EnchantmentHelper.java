package me.threadsafe.customenchants.helper;

import me.threadsafe.customenchants.CustomEnchant;
import me.threadsafe.customenchants.CustomEnchants;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentHelper {

    private static final Map<Enchantment, List<String>> aliases = new HashMap<>();

    static {

        addAliases(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection", "Prot");
        addAliases(Enchantment.PROTECTION_FIRE, "Fire Protection", "Fire_Protection", "Fire Prot", "Fire_Prot");
        addAliases(Enchantment.PROTECTION_FALL, "Feather Falling", "Feather_Falling");
        addAliases(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection", "Blast_Protection", "Blast Prot", "Blast_Prot");
        addAliases(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection", "Projectile_Protection", "Projectile Prot", "Projectile_Prot", "Proj Prot", "Proj_Prot");
        addAliases(Enchantment.OXYGEN, "Respiration");
        addAliases(Enchantment.WATER_WORKER, "Aqua Affinity", "Aqua_Affinity");
        addAliases(Enchantment.THORNS, "Thorns");
        addAliases(Enchantment.DAMAGE_ALL, "Sharpness", "Sharp");
        addAliases(Enchantment.DAMAGE_UNDEAD, "Smite");
        addAliases(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods", "Bane_of_Arthropods", "Bane");
        addAliases(Enchantment.KNOCKBACK, "Knockback", "KB");
        addAliases(Enchantment.FIRE_ASPECT, "Fire Aspect", "Fire_Aspect");
        addAliases(Enchantment.LOOT_BONUS_MOBS, "Looting");
        addAliases(Enchantment.DIG_SPEED, "Efficiency");
        addAliases(Enchantment.SILK_TOUCH, "Silk Touch", "Silk_Touch");
        addAliases(Enchantment.DURABILITY, "Unbreaking");
        addAliases(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");
        addAliases(Enchantment.ARROW_DAMAGE, "Power");
        addAliases(Enchantment.ARROW_KNOCKBACK, "Punch");
        addAliases(Enchantment.ARROW_FIRE, "Flame");
        addAliases(Enchantment.ARROW_INFINITE, "Infinity");
        addAliases(Enchantment.LUCK, "Luck of the Sea", "Luck_of_the_Sea");
        addAliases(Enchantment.LURE, "Lure");

    }

    public static void addAliases(Enchantment enchantment, String... aliases){
        EnchantmentHelper.aliases.put(enchantment, Arrays.asList(aliases));
    }

    public static String getNameByEnchantment(Enchantment enchantment){
        if(!aliases.containsKey(enchantment)) return enchantment.getName();

        List<String> list = aliases.get(enchantment);
        if(list.isEmpty()) return enchantment.getName();

        return list.get(0);
    }

    public static Enchantment getEnchantmentByArgument(String name){
        for(Enchantment ench : Enchantment.values())
            if(ench.getName().equalsIgnoreCase(name) || ench.getName().replace("_", " ").equalsIgnoreCase(name) || (ench.getId() + "").equalsIgnoreCase(name)) return ench;

        for(Map.Entry<Enchantment, List<String>> entry : aliases.entrySet()){
            for(String s : entry.getValue())
                if(s.equalsIgnoreCase(name)) return entry.getKey();
        }

        return null;
    }

    public static final Map<Enchantment, List<String>> getAllAliases(){
        return aliases;
    }

}
