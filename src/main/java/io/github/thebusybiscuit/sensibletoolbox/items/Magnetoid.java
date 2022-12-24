package io.github.thebusybiscuit.sensibletoolbox.items;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.items.components.IntegratedCircuit;
import io.github.thebusybiscuit.sensibletoolbox.items.components.SubspaceTransponder;
import io.github.thebusybiscuit.sensibletoolbox.items.energycells.FiftyKEnergyCell;
import io.github.thebusybiscuit.sensibletoolbox.items.itemroutermodules.VacuumModule;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;

import javax.annotation.ParametersAreNonnullByDefault;

public class Magnetoid extends BaseSTBItem {

    private static final int RADIUS = 6;

    @ParametersAreNonnullByDefault
    public Magnetoid() { super(); }

    public Magnetoid(ConfigurationSection conf) {
        super(conf);
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
        return new String[] { "Collects objects in a " + RADIUS, "block radius after breaking", "blocks, or damaging mobs", ChatColor.GOLD + "Offhand Gadget" };
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
