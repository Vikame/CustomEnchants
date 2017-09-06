package me.threadsafe.customenchants;

import me.threadsafe.customenchants.command.EnchantCommand;
import me.threadsafe.customenchants.command.SafeCommand;
import me.threadsafe.customenchants.enchants.AutoSmeltEnchant;
import me.threadsafe.customenchants.enchants.ExcavationEnchant;
import me.threadsafe.customenchants.enchants.MultiToolEnchant;
import me.threadsafe.customenchants.helper.EnchantmentHelper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CustomEnchants extends JavaPlugin{

    private static CustomEnchants instance;

    private EnchantmentCache enchantmentCache;

    public void onEnable(){
        instance = this;
        this.enchantmentCache = new EnchantmentCache();

        this.enchantmentCache.register(new ExcavationEnchant());
        this.enchantmentCache.register(new AutoSmeltEnchant());
        this.enchantmentCache.register(new MultiToolEnchant());

        getCommand("safe").setExecutor(new SafeCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("enchant").setTabCompleter(new EnchantCommand.TabCompleter());
    }

    public EnchantmentCache getEnchantmentCache(){
        return enchantmentCache;
    }

    @SuppressWarnings("unchecked")
    public void onDisable(){
        try{
            Field idField = Enchantment.class.getDeclaredField("byId");
            Field nameField = Enchantment.class.getDeclaredField("byName");

            idField.setAccessible(true);
            nameField.setAccessible(true);

            Map<Integer, Enchantment> ids = (HashMap<Integer, Enchantment>)idField.get(null);
            Map<String, Enchantment> names = (HashMap<String, Enchantment>)nameField.get(null);

            for(CustomEnchant enchant : enchantmentCache.all()){
                if(ids.containsKey(enchant.getId())) ids.remove(enchant.getId());
                if(names.containsKey(enchant.getName())) names.remove(enchant.getName());
            }

            enchantmentCache.all().clear();
        }catch(ReflectiveOperationException e){
            e.printStackTrace();
        }
    }

    public static CustomEnchants getInstance() {
        return instance;
    }

}
