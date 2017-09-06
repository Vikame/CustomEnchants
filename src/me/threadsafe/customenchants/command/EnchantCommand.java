package me.threadsafe.customenchants.command;

import me.threadsafe.customenchants.CustomEnchant;
import me.threadsafe.customenchants.helper.EnchantmentHelper;
import me.threadsafe.customenchants.helper.RomanHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


// Override the bukkit enchant command to allow the usage of /enchant <enchantmentName> <level> instead of /enchant <player> <enchantmentId> <level>
// Our IDs can be fairly confusing, so using an enchantment name over an id is preferred.
public class EnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.hasPermission("customenchants.enchant")) {
            sender.sendMessage(ChatColor.RED + "You cannot do that.");
            return true;
        }

        if(args.length <= 1){
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <name | id> <level>");
            return true;
        }

        Player p = (Player)sender;

        ItemStack item = p.getItemInHand();
        if(item == null || item.getType() == Material.AIR){
            sender.sendMessage(ChatColor.RED + "You cannot enchant nothing you dummy.");
            return true;
        }

        String name = "";

        for(int i = 0; i < args.length-1; i++){
            name += args[i] + " ";
        }

        name = name.trim();

        Enchantment selected = EnchantmentHelper.getEnchantmentByArgument(name);

        if(selected == null){
            sender.sendMessage(ChatColor.RED + "No enchantment named '" + args[0] + "' was found.");
            return true;
        }

        int level = 0;
        try{
            level = Integer.parseInt(args[args.length-1]);
        }catch(NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "" + args[args.length-1] + " is not a valid level.");
            return true;
        }

        if(level > selected.getMaxLevel() && !p.hasPermission("customenchants.enchant.override")){
            sender.sendMessage(ChatColor.RED + "The maximum level for " + EnchantmentHelper.getNameByEnchantment(selected) + " is " + selected.getMaxLevel() + ".");
            return true;
        }

        if(level == 0){
            if(selected instanceof CustomEnchant){
                ((CustomEnchant)selected).removeFromItem(item);
            }else item.removeEnchantment(selected);

            sender.sendMessage(ChatColor.GOLD + "You have removed " + ChatColor.WHITE + EnchantmentHelper.getNameByEnchantment(selected) + ChatColor.GOLD + " from the item in your hand.");
        }else {
            if (selected instanceof CustomEnchant) {
                ((CustomEnchant) selected).addToItem(item, level);
            } else item.addUnsafeEnchantment(selected, level);

            sender.sendMessage(ChatColor.GOLD + "You have applied " + ChatColor.WHITE + EnchantmentHelper.getNameByEnchantment(selected) + " " + RomanHelper.toRoman(level) + ChatColor.GOLD + " to the item in your hand.");
        }
        return true;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {

        public static final boolean ONLY_SHOW_FIRST_ALIAS = true;

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
            if(!(sender instanceof Player) || !sender.hasPermission("customenchants.enchant")) return null;

            List<String> suggestions = new ArrayList<>();
            Map<Enchantment, List<String>> aliases = EnchantmentHelper.getAllAliases();

            if(args.length == 0) {
                for (List<String> list : aliases.values()) {
                    for(String s : list){
                        suggestions.add(s);

                        // Could just be break;, however its possible that it could be forgotten and wouldn't be removed when changing ONLY_SHOW_FIRST_ALIAS to false.
                        if(ONLY_SHOW_FIRST_ALIAS) break;
                    }
                }
            }else if(args.length == 1){
                String ench = args[0].toLowerCase();

                for(Map.Entry<Enchantment, List<String>> entry : aliases.entrySet()){
                    for(String s : entry.getValue()) {
                        if (s.toLowerCase().startsWith(ench)) {
                            suggestions.add(s);

                            if(ONLY_SHOW_FIRST_ALIAS) break;
                        }
                    }
                }
            }

            return suggestions;
        }

    }

}
