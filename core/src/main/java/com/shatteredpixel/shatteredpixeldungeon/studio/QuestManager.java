package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestManager {
    
    private ArrayList<Quest> activeQuests = new ArrayList<>();
    private ArrayList<Quest> completedQuests = new ArrayList<>();
    private HashMap<Integer, ArrayList<Quest>> floorQuests = new HashMap<>();
    
    public QuestManager() {
        createExampleQuests();
    }
    
    private void createExampleQuests() {
        Quest mainQuest = new Quest();
        mainQuest.name = "Find the Amulet";
        mainQuest.description = "Retrieve the Amulet of Yendor from the depths";
        mainQuest.objectiveType = "Reach Floor";
        mainQuest.targetName = "25";
        mainQuest.targetCount = 1;
        mainQuest.rewardGold = 1000;
        mainQuest.triggerFloor = 1;
        
        Quest ratQuest = new Quest();
        ratQuest.name = "Rat Extermination";
        ratQuest.description = "Kill 10 rats in the sewers";
        ratQuest.objectiveType = "Kill Enemy";
        ratQuest.targetName = "rat";
        ratQuest.targetCount = 10;
        ratQuest.rewardGold = 100;
        ratQuest.triggerFloor = 1;
    }
    
    public void createQuest(String name, String objectiveType, String target, int rewardGold) {
        Quest quest = new Quest();
        quest.name = name;
        quest.description = "Complete the objective: " + objectiveType + " " + target;
        quest.objectiveType = objectiveType;
        quest.targetName = target;
        quest.targetCount = 1;
        quest.rewardGold = rewardGold;
        quest.triggerFloor = Dungeon.depth;
        quest.isActive = true;
        
        activeQuests.add(quest);
        
        Game.platform.log("STUDIO", "Created quest: " + name);
    }
    
    public void setObjective(String questName, String objectiveType, String target, int count) {
        for (Quest quest : activeQuests) {
            if (quest.name.equals(questName)) {
                quest.objectiveType = objectiveType;
                quest.targetName = target;
                quest.targetCount = count;
                quest.currentCount = 0;
                
                Game.platform.log("STUDIO", "Set objective for " + questName + ": " + objectiveType + " " + target + " x" + count);
                return;
            }
        }
    }
    
    public void setReward(String questName, String rewardType, int amount) {
        for (Quest quest : activeQuests) {
            if (quest.name.equals(questName)) {
                quest.rewardType = rewardType;
                quest.rewardGold = amount;
                
                Game.platform.log("STUDIO", "Set reward for " + questName + ": " + amount + " " + rewardType);
                return;
            }
        }
    }
    
    public void updateQuestProgress(String objectiveType, String targetName) {
        for (Quest quest : activeQuests) {
            if (quest.objectiveType.equals(objectiveType) && quest.targetName.equals(targetName)) {
                quest.currentCount++;
                
                Game.platform.log("STUDIO", "Quest progress: " + quest.name + " (" + quest.currentCount + "/" + quest.targetCount + ")");
                
                if (quest.currentCount >= quest.targetCount) {
                    completeQuestInternal(quest);
                }
            }
        }
    }
    
    private void completeQuestInternal(Quest quest) {
        quest.isComplete = true;
        quest.isActive = false;
        
        giveReward(quest);
        
        activeQuests.remove(quest);
        completedQuests.add(quest);
        
        GameScene.show(new WndOptions(
            "QUEST COMPLETE!",
            quest.name + "\n\n" + quest.description + "\n\nReward: " + quest.rewardGold + " gold",
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
        
        Game.platform.log("STUDIO", "Quest completed: " + quest.name);
    }
    
    private void giveReward(Quest quest) {
        if (quest.rewardType.equals("Gold")) {
            Gold gold = new Gold(quest.rewardGold);
            if (gold.doPickUp(Dungeon.hero)) {
                Game.platform.log("STUDIO", "Gave reward: " + quest.rewardGold + " gold");
            }
        }
    }
    
    public void checkFloorQuests(int floor) {
        ArrayList<Quest> floorQuestList = floorQuests.get(floor);
        if (floorQuestList != null) {
            for (Quest quest : floorQuestList) {
                if (!quest.isActive && !quest.isComplete) {
                    quest.isActive = true;
                    activeQuests.add(quest);
                    
                    GameScene.show(new WndOptions(
                        "NEW QUEST!",
                        quest.name + "\n\n" + quest.description,
                        "Accept",
                        "Decline"
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 1) {
                                quest.isActive = false;
                                activeQuests.remove(quest);
                            }
                        }
                    });
                }
            }
        }
    }
    
    public void showQuests() {
        StringBuilder questList = new StringBuilder();
        questList.append("ACTIVE QUESTS: ").append(activeQuests.size()).append("\n\n");
        
        for (Quest quest : activeQuests) {
            questList.append("• ").append(quest.name).append("\n");
            questList.append("  ").append(quest.description).append("\n");
            questList.append("  Progress: ").append(quest.currentCount).append("/").append(quest.targetCount).append("\n\n");
        }
        
        questList.append("\nCOMPLETED: ").append(completedQuests.size()).append("\n");
        for (Quest quest : completedQuests) {
            questList.append("✓ ").append(quest.name).append("\n");
        }
        
        GameScene.show(new WndOptions(
            "QUEST LOG",
            questList.toString(),
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
    
    public void createQuest() {
        createQuest("New Quest", "Find Item", "potion", 50);
        
        GameScene.show(new WndOptions(
            "Quest Created",
            "New quest added to active quests",
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
    
    public void completeQuest() {
        if (activeQuests.size() > 0) {
            Quest quest = activeQuests.get(0);
            quest.currentCount = quest.targetCount;
            completeQuestInternal(quest);
        } else {
            GameScene.show(new WndOptions(
                "No Active Quests",
                "You have no active quests to complete",
                "OK"
            ) {
                @Override
                protected void onSelect(int index) {
                }
            });
        }
    }
    
    public int getActiveQuestCount() {
        return activeQuests.size();
    }
    
    public int getCompletedQuestCount() {
        return completedQuests.size();
    }
    
    public boolean isQuestComplete(String questName) {
        for (Quest quest : completedQuests) {
            if (quest.name.equals(questName)) {
                return true;
            }
        }
        return false;
    }
    
    public void save(Bundle bundle) {
        bundle.put("active_quest_count", activeQuests.size());
        for (int i = 0; i < activeQuests.size(); i++) {
            Quest quest = activeQuests.get(i);
            bundle.put("active_quest_" + i + "_name", quest.name);
            bundle.put("active_quest_" + i + "_desc", quest.description);
            bundle.put("active_quest_" + i + "_type", quest.objectiveType);
            bundle.put("active_quest_" + i + "_target", quest.targetName);
            bundle.put("active_quest_" + i + "_count", quest.targetCount);
            bundle.put("active_quest_" + i + "_current", quest.currentCount);
            bundle.put("active_quest_" + i + "_reward", quest.rewardGold);
        }
        
        bundle.put("completed_quest_count", completedQuests.size());
        for (int i = 0; i < completedQuests.size(); i++) {
            Quest quest = completedQuests.get(i);
            bundle.put("completed_quest_" + i + "_name", quest.name);
        }
    }
    
    public void restore(Bundle bundle) {
        int activeCount = bundle.getInt("active_quest_count");
        activeQuests.clear();
        for (int i = 0; i < activeCount; i++) {
            Quest quest = new Quest();
            quest.name = bundle.getString("active_quest_" + i + "_name");
            quest.description = bundle.getString("active_quest_" + i + "_desc");
            quest.objectiveType = bundle.getString("active_quest_" + i + "_type");
            quest.targetName = bundle.getString("active_quest_" + i + "_target");
            quest.targetCount = bundle.getInt("active_quest_" + i + "_count");
            quest.currentCount = bundle.getInt("active_quest_" + i + "_current");
            quest.rewardGold = bundle.getInt("active_quest_" + i + "_reward");
            quest.isActive = true;
            activeQuests.add(quest);
        }
        
        int completedCount = bundle.getInt("completed_quest_count");
        completedQuests.clear();
        for (int i = 0; i < completedCount; i++) {
            Quest quest = new Quest();
            quest.name = bundle.getString("completed_quest_" + i + "_name");
            quest.isComplete = true;
            completedQuests.add(quest);
        }
    }
    
    public static class Quest {
        public String name;
        public String description;
        public String objectiveType;
        public String targetName;
        public int targetCount;
        public int currentCount;
        public String rewardType = "Gold";
        public int rewardGold;
        public int triggerFloor;
        public boolean isActive;
        public boolean isComplete;
    }
}