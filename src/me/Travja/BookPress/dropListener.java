package me.Travja.BookPress;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Entity;
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
import java.util.HashMap;
import java.util.List;

public class dropListener implements Listener {
    public Main plugin;

    public dropListener(Main m) {
        this.plugin = m;
    }

    private HashMap<Block, String> contents = new HashMap<>();
    private HashMap<Block, ArrayList<Item>> books = new HashMap<>();
    private HashMap<Block, ArrayList<Item>> wb = new HashMap<>();

    private Material pressBottom = Material.valueOf(plugin.config.getString("pressBottom"));

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) { //TODO We've got to optimize this.
        final Player p = event.getPlayer();
        if (p.hasPermission("BookPress.use")) {
            final ItemStack item = event.getItemDrop().getItemStack();
            if (item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK || item.getType() == Material.MAP || item.getType() == Material.FILLED_MAP) {
                new BukkitRunnable() {
                    public void run() {
                        for (String presses : plugin.config.getStringList("bookPresses")) {
                            Block l = event.getItemDrop().getLocation().getBlock();
                            if (presses.contains(l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ())) {
                                if (!wb.containsKey(l)) wb.put(l, new ArrayList<Item>());
                                if (!books.containsKey(l)) books.put(l, new ArrayList<Item>());
                                if (contents.containsKey(l)) {
                                    if (item.getType() == Material.WRITTEN_BOOK) {
                                        if (!contents.get(l).contains("wb"))
                                            contents.put(l, contents.get(l) + ",wb");
                                        boolean similar = false;
                                        for (Item i : wb.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            wb.get(l).add(event.getItemDrop());
                                    } else if (item.getType() == Material.WRITABLE_BOOK) {
                                        if (!contents.get(l).contains("bq"))
                                            contents.put(l, contents.get(l) + ",bq");
                                        boolean similar = false;
                                        for (Item i : books.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            books.get(l).add(event.getItemDrop());
                                    } else if (item.getType() == Material.MAP) {
                                        if (!contents.get(l).contains("em"))
                                            contents.put(l, contents.get(l) + ",em");
                                        boolean similar = false;
                                        for (Item i : books.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            books.get(l).add(event.getItemDrop());
                                    } else if (item.getType() == Material.FILLED_MAP) {
                                        if (!contents.get(l).contains("mp"))
                                            contents.put(l, contents.get(l) + ",mp");
                                        boolean similar = false;
                                        for (Item i : wb.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            wb.get(l).add(event.getItemDrop());
                                    }
                                } else {
                                    if (item.getType() == Material.WRITTEN_BOOK) {
                                        contents.put(l, "wb");
                                        boolean similar = false;
                                        for (Item i : wb.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            wb.get(l).add(event.getItemDrop());
                                    } else if (item.getType() == Material.WRITABLE_BOOK) {
                                        contents.put(l, "bq");
                                        boolean similar = false;
                                        for (Item i : books.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            books.get(l).add(event.getItemDrop());
                                    } else if (item.getType() == Material.MAP) {
                                        contents.put(l, "em");
                                        boolean similar = false;
                                        for (Item i : books.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            books.get(l).add(event.getItemDrop());
                                    } else if (item.getType() == Material.FILLED_MAP) {
                                        contents.put(l, "mp");
                                        boolean similar = false;
                                        for (Item i : wb.get(l)) {
                                            if (i.isValid() && !i.isDead() && i.getItemStack().isSimilar(event.getItemDrop().getItemStack()))
                                                similar = true;
                                        }
                                        if (!similar)
                                            wb.get(l).add(event.getItemDrop());
                                    }
                                }
                                String[] contains = contents.get(l).split(",");
                                boolean one = false;
                                boolean two = false;
                                for (int i = 0; i < contains.length; i++) {
                                    if (contains[i].equalsIgnoreCase("wb")) one = true;
                                    if (contains[i].equalsIgnoreCase("bq")) two = true;
                                    if (contains[i].equalsIgnoreCase("mp")) one = true;
                                    if (contains[i].equalsIgnoreCase("em")) two = true;
                                }
                                if (contains.length >= 2) {
                                    if (one && two) {
                                        Block b = l.getRelative(BlockFace.UP);
                                        if (b.getType() == Material.PISTON) {
                                            Piston piston = (Piston) b.getState().getData();
                                            if (!piston.isExtended()) {
                                                piston.setExtended(true);
                                                b.setBlockData(piston);
                                                //l.setType(Material.PISTON_HEAD);
                                                //l.getState().update();
                                                b.getState().update();
                                                ArrayList<Item> bookq = books.get(l);
                                                ArrayList<Item> wbs = wb.get(l);
                                                for (Item it : wbs) {
                                                    if (!it.isValid() || it.isDead()) {
                                                        wb.get(l).remove(it);
                                                        break;
                                                    }
                                                    if (it.getItemStack().getType() == Material.WRITTEN_BOOK) {
                                                        for (Item bk : bookq) {
                                                            if (!bk.isValid() || bk.isDead()) {
                                                                books.get(l).remove(bk);
                                                                break;
                                                            }
                                                            if (bk.getItemStack().getType() == Material.WRITABLE_BOOK) {
                                                                it.getItemStack().setAmount(it.getItemStack().getAmount() + bk.getItemStack().getAmount());
                                                                books.get(l).remove(bk);
                                                                contents.put(l, contents.get(l).replaceAll(",wb", ""));
                                                                contents.put(l, contents.get(l).replaceAll("wb,", ""));
                                                                contents.put(l, contents.get(l).replaceAll("wb", ""));
                                                                contents.put(l, contents.get(l).replaceAll(",bq", ""));
                                                                contents.put(l, contents.get(l).replaceAll("bq,", ""));
                                                                contents.put(l, contents.get(l).replaceAll("bq", ""));
                                                                contents.put(l, contents.get(l).length() > 1 ? contents.get(l) + ",wb" : "wb");
                                                                bk.remove();
                                                            }
                                                        }
                                                    }
                                                    if (it.getItemStack().getType() == Material.FILLED_MAP) {
                                                        for (Item bk : bookq) {
                                                            if (!bk.isValid() || bk.isDead()) {
                                                                books.get(l).remove(bk);
                                                                break;
                                                            }
                                                            if (bk.getItemStack().getType() == Material.MAP) {
                                                                it.getItemStack().setAmount(it.getItemStack().getAmount() + bk.getItemStack().getAmount());
                                                                books.get(l).remove(bk);
                                                                contents.put(l, contents.get(l).replaceAll(",mp", ""));
                                                                contents.put(l, contents.get(l).replaceAll("mp,", ""));
                                                                contents.put(l, contents.get(l).replaceAll("mp", ""));
                                                                contents.put(l, contents.get(l).replaceAll(",em", ""));
                                                                contents.put(l, contents.get(l).replaceAll("em,", ""));
                                                                contents.put(l, contents.get(l).replaceAll("em", ""));
                                                                contents.put(l, contents.get(l).length() > 1 ? contents.get(l) + ",mp" : "mp");
                                                                bk.remove();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }.runTaskLater(plugin, 40L);
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        Entity e = event.getItem();
        ItemStack item = event.getItem().getItemStack();
        for (String presses : plugin.config.getStringList("bookPresses")) {
            Block l = e.getLocation().getBlock();
            if (presses.contains(l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ())) {
                if (item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK) {
                    if (item.getType() == Material.WRITABLE_BOOK) {
                        books.remove(e);
                        if (!contents.isEmpty()) {
                            if (contents.containsKey(l)) {
                                String[] contains = contents.get(l).split(",");
                                if (contains.length > 1) {
                                    if (contents.get(l).contains(",bq"))
                                        contents.put(l, contents.get(l).replaceAll(",bq", ""));
                                    if (contents.get(l).contains("bq,"))
                                        contents.put(l, contents.get(l).replaceAll("bq,", ""));
                                } else if (contains.length == 1) {
                                    contents.remove(l);
                                }
                            }
                        }
                    }
                    if (item.getType() == Material.WRITTEN_BOOK) {
                        wb.remove(l);
                        if (!contents.isEmpty()) {
                            if (contents.containsKey(l)) {
                                String[] contains = contents.get(l).split(",");
                                if (contains.length > 1) {
                                    if (contents.get(l).contains(",wb"))
                                        contents.put(l, contents.get(l).replaceAll(",wb", ""));
                                    if (contents.get(l).contains("wb,"))
                                        contents.put(l, contents.get(l).replaceAll("wb,", ""));
                                } else if (contains.length == 1) {
                                    contents.remove(l);
                                }
                            }
                        }
                    }
                }
                if (item.getType() == Material.MAP || item.getType() == Material.FILLED_MAP) {
                    if (item.getType() == Material.FILLED_MAP) {
                        books.remove(l);
                        if (!contents.isEmpty()) {
                            if (contents.containsKey(l)) {
                                String[] contains = contents.get(l).split(",");
                                if (contains.length > 1) {
                                    if (contents.get(l).contains(",mp"))
                                        contents.put(l, contents.get(l).replaceAll(",mp", ""));
                                    if (contents.get(l).contains("mp,"))
                                        contents.put(l, contents.get(l).replaceAll("mp,", ""));
                                } else if (contains.length == 1) {
                                    contents.remove(l);
                                }
                            }
                        }
                    }
                    if (item.getType() == Material.MAP) {
                        wb.remove(l);
                        if (!contents.isEmpty()) {
                            if (contents.containsKey(l)) {
                                String[] contains = contents.get(l).split(",");
                                if (contains.length > 1) {
                                    if (contents.get(l).contains(",em"))
                                        contents.put(l, contents.get(l).replaceAll(",em", ""));
                                    if (contents.get(l).contains("em,"))
                                        contents.put(l, contents.get(l).replaceAll("em,", ""));
                                } else if (contains.length == 1) {
                                    contents.remove(l);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();
        if (isPress(b)) {
            if (p.hasPermission("BookPress.Create")) {
                List<String> presses = plugin.config.getStringList("bookPresses");
                if (b.getType() == pressBottom) {
                    if (!presses.contains(p.getWorld().getName() + "," + b.getRelative(BlockFace.UP).getX() + "," + b.getRelative(BlockFace.UP).getY() + "," + b.getRelative(BlockFace.UP).getZ())) {
                        presses.add(p.getWorld().getName() + "," + b.getRelative(BlockFace.UP).getX() + "," + b.getRelative(BlockFace.UP).getY() + "," + b.getRelative(BlockFace.UP).getZ());
                        plugin.config.set("bookPresses", presses);
                        plugin.saveConfig();
                        p.sendMessage(ChatColor.AQUA + "You have made a book press!");
                    }
                } else {
                    if (!plugin.config.getStringList("bookPresses").contains(p.getWorld().getName() + "," + b.getRelative(BlockFace.UP).getX() + "," + b.getRelative(BlockFace.UP).getY() + "," + b.getRelative(BlockFace.UP).getZ())) {
                        presses.add(p.getWorld().getName() + "," + b.getRelative(BlockFace.DOWN).getX() + "," + b.getRelative(BlockFace.DOWN).getY() + "," + b.getRelative(BlockFace.DOWN).getZ());
                        plugin.config.set("bookPresses", presses);
                        plugin.saveConfig();
                        p.sendMessage(ChatColor.AQUA + "You have made a book press!");
                    }
                }
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
            List<String> presses = plugin.config.getStringList("bookPresses");
            if (presses.contains(p.getWorld().getName() + "," + b.getRelative(BlockFace.UP).getX() + "," + b.getRelative(BlockFace.UP).getY() + "," + b.getRelative(BlockFace.UP).getZ())) {
                if (p.hasPermission("BookPress.Break")) {
                    removePress(b, BlockFace.UP, presses);
                    p.sendMessage(ChatColor.AQUA + "You have removed a book press!");
                } else {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You don't have permission to break that!");
                }
            } else if (presses.contains(p.getWorld().getName() + "," + b.getRelative(BlockFace.DOWN).getX() + "," + b.getRelative(BlockFace.DOWN).getY() + "," + b.getRelative(BlockFace.DOWN).getZ())) {
                if (p.hasPermission("BookPress.Break")) {
                    removePress(b, BlockFace.DOWN, presses);
                    p.sendMessage(ChatColor.AQUA + "You have removed a book press!");
                } else {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You don't have permission to break that!");
                }
            }
        }
    }

    private boolean isPress(Block b) {
        return (b.getType() == pressBottom && b.getRelative(BlockFace.UP).getType() == Material.AIR && b.getRelative(0, 2, 0).getType() == Material.PISTON) ||
                (b.getType() == Material.PISTON && b.getRelative(BlockFace.DOWN).getType() == Material.AIR && b.getRelative(0, -2, 0).getType() == pressBottom);
    }

    private void removePress(Block b, BlockFace dir, List<String> presses) {
        presses.remove(b.getWorld().getName() + "," + b.getRelative(dir).getX() + "," + b.getRelative(dir).getY() + "," + b.getRelative(dir).getZ());
        plugin.config.set("bookPresses", presses);
        plugin.saveConfig();
    }
}
