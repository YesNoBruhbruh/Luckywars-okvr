package com.luckywars.game.luckblock;

import com.luckywars.game.LuckyWars;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LuckyBlockManager {

    private final Map<Integer, LuckyBlock> luckyBlocks = new HashMap<>();

    private final Map<Rarity, List<LuckyBlockAction>> outcomes = new HashMap<>();

    private final Random random;

    public LuckyBlockManager(){

        random = ThreadLocalRandom.current();

        for(Rarity rarity : Rarity.values()){
            outcomes.put(rarity, new ArrayList<>());
        }

        try {
            int loaded = 0;
            String packageName = getClass().getPackage().getName();
            for(Rarity rarity : Rarity.values()){
                for(Class<?> clazz : new Reflections(packageName + ".actions." + rarity.name().toLowerCase()).getSubTypesOf(LuckyBlockAction.class)){
                    LuckyBlockAction action = (LuckyBlockAction) clazz.getDeclaredConstructor().newInstance();
                    outcomes.get(rarity).add(action);
                    loaded++;
                }
            }
            LuckyWars.getInstance().getLogger().info("Loaded " + loaded + " LuckyBlock actions!");
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public LuckyBlockAction getAction(){
        Rarity rarity = getRarity();
        if (outcomes.containsKey(rarity)) {
            List<LuckyBlockAction> actions = outcomes.get(rarity);
            int size = actions.size();
            if (size > 0) {
                int i = ThreadLocalRandom.current().nextInt(size);
                return actions.get(i);
            }
        }
        return outcomes.get(Rarity.COMMON).get(0);
//        Rarity rarity = getRarity();
//        if(outcomes.containsKey(rarity)){
//            int size = outcomes.get(rarity).size();
//            int i = ThreadLocalRandom.current().nextInt(0, size);
//            return outcomes.get(rarity).get(i);
//        }
//        return outcomes.get(Rarity.COMMON).get(0);
    }

    public Rarity getRarity(){
        double d = random.nextDouble();
        if(d > Rarity.LEGENDARY.getChance()){
            return Rarity.LEGENDARY;
        } else if(d > Rarity.EPIC.getChance()){
            return Rarity.EPIC;
        } else if(d > Rarity.RARE.getChance()){
            return Rarity.RARE;
        } else if(d > Rarity.UNCOMMON.getChance()){
            return Rarity.UNCOMMON;
        } else {
            return Rarity.COMMON;
        }
    }

    public Map<Integer, LuckyBlock> getLuckyBlocks() {
        return luckyBlocks;
    }

    public Map<Rarity, List<LuckyBlockAction>> getOutcomes() {
        return outcomes;
    }
}