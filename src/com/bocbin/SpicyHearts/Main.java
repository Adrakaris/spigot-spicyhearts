package com.bocbin.SpicyHearts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.*;

public class Main extends JavaPlugin implements Listener {

    static final String filePath = "spicyhearts_mem.data";

    // hashmap of players to their food eaten
    HashMap<UUID, Set<Material>> playerFoodsEaten;
    // setting
    boolean verySpicy;

    /*Runs on server start*/
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // load data stored
        Data savedInfo = Data.loadData(filePath);

        // generate new data or load the saved data
        if (savedInfo == null) {
            playerFoodsEaten = new HashMap<>();
            verySpicy = false;
        } else {
            playerFoodsEaten = savedInfo.playerFoodsEaten;
            verySpicy = savedInfo.verySpicy;
        }

    }

    /* Runs on server shutdown
     * Saves the hashmap to a file
     */
    @Override
    public void onDisable() {
        // create data obj and save
        Data toSave = new Data(playerFoodsEaten, verySpicy);
        boolean saved = toSave.saveData(filePath);
        if (saved) {
            System.out.println("SpicyHearts > Saved Successfully");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // to set very spicy mode on or off
        if (label.equalsIgnoreCase("veryspicy") && sender.hasPermission("spicyhearts.veryspicy")) {

            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /verySpicy <true/false>");
                return false;
            }

            if (args[0].equalsIgnoreCase("true")) {
                verySpicy = true;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Very spicy mode &4ENABLED"));
            } else {
                verySpicy = false;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Very spicy mode &2DISABLED"));
            }

            return true;
        } else if (label.equalsIgnoreCase("foodlist") && sender.isOp()) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Not a player");
                return false;
            }

            UUID playerKey = ((Player) sender).getUniqueId();

            String eaten = playerFoodsEaten.get(playerKey).toString();
            sender.sendMessage("Eaten: " + eaten);

            return true;
        } else if (label.equalsIgnoreCase("foodlist_all")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Not a player");
                return false;
            }

            for (Map.Entry<UUID, Set<Material>> entry : playerFoodsEaten.entrySet()) {
                String key = entry.getKey().toString().substring(0, 8) + "...";
                String eaten = entry.getValue().toString();

                sender.sendMessage(ChatColor.GOLD + key + ChatColor.RESET + ": " + eaten);
            }
            return true;
        }

        return false;
    }

    private void updatePlayerHealth(Player player) {
        UUID playerID = player.getUniqueId();
        // set the max health and health of a player, starting from 1 instead of 0
        double foodsEaten = (double) playerFoodsEaten.get(playerID).size() + 1;
        // this is set to the no. of foods eaten

        // setmaxhealth is disabled so have to replace
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(foodsEaten);

        player.setHealth(Math.min(foodsEaten, player.getHealth()));  // the players will not gain health w/ health increase

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();

        if (!playerFoodsEaten.containsKey(playerID)) {
            playerFoodsEaten.put(playerID, new HashSet<Material>());  // add player ID
        }

        // we should only set it if the player does not rejoin from a death, else it would break the death process, so to say.
        // other case we wait for a playerrespawnevent
        // getLogger().info("Player health update: current health " + player.getHealth());
        if (player.getHealth() > 0) updatePlayerHealth(player);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack consumed = event.getItem();

        if (consumed.getType().equals(Material.POTION) || consumed.getType().equals(Material.MILK_BUCKET)) {
            return;
        }

        // if not a potion nor milk
        playerFoodsEaten.get(player.getUniqueId()).add(consumed.getType());  // add food type to the set

        updatePlayerHealth(player);
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (verySpicy) {  // will reset their food count on death iff v.spicy enabled
            playerFoodsEaten.get(player.getUniqueId()).clear();  // clears their set of eaten items
        }

        updatePlayerHealth(player);
    }
}
