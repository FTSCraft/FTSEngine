package de.ftscraft.ftsengine.feature.roleplay.durchsuchen;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DurchsuchenRunner implements Runnable {

    private int secondsElapsed = 0;
    private final Player target;
    private int taskId;
    private final RunnerType type;

    public DurchsuchenRunner(Player target, RunnerType type) {
        this.target = target;
        this.type = type;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        secondsElapsed++;
        if (secondsElapsed >= 60) {
            System.out.println("Closed by runner!");
            Bukkit.getScheduler().cancelTask(taskId);
            if (type.equals(RunnerType.SEARCH_RUNNER))
                DurchsuchenManager.deny(target);
            else
                DurchsuchenManager.closeHideInventory(target);
        }
    }

    public enum RunnerType {
        SEARCH_RUNNER,
        HIDE_RUNNER
    }
}
