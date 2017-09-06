package me.threadsafe.customenchants.enchants;

import me.threadsafe.customenchants.CustomEnchant;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AutoSmeltEnchant extends CustomEnchant {

    // Storing an ItemStack probably isn't the best, maybe make a wrapper for the material type and data value?
    private final Map<Material, ItemStack> smelted;

    public AutoSmeltEnchant() {
        super("Auto-Smelt", 1, EnchantmentTarget.TOOL, "Auto-Smelt", "Auto Smelt", "AutoSmelt");
        smelted = new HashMap<>();

        smelted.put(Material.COBBLESTONE, new ItemStack(Material.STONE));
        smelted.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT));
        smelted.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        smelted.put(Material.LOG, new ItemStack(Material.COAL, 1, (short)1));
        smelted.put(Material.LOG_2, new ItemStack(Material.COAL, 1, (short)1));
        smelted.put(Material.SAND, new ItemStack(Material.GLASS));
        smelted.put(Material.NETHERRACK, new ItemStack(Material.NETHER_BRICK_ITEM));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(e.isCancelled()) return;

        Player p = e.getPlayer();

        if(p.getGameMode() == GameMode.CREATIVE) return;

        ItemStack i = p.getItemInHand();

        if(i == null || i.getType() == null) return;

        if(i.containsEnchantment(this)){
            Block b = e.getBlock();
            Collection<ItemStack> oldDrops = b.getDrops(i);
            List<ItemStack> drops = new ArrayList<>();

            int fortune = (i.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) ? i.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1 : 0);

            for(ItemStack item : oldDrops){
                Material type = item.getType();

                if(smelted.containsKey(type)){
                    ItemStack newStack = smelted.get(type).clone();
                    newStack.setAmount(item.getAmount());

                    if(fortune != 0){
                        newStack.setAmount(newStack.getAmount() * (ThreadLocalRandom.current().nextInt(fortune) + 1));
                    }

                    drops.add(newStack);
                }else drops.add(item);
            }

            if(drops.isEmpty()) return;

            e.setCancelled(true);
            b.setType(Material.AIR);
            for(ItemStack stack : drops) b.getWorld().dropItemNaturally(b.getLocation(), stack);
        }
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return getItemTarget().includes(itemStack) && itemStack.getType() != Material.SHEARS;
    }

}
