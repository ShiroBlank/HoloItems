package com.klin.holoItems.collections.misc.opCollection;

import com.klin.holoItems.Collection;
import com.klin.holoItems.collections.misc.opCollection.items.*;
import org.bukkit.entity.Player;

import java.util.Map;

public class OpCollection extends Collection {
    public static final String name = null;
    public static final String desc = null;
    public static final String theme = null;
    public static final String ign = null;
    public static final String uuid = null;
    public static final String base64 = null;

    public static final char key = 'Z';

    public OpCollection(){
        super(name, desc, theme, ign, uuid, key, base64);
        collection.put(Sokoban.key, new Sokoban());
        collection.put(WheatPortal.key, new WheatPortal());
        collection.put(GalleryFrame.key, new GalleryFrame());
        collection.put(Payload.key, new Payload());
        collection.put(Falchion.key, new Falchion());
    }

    public Map<String, Integer> getStat(Player player){
        return null;
    }
}