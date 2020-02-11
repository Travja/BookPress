package me.Travja.BookPress;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class dropListener implements Listener {
    public Main plugin;

    public dropListener(Main m) {
        this.plugin = m;
        pressBottom = Material.valueOf(plugin.config.getString("pressBottom"));
        loadPresses();
    }

    private Material pressBottom;

    private ArrayList<Press> presses = new ArrayList<>();

    public Press getPressAtLoc(Location loc) {
        loc = loc.getBlock().getLocation();
        for (Press press : presses) {
            if (press.isInPress(loc))
                return press;
        }
        return null;
    }

    public void savePresses() {
        ArrayList<String> pressList = new ArrayList<>();
        for (Press press : presses) {
            Location loc = press.getLocation();
            pressList.add(String.join(",", loc.getWorld().getName(),
                    String.valueOf(loc.getX()),
                    String.valueOf(loc.getY()),
                    String.valueOf(loc.getZ())));
        }
        plugin.config.set("bookPresses", pressList);
        plugin.saveConfig();
    }

    public void loadPresses() {
        for (String str : plugin.config.getStringList("bookPresses")) {
            String[] split = str.split(",");
            Location loc = new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
            presses.add(new Press(loc));
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        final Player p = event.getPlayer();
        if (p.hasPermission("bookpress.use")) {
            final ItemStack item = event.getItemDrop().getItemStack();
            if (item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.MAP || item.getType() == Material.FILLED_MAP) {
                new BukkitRunnable() {
                    public void run() {
                        Item itemDrop = event.getItemDrop();

                        Press press = getPressAtLoc(itemDrop.getLocation());
                        if (press == null)
                            return;

                        press.addItem(itemDrop);
                        press.press();

                    }
                }.runTaskLater(plugin, 40L);
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        Item it = event.getItem();
        Press press = getPressAtLoc(it.getLocation());
        if (press == null)
            return;

        press.removeItem(it);

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();

        if (getPressAtLoc(b.getLocation()) != null)
            return;

        if (isPress(b)) {
            if (p.hasPermission("bookpress.create")) {
                BlockFace dir = b.getType() == pressBottom ? BlockFace.UP : BlockFace.DOWN;
                Block mid = b.getRelative(dir);
                presses.add(new Press(mid));
                savePresses();
                p.sendMessage(ChatColor.AQUA + "You have made a book press!");
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permission to make a book press!");
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();
        if (isPress(b)) {
            Press press = getPressAtLoc(b.getLocation());
            if (press == null)
                return;

            if (p.hasPermission("bookpress.break")) {
                presses.remove(press);
                savePresses();
                p.sendMessage(ChatColor.AQUA + "You have removed a book press!");
            } else {
                event.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You don't have permission to break that!");
            }
        }
    }


    private boolean isPress(Block b) {
        return (b.getType() == pressBottom && b.getRelative(BlockFace.UP).getType() == Material.AIR && b.getRelative(0, 2, 0).getType() == Material.PISTON) ||
                (b.getType() == Material.PISTON && b.getRelative(BlockFace.DOWN).getType() == Material.AIR && b.getRelative(0, -2, 0).getType() == pressBottom);
    }
}
