package me.threadsafe.customenchants.helper;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

public class Items {

    private static final ItemType PICKAXE = new ItemType(
            ICE, PACKED_ICE, ANVIL, REDSTONE_BLOCK, BREWING_STAND,
            CAULDRON, IRON_BARDING, IRON_DOOR_BLOCK, HOPPER, GOLD_PLATE,
            IRON_PLATE, IRON_BLOCK, LAPIS_BLOCK, DIAMOND_BLOCK, EMERALD_BLOCK,
            GOLD_BLOCK, ACTIVATOR_RAIL, DETECTOR_RAIL, POWERED_RAIL, RAILS,
            STONE, COAL_BLOCK, QUARTZ_BLOCK, BRICK, COAL_ORE, COBBLESTONE,
            COBBLE_WALL, DISPENSER, DROPPER, ENCHANTMENT_TABLE, ENDER_STONE,
            ENDER_CHEST, FURNACE, MOB_SPAWNER, MOSSY_COBBLESTONE, NETHER_BRICK,
            NETHER_FENCE, IRON_FENCE, QUARTZ_ORE, NETHERRACK, SANDSTONE, STEP,
            SMOOTH_STAIRS, SANDSTONE_STAIRS, BRICK_STAIRS, COBBLESTONE_STAIRS,
            QUARTZ_STAIRS, DOUBLE_STEP, SMOOTH_BRICK, STONE_BUTTON, STONE_PLATE,
            IRON_ORE, LAPIS_ORE, DIAMOND_ORE, EMERALD_ORE, GOLD_ORE, REDSTONE_ORE,
            OBSIDIAN
    );

    private static final ItemType AXE = new ItemType(
            COCOA, JACK_O_LANTERN, PUMPKIN, VINE,
            BOOKSHELF, CHEST, WORKBENCH, DAYLIGHT_DETECTOR,
            FENCE, FENCE_GATE, HUGE_MUSHROOM_1, HUGE_MUSHROOM_2,
            JUKEBOX, LADDER, NOTE_BLOCK, SIGN, TRAPPED_CHEST, LOG,
            LOG_2, WOOD_BUTTON, WOOD_DOOR, WOOD, WOOD_PLATE, WOOD_STEP,
            WOOD_DOUBLE_STEP, WOOD_STAIRS, BIRCH_WOOD_STAIRS, JUNGLE_WOOD_STAIRS,
            SPRUCE_WOOD_STAIRS, TRAP_DOOR
    );

    private static final ItemType SHOVEL = new ItemType(
            CLAY, DIRT, SOIL, GRASS, GRAVEL, MYCEL, SAND, SOUL_SAND, SNOW, SNOW_BLOCK
    );

    private static final ItemType SWORD = new ItemType(
            MELON, WEB
    );

    private static final ItemType[] values = { PICKAXE, AXE, SHOVEL, SWORD };

    public static ItemType getType(ItemStack item){
        return getType(item.getType());
    }

    public static ItemType getType(Material type){
        switch(type){
            case WOOD_PICKAXE:
                return PICKAXE;
            case STONE_PICKAXE:
                return PICKAXE;
            case IRON_PICKAXE:
                return PICKAXE;
            case GOLD_PICKAXE:
                return PICKAXE;
            case DIAMOND_PICKAXE:
                return PICKAXE;
            case WOOD_AXE:
                return AXE;
            case STONE_AXE:
                return AXE;
            case IRON_AXE:
                return AXE;
            case GOLD_AXE:
                return AXE;
            case DIAMOND_AXE:
                return AXE;
            case WOOD_SPADE:
                return SHOVEL;
            case STONE_SPADE:
                return SHOVEL;
            case IRON_SPADE:
                return SHOVEL;
            case GOLD_SPADE:
                return SHOVEL;
            case DIAMOND_SPADE:
                return SHOVEL;
        }

        return null;
    }

    // Fake enum methods, because fake enums are great!!1one!!
    public static ItemType[] values(){
        return values;
    }

    public static class ItemType {

        public final List<Material> materials;

        public ItemType(Material... materials){
            this.materials = Arrays.asList(materials);
        }

        public boolean canBreak(Block b){
            return canBreak(b.getType());
        }

        public boolean canBreak(Material material){
            return materials.contains(material);
        }

    }

    public enum MaterialType {

        DIAMOND(DIAMOND_SWORD, DIAMOND_PICKAXE, DIAMOND_AXE, DIAMOND_SPADE), IRON(IRON_SWORD, IRON_PICKAXE, IRON_AXE, IRON_SPADE),
        GOLD(GOLD_SWORD, GOLD_PICKAXE, GOLD_AXE, GOLD_SPADE), STONE(STONE_SWORD, STONE_PICKAXE, STONE_AXE, STONE_SPADE),
        WOOD(WOOD_SWORD, WOOD_PICKAXE, WOOD_AXE, WOOD_SPADE);

        private Material sword, pickaxe, axe, shovel;

        MaterialType(Material sword, Material pickaxe, Material axe, Material shovel) {
            this.sword = sword;
            this.pickaxe = pickaxe;
            this.axe = axe;
            this.shovel = shovel;
        }

        public Material getSword(){
            return sword;
        }

        public Material getPickaxe() {
            return pickaxe;
        }

        public Material getAxe() {
            return axe;
        }

        public Material getShovel() {
            return shovel;
        }

        public Material getMaterialCanBreak(Block b){
            Material mat = b.getType();
            for(ItemType type : Items.values()){
                if(type.canBreak(mat)){
                    if(type == Items.PICKAXE) return getPickaxe();
                    else if(type == Items.AXE) return getAxe();
                    else if(type == Items.SHOVEL) return getShovel();
                    else if(type == Items.SWORD) return getSword();
                }
            }

            return null;
        }

        public static MaterialType getType(Material material){
            for(MaterialType type : values()){
                if(type.getPickaxe() == material ||
                        type.getAxe() == material ||
                        type.getShovel() == material ||
                        type.getSword() == material) return type;
            }

            return null;
        }
    }

}
