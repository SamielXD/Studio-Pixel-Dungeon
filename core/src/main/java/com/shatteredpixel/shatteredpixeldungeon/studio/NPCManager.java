package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class NPCManager {
    
    private ArrayList<CustomNPC> customNPCs = new ArrayList<>();
    private HashMap<String, String> npcDialogues = new HashMap<>();
    private HashMap<String, Integer> npcSpawnFloors = new HashMap<>();
    private HashMap<String, Integer> npcSpawnChances = new HashMap<>();
    
    public NPCManager() {
        initializeDefaultNPCs();
    }
    
    private void initializeDefaultNPCs() {
        npcDialogues.put("shopkeeper", "Welcome to my shop!");
        npcDialogues.put("wandmaker", "I need a specific item...");
        npcDialogues.put("imp", "Help me and I'll reward you!");
        
        npcSpawnFloors.put("shopkeeper", 6);
        npcSpawnFloors.put("wandmaker", 6);
        npcSpawnFloors.put("imp", 19);
        
        npcSpawnChances.put("shopkeeper", 100);
        npcSpawnChances.put("wandmaker", 100);
        npcSpawnChances.put("imp", 100);
    }
    
    public void createNPC(String name, String dialogue, int floor, int spawnChance, boolean isFixed) {
        CustomNPC npc = new CustomNPC();
        npc.name = name;
        npc.dialogue = dialogue;
        npc.spawnFloor = floor;
        npc.spawnChance = spawnChance;
        npc.isFixedSpawn = isFixed;
        
        customNPCs.add(npc);
        
        Game.platform.log("STUDIO", "Created NPC: " + name + " on floor " + floor);
    }
    
    public void editNPC(String npcType) {
        String currentDialogue = npcDialogues.getOrDefault(npcType, "Hello!");
        
        GameScene.show(new WndOptions(
            "Edit " + npcType,
            "Current dialogue: " + currentDialogue,
            "Change Dialogue",
            "Change Spawn Floor",
            "Change Spawn Chance",
            "Back"
        ) {
            @Override
            protected void onSelect(int index) {
                switch(index) {
                    case 0:
                        changeDialogue(npcType);
                        break;
                    case 1:
                        changeSpawnFloor(npcType);
                        break;
                    case 2:
                        changeSpawnChance(npcType);
                        break;
                }
            }
        });
    }
    
    private void changeDialogue(String npcType) {
        npcDialogues.put(npcType, "New dialogue for " + npcType);
        Game.platform.log("STUDIO", "Changed dialogue for " + npcType);
    }
    
    private void changeSpawnFloor(String npcType) {
        int newFloor = Random.Int(1, 25);
        npcSpawnFloors.put(npcType, newFloor);
        Game.platform.log("STUDIO", "Changed spawn floor for " + npcType + " to " + newFloor);
    }
    
    private void changeSpawnChance(String npcType) {
        int newChance = Random.Int(10, 100);
        npcSpawnChances.put(npcType, newChance);
        Game.platform.log("STUDIO", "Changed spawn chance for " + npcType + " to " + newChance + "%");
    }
    
    public void spawnNPCs(int floor) {
        for (CustomNPC npc : customNPCs) {
            if (npc.isFixedSpawn && npc.spawnFloor == floor) {
                if (Random.Int(100) < npc.spawnChance) {
                    spawnCustomNPC(npc);
                }
            } else if (!npc.isFixedSpawn) {
                if (Random.Int(100) < npc.spawnChance) {
                    spawnCustomNPC(npc);
                }
            }
        }
    }
    
    private void spawnCustomNPC(CustomNPC npc) {
        Game.platform.log("STUDIO", "Spawning custom NPC: " + npc.name + " on floor " + Dungeon.depth);
    }
    
    public void showAllNPCs() {
        StringBuilder npcList = new StringBuilder();
        npcList.append("Default NPCs:\n");
        npcList.append("- Shopkeeper (Floor ").append(npcSpawnFloors.get("shopkeeper")).append(")\n");
        npcList.append("- Wandmaker (Floor ").append(npcSpawnFloors.get("wandmaker")).append(")\n");
        npcList.append("- Imp (Floor ").append(npcSpawnFloors.get("imp")).append(")\n\n");
        
        npcList.append("Custom NPCs: ").append(customNPCs.size()).append("\n");
        for (CustomNPC npc : customNPCs) {
            npcList.append("- ").append(npc.name).append(" (Floor ").append(npc.spawnFloor).append(")\n");
        }
        
        GameScene.show(new WndOptions(
            "All NPCs",
            npcList.toString(),
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
    
    public void createNewNPC() {
        String defaultName = "Custom NPC " + (customNPCs.size() + 1);
        createNPC(defaultName, "Hello adventurer!", 10, 50, true);
        
        GameScene.show(new WndOptions(
            "NPC Created",
            "Created: " + defaultName,
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
    
    public String getDialogue(String npcType) {
        return npcDialogues.getOrDefault(npcType, "...");
    }
    
    public void save(Bundle bundle) {
        bundle.put("custom_npc_count", customNPCs.size());
        for (int i = 0; i < customNPCs.size(); i++) {
            CustomNPC npc = customNPCs.get(i);
            bundle.put("npc_" + i + "_name", npc.name);
            bundle.put("npc_" + i + "_dialogue", npc.dialogue);
            bundle.put("npc_" + i + "_floor", npc.spawnFloor);
            bundle.put("npc_" + i + "_chance", npc.spawnChance);
            bundle.put("npc_" + i + "_fixed", npc.isFixedSpawn);
        }
    }
    
    public void restore(Bundle bundle) {
        int count = bundle.getInt("custom_npc_count");
        customNPCs.clear();
        for (int i = 0; i < count; i++) {
            CustomNPC npc = new CustomNPC();
            npc.name = bundle.getString("npc_" + i + "_name");
            npc.dialogue = bundle.getString("npc_" + i + "_dialogue");
            npc.spawnFloor = bundle.getInt("npc_" + i + "_floor");
            npc.spawnChance = bundle.getInt("npc_" + i + "_chance");
            npc.isFixedSpawn = bundle.getBoolean("npc_" + i + "_fixed");
            customNPCs.add(npc);
        }
    }
    
    public static class CustomNPC {
        public String name;
        public String dialogue;
        public int spawnFloor;
        public int spawnChance;
        public boolean isFixedSpawn;
    }
}