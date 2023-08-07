package io.github.thebusybiscuit.sensibletoolbox.utils;

import javax.annotation.Nullable;

import io.github.thebusybiscuit.sensibletoolbox.SensibleToolboxPlugin;
import io.github.thebusybiscuit.sensibletoolbox.api.SensibleToolbox;
import io.github.thebusybiscuit.sensibletoolbox.items.Magnetoid;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MagnetoidTask extends BukkitRunnable {

    SensibleToolboxPlugin parent;

    public MagnetoidTask(SensibleToolboxPlugin plugin) {
        parent = plugin;
    }

    @Override
    public void run() {
        for (Player p : parent.getServer().getOnlinePlayers()) {
            ItemStack m = getMagnetoid(p);
            if (m != null ) {
                return;
            }
        }
    }


    @Nullable
    public static ItemStack getMagnetoid(Player p) {
        ItemStack i = p.getInventory().getItemInOffHand();
        Magnetoid m = SensibleToolbox.getItemRegistry().fromItemStack(i, Magnetoid.class);
        boolean playSound = false;

        if (m != null && m.getCharge() >= 1){
            for (Entity n : p.getNearbyEntities(8, 8, 8)) {
                if (n instanceof Item item) {
                    if (!SlimefunUtils.hasNoPickupFlag(item) && item.getPickupDelay() <= 0 && p.getLocation().distanceSquared(item.getLocation()) > 0.3 && !p.isSneaking()) {
                        item.teleport(p.getLocation());
                        m.setCharge(m.getCharge() - 1);
                        playSound = true;
                    }
                }
            }

            if (m.getCharge() <= 0) {
                m.setCharge(0);
                STBUtil.complain(p, UnicodeSymbol.ELECTRICITY.toUnicode() + " Magnetoid out of charge!");
            }

            if (playSound) {
                p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.PLAYERS, 2F, 0.9F);
            }

            p.getInventory().setItem(EquipmentSlot.OFF_HAND, m.toItemStack());
            //Debugger.getInstance().debug(1, "Charge: " + m.getCharge());

            return i;

        } else {
            return null;
        }
    }
}
