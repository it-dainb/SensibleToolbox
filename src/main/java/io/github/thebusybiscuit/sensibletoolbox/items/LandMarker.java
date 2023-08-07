package io.github.thebusybiscuit.sensibletoolbox.items;

import java.util.UUID;

import io.github.thebusybiscuit.sensibletoolbox.api.SensibleToolbox;
import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.items.components.SimpleCircuit;
import me.desht.dhutils.MiscUtil;

public class LandMarker extends BaseSTBItem {

    private Location loc;

    public LandMarker() {
        loc = null;
    }

    public LandMarker(ConfigurationSection conf) {
        if (conf.contains("worldId")) {
            UUID worldId = UUID.fromString(conf.getString("worldId"));
            World w = Bukkit.getWorld(worldId);
            if (w != null) {
                loc = new Location(w, conf.getInt("x"), conf.getInt("y"), conf.getInt("z"));
            } else {
                loc = null;
            }
        } else {
            loc = null;
        }
    }

    @Override
    public YamlConfiguration freeze() {
        YamlConfiguration conf = super.freeze();

        if (loc != null) {
            conf.set("worldId", loc.getWorld().getUID().toString());
            conf.set("x", loc.getBlockX());
            conf.set("y", loc.getBlockY());
            conf.set("z", loc.getBlockZ());
        }

        return conf;
    }

    @Override
    public Material getMaterial() {
        return Material.FIREWORK_ROCKET;
    }

    @Override
    public String getItemName() {
        return "Land Marker";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Stores positions via Sensible GPS", "R-Click block: store position", "R-Click air: clear position" };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getKey(), toItemStack());
        recipe.shape(" T ", " C ", " S ");
        SimpleCircuit sc = new SimpleCircuit();
        registerCustomIngredients(sc);
        recipe.setIngredient('T', Material.REDSTONE_TORCH);
        recipe.setIngredient('C', sc.getMaterial());
        recipe.setIngredient('S', Material.STICK);
        return recipe;
    }

    @Override
    public String getDisplaySuffix() {
        return loc == null ? null : MiscUtil.formatLocation(loc);
    }

    public Location getMarkedLocation() {
        return loc;
    }

    public void setMarkedLocation(Location loc) {
        this.loc = loc;
    }

    @Override
    public void onInteractItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) && getMarkedLocation() != null) {
            setMarkedLocation(null);
            updateHeldItemStack(event.getPlayer(), event.getHand());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.6F);
        } else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) && !event.getClickedBlock().getLocation().equals(loc)) {
            BaseSTBBlock stb = SensibleToolbox.getBlockAt(event.getClickedBlock().getLocation());
            if (stb == null) {
                setMarkedLocation(event.getClickedBlock().getLocation());
                updateHeldItemStack(event.getPlayer(), event.getHand());
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
            } else {
                stb.onInteractBlock(event);
            }
        }
        event.setCancelled(true);
    }
}
