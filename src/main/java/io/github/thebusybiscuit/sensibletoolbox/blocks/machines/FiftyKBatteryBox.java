package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

import io.github.thebusybiscuit.sensibletoolbox.api.util.STBUtil;
import io.github.thebusybiscuit.sensibletoolbox.items.energycells.FiftyKEnergyCell;

public class FiftyKBatteryBox extends BatteryBox {
    private static final MaterialData md = STBUtil.makeColouredMaterial(Material.STAINED_GLASS, DyeColor.PURPLE);

    public FiftyKBatteryBox() {
    }

    public FiftyKBatteryBox(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public MaterialData getMaterialData() {
        return md;
    }

    @Override
    public String getItemName() {
        return "50K Battery Box";
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(toItemStack());
        recipe.shape("GGG", "GCG", "RIR");
        FiftyKEnergyCell cell = new FiftyKEnergyCell();
        cell.setCharge(0.0);
        registerCustomIngredients(cell);
        recipe.setIngredient('G', Material.GLASS);
        recipe.setIngredient('C', STBUtil.makeWildCardMaterialData(cell));
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('I', Material.GOLD_INGOT);
        return recipe;
    }

    @Override
    public int getMaxCharge() {
        return 50000;
    }

    @Override
    public int getChargeRate() {
        return isRedstoneActive() ? 500 : 0;
    }
}