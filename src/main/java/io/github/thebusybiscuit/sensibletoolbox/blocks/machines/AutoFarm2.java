package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;


import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.items.GoldCombineHoe;
import io.github.thebusybiscuit.sensibletoolbox.items.components.MachineFrame;

public class AutoFarm2 extends AutoFarm {

    private static final Map<Material, Material> crops = new EnumMap<>(Material.class);
    private static final int RADIUS = 5;

    static {
        crops.put(Material.COCOA, Material.COCOA_BEANS);
        crops.put(Material.SWEET_BERRY_BUSH, Material.SWEET_BERRIES);
        crops.put(Material.SUGAR_CANE, Material.SUGAR_CANE);
        crops.put(Material.CACTUS, Material.CACTUS);
    }

    private Set<Block> blocks;
    private Material buffer;

    public AutoFarm2() {
        blocks = new HashSet<>();
    }

    public AutoFarm2(ConfigurationSection conf) {
        super(conf);
        blocks = new HashSet<>();
    }

    @Override
    public String getItemName() {
        return "Auto Farm MkII";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Automatically harvests and replants", "Cocoa Beans/Sugar Cane/Cactus/Sweet Berries", "in a " + RADIUS + "x" + RADIUS + " Radius 2 Blocks above the Machine" };
    }

    @Override
    public Recipe getMainRecipe() {
        MachineFrame frame = new MachineFrame();
        GoldCombineHoe hoe = new GoldCombineHoe();
        registerCustomIngredients(frame, hoe);
        ShapedRecipe res = new ShapedRecipe(getKey(), toItemStack());
        res.shape("LHL", "IFI", "RGR");
        res.setIngredient('R', Material.REDSTONE);
        res.setIngredient('G', Material.GOLD_INGOT);
        res.setIngredient('I', Material.IRON_INGOT);
        res.setIngredient('L', new MaterialChoice(Tag.LOGS));
        res.setIngredient('H', hoe.getMaterial());
        res.setIngredient('F', frame.getMaterial());
        return res;
    }

    @Override
    public void onBlockRegistered(Location location, boolean isPlacing) {
        int i = RADIUS;
        Block block = location.getBlock();

        for (int x = -i; x <= i; x++) {
            for (int z = -i; z <= i; z++) {
                blocks.add(block.getRelative(x, 2, z));
            }
        }
        //Without this, the machine stops updating the charge after a restart.
        super.onBlockRegistered(location, isPlacing);
    }

    @Override
    public void onServerTick() {
        if (!isJammed()) {
            for (Block crop : blocks) {
                if (crops.containsKey(crop.getType())) {
                    Ageable ageable = (Ageable) crop.getBlockData();

                    //First let's look for the ageable plants
                    if (ageable.getAge() >= ageable.getMaximumAge()) {
                        if (getCharge() >= getScuPerCycle()) {
                            setCharge(getCharge() - getScuPerCycle());
                        } else {
                            break;
                        }

                        BlockData data = crop.getBlockData();
                        Ageable cropData = (Ageable) data;
                        cropData.setAge(0);

                        crop.setBlockData(data);
                        crop.getWorld().playEffect(crop.getLocation(), Effect.STEP_SOUND, crop.getType());
                        setJammed(!output(crops.get(crop.getType())));
                        break;
                    }
                    Block block = crop.getRelative(BlockFace.UP);

                    //Next, lets look for crops that do not age (cactus, sugarcane)
                    if (crops.containsKey(block.getType()) && block.getType() != Material.COCOA && block.getType() != Material.SWEET_BERRY_BUSH) {
                        if (getCharge() >= getScuPerCycle()) {
                            setCharge(getCharge() - getScuPerCycle());
                        } else {
                            break;
                        }

                        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                        setJammed(!output(crops.get(block.getType())));
                        block.setType(Material.AIR);
                        break;
                    }

                }
            }
        } else if (buffer != null) {
            setJammed(!output(buffer));
        }
        super.onServerTick();
    }

    @Override
    public double getScuPerCycle() {
        return 30.0;
    }
}
