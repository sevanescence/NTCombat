package com.makotomiyamoto.combat.command;

import com.makotomiyamoto.combat.CombatSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CombatCommand implements CommandExecutor {
    private CombatSystem system;
    public CombatCommand(CombatSystem system) {
        this.system = system;
    }
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
            commandSender.sendMessage("[]============[NTCombat]============[]");
            commandSender.sendMessage("[]= /ntc r[eload]");
            commandSender.sendMessage("[]=== Reloads mob configurations.");
            commandSender.sendMessage("[]============[NTCombat]============[]");
            return true;
        }
        if (args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("reload")) {
            commandSender.sendMessage("[" + system.getName() + "] Reloading...");
            system.onEnable();
            commandSender.sendMessage("[" + system.getName() + "] Reload complete!");
            return true;
        }
        commandSender.sendMessage("Invalid arguments. Type /ntc for help.");
        return true;
    }
}
