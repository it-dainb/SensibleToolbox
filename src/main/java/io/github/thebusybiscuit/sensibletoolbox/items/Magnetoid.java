package io.github.thebusybiscuit.sensibletoolbox.items;

import io.github.thebusybiscuit.sensibletoolbox.items.components.IntegratedCircuit;
import io.github.thebusybiscuit.sensibletoolbox.items.components.SubspaceTransponder;
import io.github.thebusybiscuit.sensibletoolbox.items.energycells.EnergyCell;
import io.github.thebusybiscuit.sensibletoolbox.items.energycells.FiftyKEnergyCell;
import io.github.thebusybiscuit.sensibletoolbox.items.itemroutermodules.VacuumModule;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;

import javax.annotation.ParametersAreNonnullByDefault;

public class Magnetoid extends EnergyCell {

    @ParametersAreNonnullByDefault
    public Magnetoid() { super(); }

    public Magnetoid(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public int getMaxCharge() {
        return 250000;
    }

    @Override
    public int getChargeRate() {
        return 1000;
    }

    @Override
    public Color getCellColor() {
        return null;
    }

    public Material getMaterial() {
        return Material.ECHO_SHARD;
    }

    @Override
    public String getItemName() {
        return "Magnetoid";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Collects objects in a 8", "block radius automatically.", ChatColor.AQUA + "Offhand Gadget" };
    }

    @Override
    public boolean hasGlow() {
        return true;
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getKey(), toItemStack());
        IntegratedCircuit ic = new IntegratedCircuit();
        VacuumModule vm = new VacuumModule();
        FiftyKEnergyCell fkc = new FiftyKEnergyCell();
        SubspaceTransponder sst = new SubspaceTransponder();
        registerCustomIngredients(ic);
        registerCustomIngredients(vm);
        registerCustomIngredients(fkc);
        registerCustomIngredients(sst);
        recipe.shape("CVC", "FSF", "CVC");
        recipe.setIngredient('V', vm.getMaterial());
        recipe.setIngredient('C', ic.getMaterial());
        recipe.setIngredient('F', fkc.getMaterial());
        recipe.setIngredient('S', sst.getMaterial());
        return recipe;
    }
}
