package io.github.thebusybiscuit.sensibletoolbox.utils;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class MagnetoidTask extends PlayerTask {

    private final double RADIUS;

    public MagnetoidTask(@Nonnull Player p, double radius) {
        super(p);

        this.RADIUS = radius;
    }

    @Override
    protected void executeTask() {
        boolean playSound = false;

        for (Entity n : p.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (n instanceof Item item) {
                if (!SlimefunUtils.hasNoPickupFlag(item) && item.getPickupDelay() <= 0 && p.getLocation().distanceSquared(item.getLocation()) > 0.3 && !p.isSneaking()) {
                    item.teleport(p.getLocation());
                    playSound = true;
                }
            }
        }

        // Only play a sound if an Item was found
        if (playSound) {
            p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.PLAYERS, 2F, 0.9F);
        }
    }

    @Override
    protected boolean isValid() {
        return super.isValid() && p.getGameMode() != GameMode.SPECTATOR;
    }

}
