package com.machinespray.ROYAL;

import com.machinespray.ROYAL.items.ItemRing;
import com.machinespray.ROYAL.items.ItemScroll;
import com.machinespray.ROYAL.items.NetHackItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class RoyalItems {
    public static ArrayList<ItemScroll> scrolls = new ArrayList<ItemScroll>();
    public static NetHackItem base;
    public static ArrayList<ItemRing> rings = new ArrayList<ItemRing>();
    public static NetHackItem AmuletUndying;

    public static void initItems() {
        ItemScroll.initNames();
        ItemRing.initNames();
        base = new NetHackItem("base");
        //AmuletUndying = new NetHackItem("amuletOfUndying");
    }

    public static void registerItems() {
        for (ItemScroll i : scrolls)
            i.register();
        for (ItemRing i : rings)
            i.register();
        base.register();
        //AmuletUndying.register();

    }

    @SideOnly(Side.CLIENT)
    public static void registerClient() {
        base.registerClient();
        for (ItemScroll i : scrolls)
            i.registerClient();
        for (ItemRing i : rings)
            i.registerClient();
        //AmuletUndying.registerClient();
    }
}