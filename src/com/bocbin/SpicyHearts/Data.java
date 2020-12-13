package com.bocbin.SpicyHearts;

import org.bukkit.Material;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Data implements Serializable {
    private static final long serialVersionUID = 5595735376170011801L;

    public HashMap<UUID, Set<Material>> playerFoodsEaten;
    boolean verySpicy;

    // for saving
    public Data(HashMap<UUID, Set<Material>> playerFoodsEaten, boolean verySpicy) {
        this.playerFoodsEaten = playerFoodsEaten;
        this.verySpicy = verySpicy;
    }

    // for loading
    public Data(Data loadedData) {
        this.playerFoodsEaten = loadedData.playerFoodsEaten;
        this.verySpicy = loadedData.verySpicy;
    }

    public boolean saveData(String filePath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            GZIPOutputStream gzOut = new GZIPOutputStream(fileOut);
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(gzOut);

            out.writeObject(this);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Data loadData(String filePath) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));

            Data data = (Data) in.readObject();
            in.close();
            return data;
        } catch (IOException | ClassNotFoundException e) {
            // e.printStackTrace();
            return null;
        }
    }
}
