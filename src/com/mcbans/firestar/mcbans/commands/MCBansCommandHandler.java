package com.mcbans.firestar.mcbans.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.mcbans.firestar.mcbans.BukkitInterface;
import com.mcbans.firestar.mcbans.Settings;
import com.mcbans.firestar.mcbans.log.LogLevels;

public class MCBansCommandHandler implements TabExecutor{
    private final BukkitInterface plugin;
    private final Settings config;

    // command map
    private Map<String, BaseCommand> commands = new HashMap<String, BaseCommand>();

    /**
     * Constructor
     */
    public MCBansCommandHandler(final BukkitInterface plugin, final Settings config){
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String commandLabel, String[] args) {
        final String commandName = command.getName().toLowerCase(Locale.ENGLISH);
        final BaseCommand cmd = commands.get(commandName);
        if (cmd == null){
            plugin.broadcastPlayer(sender, ChatColor.RED + "This command not loaded properly!");
            return true;
        }

        // Run the command
        cmd.run(plugin, sender, commandLabel, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, Command command, String commandLabel, String[] args) {
        final String commandName = command.getName().toLowerCase(Locale.ENGLISH);
        final BaseCommand cmd = commands.get(commandName);
        if (cmd == null){
            return null;
        }

        // check permission here
        if (sender != null && !cmd.permission(sender)){
            return null;
        }

        // Get tab complete list
        return cmd.tabComplete(plugin, sender, commandLabel, args);
    }

    public void registerCommand(BaseCommand bc){
        if (bc.name != null){
            commands.put(bc.name, bc);
        }else{
            plugin.log(LogLevels.WARNING, "Invalid command not registered! " + bc.getClass().getName());
        }
    }
}