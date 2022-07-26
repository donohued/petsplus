package com.softwaremongers.petsplus;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.softwaremongers.petsplus.utils.UpdateChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class Petsplus extends JavaPlugin implements Listener {

    static final int RESOURCE_ID = 123;
    static final String RESOURCE_NAME = "Pets+";
    private final Logger logger = this.getLogger();

    public static Map<String, ItemStack[]> menus = new HashMap<String, ItemStack[]>();

    @Override
    public void onEnable() {// Plugin startup logic
        // Check For Updates
        new UpdateChecker(this, RESOURCE_ID).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("New version of "+ RESOURCE_NAME +" is available! New version: " + version + ", Installed version: " + this.getDescription().getVersion());
            }
        });

        //do stuff
        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();

        if(this.getConfig().contains("data"))
            this.loadPetInv();
    }

    @Override
    public void onDisable() {// Plugin shutdown logic
        if(!menus.isEmpty())
            this.savePetInv();
    }

    public void savePetInv(){
        for (Map.Entry<String, ItemStack[]> entry : menus.entrySet()){
            this.getConfig().set("data." + entry.getKey(), entry.getValue());
        }

    }
    public void loadPetInv(){
        this.getConfig().getConfigurationSection("data").getKeys(false).forEach(key ->{
            @SuppressWarnings("unchecked")
            ItemStack[] content = ((List<ItemStack>) this.getConfig().get("data." + key)).toArray(new ItemStack[0]);
            menus.put(key, content);
        });
    }

    //do command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("pi")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return false;
            }
            //create player and inv for player
            Player player = (Player) sender;
            //TODO: save inv as name from arg, not players name
            Inventory inv = Bukkit.createInventory(player, 54, player.getName());

            //look for inv attached to player uuid in the hashmap an apply to inv
            //TODO: look for inv attached to player, with the name of the given arg.
            if(menus.containsKey((player.getUniqueId().toString())))
                inv.setContents((menus.get(player.getUniqueId().toString())));

            //open inv variable. Loaded from hash map if key exists. New inv otherwise.
            player.openInventory(inv);

            return true;
        }
        return false;
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent event){
        if(event.getView().getTitle().contains(event.getPlayer().getName()))
                menus.put(event.getPlayer().getUniqueId().toString(), event.getInventory().getContents());
    }
}
