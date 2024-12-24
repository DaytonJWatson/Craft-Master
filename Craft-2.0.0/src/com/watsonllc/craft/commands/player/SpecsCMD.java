package com.watsonllc.craft.commands.player;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class SpecsCMD implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("craft.specs")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that"));
            return false;
        }

        sendHardwareSpecs(player);

        return false;
    }

    private void sendHardwareSpecs(Player player) {
        Runtime runtime = Runtime.getRuntime();
        File root = new File("/");

        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);

        long totalDiskSpace = root.getTotalSpace() / (1024 * 1024 * 1024);
        long freeDiskSpace = root.getFreeSpace() / (1024 * 1024 * 1024);

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        // Get system CPU load
        double systemCpuLoad = ((com.sun.management.OperatingSystemMXBean) osBean).getCpuLoad();

        // Get process CPU load
        double processCpuLoad = ((com.sun.management.OperatingSystemMXBean) osBean).getProcessCpuLoad();

        // Get number of processors
        int availableProcessors = osBean.getAvailableProcessors();

        // Get system load average
        double systemLoadAverage = osBean.getSystemLoadAverage();

        player.sendMessage(Utils.color("&6Server Hardware Specs:"));
        player.sendMessage(Utils.color("&7System CPU load: " + Math.round(systemCpuLoad * 100) + "%"));
        player.sendMessage(Utils.color("&7Process CPU load: " + Math.round(processCpuLoad * 100) + "%"));
        player.sendMessage(Utils.color("&7Available processors: &f" + availableProcessors));
        player.sendMessage(Utils.color("&7System load average: &f" + systemLoadAverage));
        player.sendMessage(Utils.color("&7Max Memory: &f" + maxMemory + "MB"));
        player.sendMessage(Utils.color("&7Used Memory: &f" + usedMemory + "MB"));
        player.sendMessage(Utils.color("&7Free Memory: &f" + freeMemory + "MB"));
        player.sendMessage(Utils.color("&7Total Disk Space: &f" + totalDiskSpace + "GB"));
        player.sendMessage(Utils.color("&7Free Disk Space: &f" + freeDiskSpace + "GB"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
