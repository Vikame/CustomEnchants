package me.threadsafe.customenchants;

import me.threadsafe.customenchants.helper.EnchantmentHelper;
import me.threadsafe.customenchants.helper.RomanHelper;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomEnchant extends Enchantment implements Listener {

    // Maybe utilize any available IDs that aren't used by Minecraft already? (e.g. 11-15)
    private static int NEXT_AVAILABLE_ID = 101;

    protected final String name;
    protected final int maxLevel;
    protected final EnchantmentTarget target;

    public CustomEnchant(String name, int maxLevel, EnchantmentTarget target){
        super(NEXT_AVAILABLE_ID++);
        this.name = name;
        this.maxLevel = maxLevel;
        this.target = target;
    }

    public CustomEnchant(String name, int maxLevel, EnchantmentTarget target, String... aliases) {
        super(NEXT_AVAILABLE_ID++);
        this.name = name;
        this.maxLevel = maxLevel;
        this.target = target;

        EnchantmentHelper.addAliases(this, aliases);
    }

    public void addToItem(ItemStack item, int level){
        item.addUnsafeEnchantment(this, level);

        ItemMeta meta = item.getItemMeta();

        List<String> lore = (meta.hasLore() ? item.getItemMeta().getLore() : new ArrayList<>());

        lore.removeIf((String s) -> s.startsWith(ChatColor.GRAY + getName() + " "));

//        Only use if we are not on Java 8+
//
//        Iterator<String> it = lore.iterator();
//        while(it.hasNext()){
//            String s = it.next();
//            if(s.startsWith(ChatColor.GRAY + getName() + " ")) it.remove();
//        }

        lore.add(ChatColor.GRAY + getName() + " " + RomanHelper.toRoman(level));

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void removeFromItem(ItemStack item) {
        item.removeEnchantment(this);

        ItemMeta meta = item.getItemMeta();

        List<String> lore = (meta.hasLore() ? item.getItemMeta().getLore() : new ArrayList<>());

        lore.removeIf((String s) -> s.startsWith(ChatColor.GRAY + getName() + " "));

//        Only use if we are not on Java 8+
//
//        Iterator<String> it = lore.iterator();
//        while(it.hasNext()){
//            String s = it.next();
//            if(s.startsWith(ChatColor.GRAY + getName() + " ")) it.remove();
//        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return target != null ? target : EnchantmentTarget.ALL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return getItemTarget().includes(itemStack);
    }

}
