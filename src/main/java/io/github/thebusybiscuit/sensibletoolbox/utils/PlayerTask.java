package io.github.thebusybiscuit.sensibletoolbox.utils;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.sensibletoolbox.SensibleToolboxPlugin;
import io.github.thebusybiscuit.sensibletoolbox.api.SensibleToolbox;
import io.github.thebusybiscuit.sensibletoolbox.items.Magnetoid;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

abstract class PlayerTask implements Runnable {

    protected final Player p;
    private int id;

    PlayerTask(@Nonnull Player p) {
        this.p = p;
    }

    private void setID(int id) {
        this.id = id;
    }

    public void schedule(long delay) {
        setID(Bukkit.getScheduler().scheduleSyncDelayedTask(SensibleToolboxPlugin.getInstance(), this, delay));
    }

    public void scheduleRepeating(long delay, long interval) {
        setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SensibleToolboxPlugin.getInstance(), this, delay, interval));
    }

    @Override
    public final void run() {
        if (isValid()) {
            executeTask();
        }
    }

    public final void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    protected boolean isValid() {
        if (!p.isOnline() || !p.isValid() || p.isDead() || !SensibleToolbox.getItemRegistry().isSTBItem(p.getInventory().getItemInOffHand(), Magnetoid.class)) {
            cancel();
            return false;
        }

        return true;
    }

    protected abstract void executeTask();
}

