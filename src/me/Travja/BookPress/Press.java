package me.Travja.BookPress;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;

public class Press {

    private Location loc;
    private ArrayList<Block> blocks = new ArrayList<>();
    private Block pBlock;
    private Piston piston;

    private HashSet<Item> itemsInPress = new HashSet<>();

    public Press(Location loc) {

        this.loc = loc;
        blocks.add(loc.getBlock());
        this.pBlock = loc.getBlock().getRelative(BlockFace.UP);
        this.piston = (Piston) this.pBlock.getBlockData();
        blocks.add(this.pBlock);
        blocks.add(loc.getBlock().getRelative(BlockFace.DOWN));

        orientPiston();

    }

    public Press(Block bl) {

        this(bl.getLocation());

    }

    private void orientPiston() {
        this.piston.setFacing(BlockFace.DOWN);
        this.pBlock.setBlockData(this.piston);
        this.pBlock.getState().update();
    }

    public void press() {
        Item book = null,
                emptyB = null,
                map = null,
                emptyM = null;

        for (Item it : itemsInPress) {
            if (it.getItemStack().getType() == Material.WRITTEN_BOOK && book == null)
                book = it;
            else if (it.getItemStack().getType() == Material.WRITABLE_BOOK && emptyB == null)
                emptyB = it;
            else if (it.getItemStack().getType() == Material.FILLED_MAP && map == null)
                map = it;
            else if (it.getItemStack().getType() == Material.MAP && emptyM == null)
                emptyM = it;
        }

        boolean dupeBooks = book != null && emptyB != null;
        boolean dupeMaps = map != null && emptyM != null;
        if (dupeBooks || dupeMaps)
            pressAnimation();


        if (dupeBooks)
            dupeItems(book, emptyB);

        if (map != null && emptyM != null)
            dupeItems(map, emptyM);

    }

    private void dupeItems(Item main, Item other) {
        ItemStack mit = main.getItemStack();
        int more = other.getItemStack().getAmount();
        mit.setAmount(mit.getAmount() + more);
        main.setItemStack(mit);
        itemsInPress.remove(main);
        itemsInPress.remove(other);
        other.remove();
    }

    public void pressAnimation() {
        piston.setExtended(true);
        pBlock.setBlockData(piston);

        Block mid = pBlock.getRelative(BlockFace.DOWN);

        mid.setType(Material.PISTON_HEAD);
        mid.getState().update();
        pBlock.getState().update();
    }

    public Location getLocation() {
        return loc;
    }

    public boolean isInPress(Block bl) {
        for (Block b : blocks) {
            if (b.getLocation().equals(bl.getLocation()))
                return true;
        }
        return false;
    }

    public boolean isInPress(Location loc) {
        for (Block b : blocks) {
            if (b.getLocation().equals(loc))
                return true;
        }
        return false;
    }

    public void addItem(Item item) {
        itemsInPress.add(item);
    }

    public void removeItem(Item item) {
        itemsInPress.remove(item);
    }

}
