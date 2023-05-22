package io.github.thebusybiscuit.sensibletoolbox.utils;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.sensibletoolbox.SensibleToolboxPlugin;
import me.desht.dhutils.MiscUtil;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;

/**
 * This utility class is used to display a holographic pop up using the {@link HologramsAPI}.
 * 
 * @author desht
 * @author TheBusyBiscuit
 *
 */
public final class HoloMessage {

    private HoloMessage() {}
    
    private static HolographicDisplaysAPIProvider impl = HolographicDisplaysAPIProvider.getImplementation();
    private static HolographicDisplaysAPI hologramapi = impl.getHolographicDisplaysAPI(SensibleToolboxPlugin.getInstance());

    @ParametersAreNonnullByDefault
    public static void popup(Player player, Location loc, String... message) {
        if (!SensibleToolboxPlugin.getInstance().isHolographicDisplaysEnabled() || !SensibleToolboxPlugin.getInstance().getConfig().getBoolean("holograms.enabled")) {
            for (String line : message) {
                MiscUtil.statusMessage(player, line);
            }

            return;
        }

        Vector v = player.getLocation().getDirection();
        v.setY(0).multiply(-0.8).add(new Vector(0.5, 0.8, 0.5));

        Hologram h = hologramapi.createHologram(loc.add(v));
        
        SensibleToolboxPlugin.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SensibleToolboxPlugin.getInstance(), h::delete, SensibleToolboxPlugin.getInstance().getConfig().getInt("holograms.duration-in-seconds"));
    }
}
