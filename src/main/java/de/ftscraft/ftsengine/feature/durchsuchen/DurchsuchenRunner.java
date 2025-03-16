package de.ftscraft.ftsengine.feature.durchsuchen;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DurchsuchenRunner implements Runnable {

    private int secondsElapsed = 0;
    private final Player target;
    private int taskId;

    public DurchsuchenRunner(Player target) {
        this.target = target;
    }
    
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        secondsElapsed++;
        if (secondsElapsed >= 60) {
            DurchsuchenManager.deny(target);
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }
}
