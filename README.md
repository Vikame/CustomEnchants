# CustomEnchants

A library used for creating custom enchants. Built using spigot for 1.7.10 on Java 8.

## Custom Enchantment Creation

To create a custom enchantment, you can either instantiate an instance of the CustomEnchant class or have a class extend CustomEnchant.

A CustomEnchant instance requires a name, maximum level, and the enchantment target. You can specify aliases for a CustomEnchant when creating a CustomEnchant for usage in the /enchant command.

## Built-in Custom Enchants

There are 3 built-in custom enchants, however they are VERY rough and would need polishing on a server by server basis.

* Auto Smelt: Turns drops into their smelted form when a block is broken.
* Excavation: Mines a radius around the block broken. Will not break any blocks that the tool cannot normally break (e.g. a pickaxe cannot break wood) unless the item has the MultiTool custom enchant.
* MultiTool: Changes the tool type to match whatever block is being broken.