package me.threadsafe.customenchants.command;

import me.threadsafe.customenchants.CustomEnchants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class SafeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You cannot do that.");
            return true;
        }

        Player p = (Player)sender;

        // Bad, should probably swap over to the usage of MySQL or configs
        if(p.hasMetadata("safe")){
            p.removeMetadata("safe", CustomEnchants.getInstance());
            p.sendMessage(ChatColor.GOLD + "You have turned excavation safe-mode " + ChatColor.RED + "off" + ChatColor.GOLD + ".");
        }else{
            p.setMetadata("safe", new FixedMetadataValue(CustomEnchants.getInstance(), true));
            p.sendMessage(ChatColor.GOLD + "You have turned excavation safe-mode " + ChatColor.GREEN + "on" + ChatColor.GOLD + ".");
        }
        return true;
    }

}
