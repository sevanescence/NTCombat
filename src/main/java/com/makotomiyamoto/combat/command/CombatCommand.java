package com.makotomiyamoto.combat.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.nio.Buffer;
import java.util.Objects;

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
        if (args[0].equalsIgnoreCase("printmobdata") && args.length > 1) {
            String s = File.separator;
            File file = new File(system.getDataFolder() + s + "mob_data"
                    + s + args[1] + ".json");
            System.out.println(file.getPath());
            if (!file.exists()) {
                commandSender.sendMessage(args[1] + " does not exist.");
                return true;
            }
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            return true;
        }
        if (args[0].equalsIgnoreCase("testregex")) {
            String regex = "§r§5Hello!";
            System.out.println(regex);
            regex = regex.replaceAll("§r§[0-9,A-z]", "");
            System.out.println(regex);
            return true;
        }
        commandSender.sendMessage("Invalid arguments. Type /ntc for help.");
        return true;
    }
}
