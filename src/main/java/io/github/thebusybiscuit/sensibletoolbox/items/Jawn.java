package io.github.thebusybiscuit.sensibletoolbox.items;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.items.components.EnergizedIronIngot;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;

import me.desht.dhutils.Debugger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class Jawn extends BaseSTBItem {

    public Jawn() {
        super();
    }

    public Jawn(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return Material.FEATHER;
    }

    @Override
    public String getItemName() {
        return "Jawn";
    }

    @Override
    public String[] getLore() {
        return new String[]{"Use on placed head items", "to instantly pick up. Works", "for any type of head.", "R-Click: " + ChatColor.WHITE + "Break and Collect"};
    }

    @Override
    public boolean hasGlow() {
        return true;
    }

    @Override
    public Recipe getMainRecipe() {
        EnergizedIronIngot ei = new EnergizedIronIngot();
        registerCustomIngredients(ei);
        ShapedRecipe recipe = new ShapedRecipe(getKey(), toItemStack());
        recipe.shape(" I ", " C ", "CRC");
        recipe.setIngredient('I', ei.getMaterial());
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('R', Material.REDSTONE);
        return recipe;
    }

    @Override
    public void onInteractItem(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();

            if (Slimefun.getProtectionManager().hasPermission(event.getPlayer(), block, Interaction.BREAK_BLOCK)
                && (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD)) {
                BlockBreakEvent e = new BlockBreakEvent(block, player);

                event.setCancelled(true);
                Bukkit.getPluginManager().callEvent(e);
                block.setType(Material.AIR);

            }
        }
    }
}

