package me.threadsafe.customenchants.enchants;

import me.threadsafe.customenchants.CustomEnchant;
import me.threadsafe.customenchants.helper.Items;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MultiToolEnchant extends CustomEnchant {

    public MultiToolEnchant() {
        super("Multi-Tool", 1, EnchantmentTarget.TOOL, "Multi-Tool", "Multi Tool", "MultiTool");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        ItemStack i = p.getItemInHand();

        if(i == null || i.getType() == null) return;

        if(i.containsEnchantment(this)) {
            if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Block b = e.getClickedBlock();

                Items.MaterialType type = Items.MaterialType.getType(i.getType());
                if (type == null) return;

                Material mat = type.getMaterialCanBreak(b);

                if (mat == null) return;
                i.setType(mat);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player p = (Player)e.getDamager();

            ItemStack i = p.getItemInHand();

            if(i == null || i.getType() == null) return;

            if(i.containsEnchantment(this)){
                Items.MaterialType type = Items.MaterialType.getType(i.getType());
                if (type == null) return;

                i.setType(type.getSword());
            }
        }
    }

}
