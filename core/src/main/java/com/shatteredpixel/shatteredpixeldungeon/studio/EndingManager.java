package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class EndingManager {
    
    private ArrayList<Ending> allEndings = new ArrayList<>();
    private ArrayList<String> unlockedEndings = new ArrayList<>();
    private HashMap<String, EndingCondition> endingConditions = new HashMap<>();
    
    public EndingManager() {
        createDefaultEndings();
    }
    
    private void createDefaultEndings() {
        createEnding("True Hero", "You defeated the final boss and saved the world!", "Beat Boss", "Yog-Dzewa", 1);
        createEnding("Pacifist", "You completed the game without killing anyone", "No Kills", "", 0);
        createEnding("Speed Runner", "You completed the game in record time", "Time", "30", 1800);
        createEnding("Collector", "You collected every item in the game", "Items Collected", "All", 100);
        createEnding("Quest Master", "You completed all quests", "Quests", "All", 10);
        createEnding("Secret Path", "You found the hidden passage", "Found Secret", "Hidden Door", 1);
        createEnding("Merchant's Friend", "You helped the mysterious merchant", "Quest Complete", "Merchant Quest", 1);
        createEnding("Dungeon Explorer", "You explored every floor", "Floors Visited", "All", 25);
        createEnding("Survivor", "You survived 100 floors", "Reach Floor", "100", 1);
        createEnding("Lucky Hero", "You won with only 1 HP remaining", "Low HP Victory", "1", 1);
    }
    
    public void createEnding(String name, String description, String conditionType, String target, int value) {
        Ending ending = new Ending();
        ending.name = name;
        ending.description = description;
        ending.isUnlocked = false;
        
        EndingCondition condition = new EndingCondition();
        condition.type = conditionType;
        condition.target = target;
        condition.value = value;
        
        allEndings.add(ending);
        endingConditions.put(name, condition);
        
        Game.platform.log("STUDIO", "Created ending: " + name);
    }
    
    public void setCondition(String endingName, String conditionType, int value) {
        EndingCondition condition = endingConditions.get(endingName);
        if (condition == null) {
            condition = new EndingCondition();
            endingConditions.put(endingName, condition);
        }
        condition.type = conditionType;
        condition.value = value;
        
        Game.platform.log("STUDIO", "Set condition for " + endingName + ": " + conditionType + " = " + value);
    }
    
    public void unlockEnding(String endingName) {
        for (Ending ending : allEndings) {
            if (ending.name.equals(endingName) && !ending.isUnlocked) {
                ending.isUnlocked = true;
                unlockedEndings.add(endingName);
                
                GameScene.show(new WndOptions(
                    "ENDING UNLOCKED!",
                    "[GOLD]" + ending.name + "[]\n\n" + ending.description,
                    "Amazing!"
                ) {
                    @Override
                    protected void onSelect(int index) {
                    }
                });
                
                Game.platform.log("STUDIO", "Unlocked ending: " + endingName);
                return;
            }
        }
    }
    
    public void checkEndings() {
        for (Ending ending : allEndings) {
            if (!ending.isUnlocked) {
                EndingCondition condition = endingConditions.get(ending.name);
                if (condition != null && checkCondition(condition)) {
                    unlockEnding(ending.name);
                }
            }
        }
    }
    
    private boolean checkCondition(EndingCondition condition) {
        switch (condition.type) {
            case "Quests":
                if (StudioMod.questManager != null) {
                    return StudioMod.questManager.getCompletedQuestCount() >= condition.value;
                }
                break;
                
            case "Reach Floor":
                return Dungeon.depth >= condition.value;
                
            case "Items Collected":
                return Dungeon.hero.belongings.backpack.size() >= condition.value;
                
            case "Quest Complete":
                if (StudioMod.questManager != null) {
                    return StudioMod.questManager.isQuestComplete(condition.target);
                }
                break;
                
            case "Beat Boss":
                return false;
                
            case "No Kills":
                return false;
                
            case "Time":
                return false;
        }
        return false;
    }
    
    public void triggerEnding(String endingName) {
        for (Ending ending : allEndings) {
            if (ending.name.equals(endingName)) {
                showEndingCutscene(ending);
                return;
            }
        }
    }
    
    private void showEndingCutscene(Ending ending) {
        GameScene.show(new WndOptions(
            "[ROYAL]THE END[]",
            "[GOLD]" + ending.name + "[]\n\n" + ending.description + "\n\n" +
            "Endings Unlocked: " + unlockedEndings.size() + "/" + allEndings.size(),
            "Continue",
            "View Endings"
        ) {
            @Override
            protected void onSelect(int index) {
                if (index == 1) {
                    showEndings();
                }
            }
        });
    }
    
    public void showEndings() {
        StringBuilder endingList = new StringBuilder();
        endingList.append("UNLOCKED: ").append(unlockedEndings.size()).append("/").append(allEndings.size()).append("\n\n");
        
        for (Ending ending : allEndings) {
            if (ending.isUnlocked) {
                endingList.append("[GOLD]✓ ").append(ending.name).append("[]\n");
                endingList.append("  ").append(ending.description).append("\n\n");
            } else {
                endingList.append("[GRAY]✗ ").append(ending.name).append("[]\n");
                endingList.append("  [GRAY]???[]\n\n");
            }
        }
        
        GameScene.show(new WndOptions(
            "ALL ENDINGS",
            endingList.toString(),
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
    
    public void createEnding() {
        String newName = "Custom Ending " + (allEndings.size() + 1);
        createEnding(newName, "A custom ending you created", "Quest Complete", "Custom Quest", 1);
        
        GameScene.show(new WndOptions(
            "Ending Created",
            "Created: " + newName,
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
    
    public void unlockEnding() {
        if (allEndings.size() > 0) {
            for (Ending ending : allEndings) {
                if (!ending.isUnlocked) {
                    unlockEnding(ending.name);
                    return;
                }
            }
            
            GameScene.show(new WndOptions(
                "All Unlocked",
                "All endings are already unlocked!",
                "OK"
            ) {
                @Override
                protected void onSelect(int index) {
                }
            });
        }
    }
    
    public void triggerEnding() {
        if (unlockedEndings.size() > 0) {
            String endingName = unlockedEndings.get(0);
            for (Ending ending : allEndings) {
                if (ending.name.equals(endingName)) {
                    triggerEnding(endingName);
                    return;
                }
            }
        } else {
            GameScene.show(new WndOptions(
                "No Endings",
                "No endings unlocked yet!",
                "OK"
            ) {
                @Override
                protected void onSelect(int index) {
                }
            });
        }
    }
    
    public int getUnlockedCount() {
        return unlockedEndings.size();
    }
    
    public int getTotalCount() {
        return allEndings.size();
    }
    
    public void save(Bundle bundle) {
        bundle.put("ending_count", allEndings.size());
        for (int i = 0; i < allEndings.size(); i++) {
            Ending ending = allEndings.get(i);
            bundle.put("ending_" + i + "_name", ending.name);
            bundle.put("ending_" + i + "_desc", ending.description);
            bundle.put("ending_" + i + "_unlocked", ending.isUnlocked);
        }
        
        bundle.put("unlocked_count", unlockedEndings.size());
        for (int i = 0; i < unlockedEndings.size(); i++) {
            bundle.put("unlocked_" + i, unlockedEndings.get(i));
        }
    }
    
    public void restore(Bundle bundle) {
        int count = bundle.getInt("ending_count");
        allEndings.clear();
        for (int i = 0; i < count; i++) {
            Ending ending = new Ending();
            ending.name = bundle.getString("ending_" + i + "_name");
            ending.description = bundle.getString("ending_" + i + "_desc");
            ending.isUnlocked = bundle.getBoolean("ending_" + i + "_unlocked");
            allEndings.add(ending);
        }
        
        int unlockedCount = bundle.getInt("unlocked_count");
        unlockedEndings.clear();
        for (int i = 0; i < unlockedCount; i++) {
            unlockedEndings.add(bundle.getString("unlocked_" + i));
        }
    }
    
    public static class Ending {
        public String name;
        public String description;
        public boolean isUnlocked;
    }
    
    public static class EndingCondition {
        public String type;
        public String target;
        public int value;
    }
}