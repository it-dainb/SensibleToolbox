package io.github.thebusybiscuit.sensibletoolbox.listeners;

import io.github.thebusybiscuit.sensibletoolbox.SensibleToolboxPlugin;
import io.github.thebusybiscuit.sensibletoolbox.api.SensibleToolbox;
import io.github.thebusybiscuit.sensibletoolbox.items.Magnetoid;
import io.github.thebusybiscuit.sensibletoolbox.utils.MagnetoidTask;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;


public class MagnetoidListener extends STBBaseListener {

    public MagnetoidListener(SensibleToolboxPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) {
            Player p = e.getPlayer();

            if (SensibleToolbox.getItemRegistry().isSTBItem(p.getInventory().getItemInOffHand(), Magnetoid.class)) {
                new MagnetoidTask(p, 6).scheduleRepeating(0, 14);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (SensibleToolbox.getItemRegistry().isSTBItem(p.getInventory().getItemInOffHand(), Magnetoid.class)) {
            new MagnetoidTask(p, 6).scheduleRepeating(0, 14);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player) {
            Player p = ((Player) e.getDamager()).getPlayer();

            if (SensibleToolbox.getItemRegistry().isSTBItem(p.getInventory().getItemInOffHand(), Magnetoid.class)) {
                new MagnetoidTask(p, 6).scheduleRepeating(0, 14);
            }
        }
    }
}


