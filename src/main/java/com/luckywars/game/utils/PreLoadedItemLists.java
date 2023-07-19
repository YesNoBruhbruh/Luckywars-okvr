package com.luckywars.game.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PreLoadedItemLists {

    public static List<List<ItemStack>> commonItems;
    public static List<List<ItemStack>> uncommonItems;
    public static List<List<ItemStack>> rareItems;
    public static List<List<ItemStack>> legendaryItems;

    public PreLoadedItemLists(){
        commonItems = new ArrayList<>();
        uncommonItems = new ArrayList<>();
        rareItems = new ArrayList<>();
        legendaryItems = new ArrayList<>();

        initCommon();
        initUncommon();
        initRare();
        initLegendary();
    }

    public void initCommon(){

        commonItems.add(Collections.singletonList(new ItemStack(Material.STONE)));

        commonItems.add(Collections.singletonList(new ItemStack(Material.COOKED_CHICKEN, 2)));
        commonItems.add(Arrays.asList(new ItemStack(Material.STICK, 2), new ItemStack(Material.COBBLESTONE, 3)));

    }

    public void initUncommon(){

        uncommonItems.add(Arrays.asList(new ItemStack(Material.APPLE)));
        uncommonItems.add(Arrays.asList(new ItemStack(Material.APPLE)));

        uncommonItems.add(Arrays.asList(new ItemStack(Material.BOW)));
        uncommonItems.add(Arrays.asList(new ItemStack(Material.ARROW, 15)));

    }

    public void initRare(){

        rareItems.add(Arrays.asList(new ItemStack(Material.CARROT_ITEM)));
        rareItems.add(Arrays.asList(new ItemStack(Material.CARROT_ITEM)));

        rareItems.add(Arrays.asList(new ItemStack(Material.DIAMOND_SWORD)));
        rareItems.add(Arrays.asList(new ItemStack(Material.GOLDEN_APPLE, 5)));
        rareItems.add(Arrays.asList(new ItemStack(Material.EMERALD_BLOCK, 40)));

    }

    public void initLegendary() {

        legendaryItems.add(Arrays.asList(new ItemStack(Material.DIAMOND_BLOCK, 64)));
    }

}
