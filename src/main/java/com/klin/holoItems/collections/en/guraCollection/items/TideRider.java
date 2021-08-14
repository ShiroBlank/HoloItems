package com.klin.holoItems.collections.en.guraCollection.items;

import com.klin.holoItems.HoloItems;
import com.klin.holoItems.Item;
import com.klin.holoItems.collections.en.guraCollection.GuraCollection;
import com.klin.holoItems.interfaces.Interactable;
import com.klin.holoItems.utility.Task;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class TideRider extends Item implements Interactable {
    public static final String name = "tideRider";

    private static final Material material = Material.TRIDENT;
    private static final int quantity = 1;
    private static final String lore =
            "§6Ability" +"/n"+
                "Surf the waves";
    private static final int durability = -1;
    private static final boolean stackable = false;
    private static final boolean shiny = false;

    public static final int cost = 3210;
    public static final char key = '0';
    public static final String id = ""+GuraCollection.key+key;

    public TideRider(){
        super(name, material, quantity, lore, durability, stackable, shiny, cost, id, key);
    }

    public void registerRecipes(){
        ShapedRecipe recipe =
                new ShapedRecipe(new NamespacedKey(HoloItems.getInstance(), name), item);
        recipe.shape("aba"," c "," a ");
        recipe.setIngredient('a', Material.WAXED_OXIDIZED_CUT_COPPER);
        recipe.setIngredient('b', Material.ENCHANTED_GOLDEN_APPLE);
        recipe.setIngredient('c', Material.SEA_LANTERN);
        recipe.setGroup(name);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void ability(PlayerInteractEvent event, Action action){
        if(action!=Action.RIGHT_CLICK_BLOCK && action!=Action.RIGHT_CLICK_AIR)
            return;
        Player player = event.getPlayer();
        Map<Block, BlockData> data = new HashMap<>();
        Location loc = player.getLocation();
        World world = player.getWorld();
        Block block = world.getBlockAt(loc);
        data.put(block, block.getBlockData());
        BlockData water = Bukkit.createBlockData(Material.WATER);
        player.sendBlockChange(loc, water);

        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        new Task(HoloItems.getInstance(), 0, 1){
            int increment = 0;
            Block current = block;
            Block previous;
            public void run(){
                if(increment>=1200 || !player.isValid() || !player.isHandRaised()){
                    player.sendBlockChange(current.getLocation(), data.remove(current));
                    meta.removeEnchant(Enchantment.RIPTIDE);
                    item.setItemMeta(meta);
                    cancel();
                    return;
                }
                else if(increment==0){
                    meta.addEnchant(Enchantment.RIPTIDE, 3, false);
                    item.setItemMeta(meta);
                }
                increment++;
                previous = current;
                Location loc = player.getLocation();
                current = world.getBlockAt(loc);
                Vector dir = loc.getDirection().setY(0).normalize();
                Block rise = world.getBlockAt(loc.clone().add(dir));
                Block fall = world.getBlockAt(loc.add(0, -1, 0));
                player.setVelocity(player.getVelocity().add(dir).normalize().setY(
                        rise.isPassable()&&!rise.isLiquid()?(fall.isPassable()&&!fall.isLiquid()?-1:0):1));
                if(previous.equals(current))
                    return;
                player.sendBlockChange(previous.getLocation(), data.remove(previous));
                data.put(current, current.getBlockData());
                player.sendBlockChange(current.getLocation(), water);
            }
        };
    }
}