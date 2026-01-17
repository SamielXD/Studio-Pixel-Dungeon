package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class StudioMod {
    
    public static NodeEditor nodeEditor;
    public static NPCManager npcManager;
    public static DialogueSystem dialogueSystem;
    public static QuestManager questManager;
    public static EndingManager endingManager;
    
    private static IconButton floatingButton;
    private static float buttonX = 200;
    private static float buttonY = 100;
    
    public static void init() {
        Game.platform.log("STUDIO", "Initializing Story & NPC System...");
        
        nodeEditor = new NodeEditor();
        npcManager = new NPCManager();
        dialogueSystem = new DialogueSystem();
        questManager = new QuestManager();
        endingManager = new EndingManager();
        
        createFloatingButton();
        
        Game.platform.log("STUDIO", "Studio loaded successfully!");
    }
    
    private static void createFloatingButton() {
        if (GameScene.scene != null) {
            floatingButton = new IconButton(Icons.get(Icons.CLOSE)) {
                @Override
                protected void onClick() {
                    showStudioMenu();
                }
                
                @Override
                protected void onPointerDown() {
                    super.onPointerDown();
                }
                
                @Override
                protected void onPointerUp() {
                    super.onPointerUp();
                }
            };
            
            floatingButton.setPos(buttonX, buttonY);
            floatingButton.setSize(20, 20);
            
            GameScene.scene.add(floatingButton);
        }
    }
    
    private static void showStudioMenu() {
        GameScene.show(new WndOptions(
            "STUDIO",
            "Story & NPC System",
            "Story Editor",
            "NPC Manager",
            "Quest Manager",
            "Ending Manager",
            "Load Example"
        ) {
            @Override
            protected void onSelect(int index) {
                switch(index) {
                    case 0:
                        nodeEditor.show();
                        break;
                    case 1:
                        showNPCManager();
                        break;
                    case 2:
                        showQuestManager();
                        break;
                    case 3:
                        showEndingManager();
                        break;
                    case 4:
                        loadExampleStory();
                        break;
                }
            }
        });
    }
    
    private static void showNPCManager() {
        GameScene.show(new WndOptions(
            "NPC Manager",
            "Manage NPCs in dungeon",
            "View All NPCs",
            "Edit Shopkeeper",
            "Edit Wandmaker",
            "Edit Imp",
            "Create New NPC"
        ) {
            @Override
            protected void onSelect(int index) {
                switch(index) {
                    case 0:
                        npcManager.showAllNPCs();
                        break;
                    case 1:
                        npcManager.editNPC("shopkeeper");
                        break;
                    case 2:
                        npcManager.editNPC("wandmaker");
                        break;
                    case 3:
                        npcManager.editNPC("imp");
                        break;
                    case 4:
                        npcManager.createNewNPC();
                        break;
                }
            }
        });
    }
    
    private static void showQuestManager() {
        GameScene.show(new WndOptions(
            "Quest Manager",
            "Active quests: " + questManager.getActiveQuestCount(),
            "View Quests",
            "Create Quest",
            "Complete Quest",
            "Back"
        ) {
            @Override
            protected void onSelect(int index) {
                switch(index) {
                    case 0:
                        questManager.showQuests();
                        break;
                    case 1:
                        questManager.createQuest();
                        break;
                    case 2:
                        questManager.completeQuest();
                        break;
                }
            }
        });
    }
    
    private static void showEndingManager() {
        GameScene.show(new WndOptions(
            "Ending Manager",
            "Unlocked: " + endingManager.getUnlockedCount() + "/10",
            "View Endings",
            "Create Ending",
            "Unlock Ending",
            "Trigger Ending"
        ) {
            @Override
            protected void onSelect(int index) {
                switch(index) {
                    case 0:
                        endingManager.showEndings();
                        break;
                    case 1:
                        endingManager.createEnding();
                        break;
                    case 2:
                        endingManager.unlockEnding();
                        break;
                    case 3:
                        endingManager.triggerEnding();
                        break;
                }
            }
        });
    }
    
    private static void loadExampleStory() {
        nodeEditor.nodes.clear();
        
        Node npcNode = new Node();
        npcNode.type = "npc";
        npcNode.label = "Create NPC";
        npcNode.x = 100;
        npcNode.y = 300;
        npcNode.value = "Mysterious Merchant";
        npcNode.setupInputs();
        npcNode.inputs.get(0).value = "Mysterious Merchant";
        npcNode.inputs.get(1).value = "Fixed";
        npcNode.inputs.get(2).value = "10";
        
        Node dialogueNode = new Node();
        dialogueNode.type = "dialogue";
        dialogueNode.label = "Add Dialogue";
        dialogueNode.x = 400;
        dialogueNode.y = 300;
        dialogueNode.value = "Greetings traveler...";
        dialogueNode.setupInputs();
        dialogueNode.inputs.get(0).value = "Greetings traveler...";
        
        Node choiceNode = new Node();
        choiceNode.type = "dialogue";
        choiceNode.label = "Player Choice";
        choiceNode.x = 700;
        choiceNode.y = 250;
        choiceNode.setupInputs();
        choiceNode.inputs.get(0).value = "Who are you?";
        choiceNode.inputs.get(1).value = "What do you sell?";
        
        Node questNode = new Node();
        questNode.type = "quest";
        questNode.label = "Create Quest";
        questNode.x = 700;
        questNode.y = 400;
        questNode.value = "Find the Ancient Artifact";
        questNode.setupInputs();
        questNode.inputs.get(0).value = "Find the Ancient Artifact";
        questNode.inputs.get(1).value = "Find Item";
        questNode.inputs.get(2).value = "Ancient Artifact";
        
        Node endingNode = new Node();
        endingNode.type = "ending";
        endingNode.label = "Create Ending";
        endingNode.x = 1000;
        endingNode.y = 400;
        endingNode.value = "Merchant's Gratitude";
        endingNode.setupInputs();
        endingNode.inputs.get(0).value = "Merchant's Gratitude";
        endingNode.inputs.get(1).value = "You helped the merchant and gained his eternal gratitude.";
        
        npcNode.connections.add(dialogueNode);
        dialogueNode.connections.add(choiceNode);
        choiceNode.connections.add(questNode);
        questNode.connections.add(endingNode);
        
        nodeEditor.nodes.add(npcNode);
        nodeEditor.nodes.add(dialogueNode);
        nodeEditor.nodes.add(choiceNode);
        nodeEditor.nodes.add(questNode);
        nodeEditor.nodes.add(endingNode);
        
        nodeEditor.show();
        
        Game.platform.log("STUDIO", "Loaded example story!");
    }
    
    public static void onLevelLoad() {
        if (npcManager != null) {
            npcManager.spawnNPCs(Dungeon.depth);
        }
        
        if (questManager != null) {
            questManager.checkFloorQuests(Dungeon.depth);
        }
    }
    
    public static void onGameEnd() {
        if (endingManager != null) {
            endingManager.checkEndings();
        }
    }
    
    public static void save(Bundle bundle) {
        if (npcManager != null) npcManager.save(bundle);
        if (questManager != null) questManager.save(bundle);
        if (endingManager != null) endingManager.save(bundle);
    }
    
    public static void restore(Bundle bundle) {
        if (npcManager != null) npcManager.restore(bundle);
        if (questManager != null) questManager.restore(bundle);
        if (endingManager != null) endingManager.restore(bundle);
    }
}