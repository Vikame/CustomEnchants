package me.threadsafe.customenchants.enchants;

import me.threadsafe.customenchants.CustomEnchant;
import me.threadsafe.customenchants.CustomEnchants;
import me.threadsafe.customenchants.helper.Items;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ExcavationEnchant extends CustomEnchant {

    // Needed for support with other plugins, could also add built in support for WorldGuard or Factions rather than leaving this mess
    private final List<BlockBreakEvent> noRepeat;

    private final BlockFace[] check = new BlockFace[]{BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    public ExcavationEnchant() {
        super("Excavation", 2, EnchantmentTarget.TOOL, "Excavation", "Exca");
        noRepeat = new ArrayList<>();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(noRepeat.contains(e)){
            // This should be changed to manual checks for any protection plugins (e.g. WorldGuard, Factions)
            noRepeat.remove(e);
            return;
        }
        if(e.isCancelled()) return;

        Player p = e.getPlayer();

        ItemStack i = p.getItemInHand();

        if(i == null || i.getType() == null) return;

        if(i.containsEnchantment(this)){
            Items.ItemType type = Items.getType(i);

            if(type == null) return;

            Block block = e.getBlock();

            boolean safe = p.hasMetadata("safe");

            if(safe && !isSafeBlock(block)){
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "That block cannot be mined as it is unsafe. If you wish to mine this block, turn excavation safe-mode off using /safe.");
                return;
            }

            if(!type.canBreak(block) && !i.getItemMeta().hasEnchant(CustomEnchants.getInstance().getEnchantmentCache().get("Multi-Tool"))) return;

            Location l = block.getLocation();

            int radius = (int)Math.floor(((i.getEnchantmentLevel(this)*2)/2));
            for(int x = l.getBlockX() + radius; x >= l.getBlockX() - radius; x--){
                for(int y = l.getBlockY() + radius; y >= l.getBlockY() - radius; y--){
                    for(int z = l.getBlockZ() + radius; z >= l.getBlockZ() - radius; z--){
                        Block b = new Location(l.getWorld(), x, y, z).getBlock();

                        if(safe && !isSafeBlock(b)) continue;

                        Material t = b.getType();
                        if(t == Material.BEDROCK || t == Material.ENDER_PORTAL || t == Material.ENDER_PORTAL_FRAME
                                || t == Material.PORTAL || b.isLiquid()) continue; // Unbreakable blocks shouldn't be broken, that is why they are unbreakable right?

                        BlockBreakEvent event = new BlockBreakEvent(b, p);
                        noRepeat.add(event);

                        Bukkit.getPluginManager().callEvent(event);

                        if(event.isCancelled()) continue;

                        if(type.canBreak(b) || i.getItemMeta().hasEnchant(CustomEnchants.getInstance().getEnchantmentCache().get("Multi-Tool"))){
                            if(p.getGameMode() == GameMode.CREATIVE) b.setType(Material.AIR); // We want vanilla behavior, so we don't want to drop items for players in creative mode.
                            else b.breakNaturally(i);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return getItemTarget().includes(itemStack) && itemStack.getType() != Material.SHEARS;
    }

    // Checks if a block is safe, a safe block is determined on whether or not it is next to a liquid.
    // This check also checks if the block above the broken block has gravity, if it does, recursion is used to check if it is safe.
    // E.G (W = Water, G = Gravity Block, S = Solid Block)

    /*
     *   W
     *   G    The solid block and both gravity blocks are unsafe because the solid block being broken would allow the gravity blocks to fall, meaning the water could drop and flood the mine.
     *   G
     *   S
     *
     *
     *
     *
     *
     *   W
     *   S
     *   G    The solid block at the bottom and both gravity blocks are safe, but the solid block underneath the water is not.
     *   G
     *   S
     */
    private boolean isSafeBlock(Block b){
        if(isNextToLiquid(b)) return false;

        Block above = b.getRelative(BlockFace.UP);

        return !above.getType().hasGravity() || isSafeBlock(above);
    }

    private boolean isNextToLiquid(Block b){
        for(BlockFace face : check){
            if(b.getRelative(face).isLiquid()) return true;
        }

        return false;
    }

}
