package me.threadsafe.customenchants;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EnchantmentCache {

    private final List<CustomEnchant> enchants;

    public EnchantmentCache(){
        this.enchants = new ArrayList<>();

        try{
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }catch(ReflectiveOperationException e){
            e.printStackTrace();
        }
    }

    public CustomEnchant register(CustomEnchant enchant){
        Enchantment.registerEnchantment(enchant);
        Bukkit.getPluginManager().registerEvents(enchant, CustomEnchants.getInstance());
        enchants.add(enchant);

        return enchant;
    }

    // Not really necessary, possibly keep for API usage?
    public CustomEnchant remove(CustomEnchant enchant){
        enchants.remove(enchant);
        return enchant;
    }

    public List<CustomEnchant> all(){
        return enchants;
    }

    // Preferably keep a class with references to the CustomEnchant class you need, rather than getting it from this.
    // This method is only needed for API usage.
    public CustomEnchant get(String s) {
        for(CustomEnchant enchant : enchants){
            if(enchant.getName().equalsIgnoreCase(s)) return enchant;
        }

        return null;
    }
}
