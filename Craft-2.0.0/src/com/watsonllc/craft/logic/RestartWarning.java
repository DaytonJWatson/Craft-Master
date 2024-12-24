package com.watsonllc.craft.logic;

import java.util.Calendar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

/**
 * Manages automatic server restart warnings and updates the player list footer with time remaining.
 * This class schedules warnings at specific intervals and calculates the time until the next restart.
 */
public class RestartWarning {

    private static final long ONE_HOUR_TICKS = 72000L;
    private static final long HALF_HOUR_TICKS = 36000L;
    private static final long FIFTEEN_MINUTES_TICKS = 18000L;
    private static final long FIVE_MINUTES_TICKS = 6000L;
    private static final int[] RESTART_HOURS = {6, 12, 18, 0};

    /**
     * Initializes and starts the restart warning tasks.
     * This method schedules both the restart warnings and the player list footer updates.
     */
    public static void startRestartWarningTask() {
        scheduleRestartWarnings();
        updatePlayerListFooter();
    }

    /**
     * Schedules restart warnings at specific intervals before the next restart.
     */
    private static void scheduleRestartWarnings() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long ticksToNextRestart = calculateTicksToNextRestart();

                scheduleWarning(ticksToNextRestart - ONE_HOUR_TICKS, "Restart in 1 hour!");
                scheduleWarning(ticksToNextRestart - HALF_HOUR_TICKS, "Restart in 30 minutes!");
                scheduleWarning(ticksToNextRestart - FIFTEEN_MINUTES_TICKS, "Restart in 15 minutes!");
                scheduleWarning(ticksToNextRestart - FIVE_MINUTES_TICKS, "Restart in 5 minutes!");
            }
        }.runTaskTimer(Main.instance, 0, 72000); // Check every 6 hours
    }

    /**
     * Calculates the number of ticks remaining until the next server restart.
     *
     * @return The number of ticks until the next restart.
     */
    private static long calculateTicksToNextRestart() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int nextRestartHour = 24;

        for (int restartHour : RESTART_HOURS) {
            if (hour < restartHour || (hour == restartHour && minute == 0)) {
                nextRestartHour = restartHour;
                break;
            }
        }

        if (nextRestartHour == 24) nextRestartHour = 0;

        Calendar nextRestart = (Calendar) now.clone();
        nextRestart.set(Calendar.HOUR_OF_DAY, nextRestartHour);
        nextRestart.set(Calendar.MINUTE, 0);
        nextRestart.set(Calendar.SECOND, 0);

        if (nextRestart.before(now)) nextRestart.add(Calendar.DAY_OF_MONTH, 1);

        long millisecondsToNextRestart = nextRestart.getTimeInMillis() - now.getTimeInMillis();
        return millisecondsToNextRestart / 50; // Convert milliseconds to ticks
    }

    /**
     * Schedules a broadcast message to warn players at a specific time before restart.
     *
     * @param delayTicks The delay in ticks until the message is broadcast.
     * @param message    The message to broadcast.
     */
    private static void scheduleWarning(long delayTicks, String message) {
        if (delayTicks > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(Utils.color("&fServer &7>> &f" + message));
                }
            }.runTaskLater(Main.instance, delayTicks);
        }
    }

    /**
     * Updates the footer of the player list with the time remaining until the next restart.
     */
    private static void updatePlayerListFooter() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long ticksToNextRestart = calculateTicksToNextRestart();
                long secondsToNextRestart = ticksToNextRestart / 20;

                long hours = secondsToNextRestart / 3600;
                long minutes = (secondsToNextRestart % 3600) / 60;
                long seconds = secondsToNextRestart % 60;

                String timeRemaining = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                String footer = Utils.color("&7Next Restart: &c" + timeRemaining);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setPlayerListFooter(footer);
                }
            }
        }.runTaskTimer(Main.instance, 0, 20); // Update every second
    }
}
