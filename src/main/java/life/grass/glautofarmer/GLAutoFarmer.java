package life.grass.glautofarmer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GLAutoFarmer extends JavaPlugin implements Listener {
    static Map<Material, Material> crops;
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        crops = new HashMap<>();
        crops.put(Material.CROPS, Material.SEEDS);
        crops.put(Material.POTATO, Material.POTATO_ITEM);
        crops.put(Material.BEETROOT_BLOCK, Material.BEETROOT_SEEDS);
        crops.put(Material.CARROT, Material.CARROT_ITEM);
        
    }
    
    @EventHandler
    public void onBreaking(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if(player == null) return;
        final Block cropBlock = e.getBlock();
        final Material crop = cropBlock.getType();
        if(crops.containsKey(crop)) {
            Material seed = crops.get(crop);
            ItemStack leftHand = player.getInventory().getItemInOffHand();
            if(leftHand.getType().equals(seed)) {
                if(leftHand.getAmount() == 1) {
                    leftHand.setType(Material.AIR);
                    player.getInventory().setItemInOffHand(leftHand);
                } else {
                   leftHand.setAmount(leftHand.getAmount() - 1); 
                }
                final Location loc = cropBlock.getLocation();
                Bukkit.getServer().getScheduler()
                        .scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getServer().getWorld(loc.getWorld().getName())
                                .getBlockAt(loc)
                                .setType(crop);
                    }
                }, 5);
            }
        }
    }
}
