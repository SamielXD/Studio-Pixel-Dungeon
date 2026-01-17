package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogueSystem {
    
    private HashMap<String, DialogueTree> dialogueTrees = new HashMap<>();
    private ArrayList<DialogueChoice> activeChoices = new ArrayList<>();
    
    public DialogueSystem() {
        createExampleDialogues();
    }
    
    private void createExampleDialogues() {
        DialogueTree shopkeeperTree = new DialogueTree("shopkeeper");
        shopkeeperTree.addLine("Welcome to my shop! What can I do for you?");
        shopkeeperTree.addChoice("What do you sell?", "I have potions, scrolls, and weapons!");
        shopkeeperTree.addChoice("Nothing, thanks.", "Come back anytime!");
        dialogueTrees.put("shopkeeper", shopkeeperTree);
        
        DialogueTree wandmakerTree = new DialogueTree("wandmaker");
        wandmakerTree.addLine("I need a corpse dust or rotberry seed...");
        wandmakerTree.addChoice("I'll help you.", "Thank you, adventurer!");
        wandmakerTree.addChoice("Not interested.", "Very well...");
        dialogueTrees.put("wandmaker", wandmakerTree);
    }
    
    public void createDialogue(String npcName, String text, String speaker) {
        DialogueTree tree = dialogueTrees.get(npcName);
        if (tree == null) {
            tree = new DialogueTree(npcName);
            dialogueTrees.put(npcName, tree);
        }
        tree.addLine(text);
        
        Game.platform.log("STUDIO", "Created dialogue for " + npcName + ": " + text);
    }
    
    public void addChoice(String npcName, String choiceText, String responseText) {
        DialogueTree tree = dialogueTrees.get(npcName);
        if (tree == null) {
            tree = new DialogueTree(npcName);
            dialogueTrees.put(npcName, tree);
        }
        tree.addChoice(choiceText, responseText);
        
        Game.platform.log("STUDIO", "Added choice for " + npcName + ": " + choiceText);
    }
    
    public void showDialogue(String npcName) {
        DialogueTree tree = dialogueTrees.get(npcName);
        if (tree == null) {
            Game.platform.log("STUDIO", "No dialogue tree for " + npcName);
            return;
        }
        
        if (tree.choices.size() > 0) {
            String[] choiceTexts = new String[tree.choices.size()];
            for (int i = 0; i < tree.choices.size(); i++) {
                choiceTexts[i] = tree.choices.get(i).choiceText;
            }
            
            GameScene.show(new WndOptions(
                npcName.toUpperCase(),
                tree.currentLine,
                choiceTexts
            ) {
                @Override
                protected void onSelect(int index) {
                    if (index < tree.choices.size()) {
                        String response = tree.choices.get(index).responseText;
                        showResponse(npcName, response);
                    }
                }
            });
        } else {
            GameScene.show(new WndOptions(
                npcName.toUpperCase(),
                tree.currentLine,
                "OK"
            ) {
                @Override
                protected void onSelect(int index) {
                }
            });
        }
    }
    
    private void showResponse(String npcName, String response) {
        GameScene.show(new WndOptions(
            npcName.toUpperCase(),
            response,
            "OK"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
    
    public void checkCondition(String condition, String itemName, int amount) {
        Game.platform.log("STUDIO", "Checking condition: " + condition + " " + itemName + " x" + amount);
    }
    
    public void save(Bundle bundle) {
        bundle.put("dialogue_tree_count", dialogueTrees.size());
        int index = 0;
        for (String key : dialogueTrees.keySet()) {
            DialogueTree tree = dialogueTrees.get(key);
            bundle.put("tree_" + index + "_npc", key);
            bundle.put("tree_" + index + "_line", tree.currentLine);
            bundle.put("tree_" + index + "_choice_count", tree.choices.size());
            for (int i = 0; i < tree.choices.size(); i++) {
                bundle.put("tree_" + index + "_choice_" + i + "_text", tree.choices.get(i).choiceText);
                bundle.put("tree_" + index + "_choice_" + i + "_response", tree.choices.get(i).responseText);
            }
            index++;
        }
    }
    
    public void restore(Bundle bundle) {
        int treeCount = bundle.getInt("dialogue_tree_count");
        dialogueTrees.clear();
        for (int i = 0; i < treeCount; i++) {
            String npcName = bundle.getString("tree_" + i + "_npc");
            DialogueTree tree = new DialogueTree(npcName);
            tree.currentLine = bundle.getString("tree_" + i + "_line");
            int choiceCount = bundle.getInt("tree_" + i + "_choice_count");
            for (int j = 0; j < choiceCount; j++) {
                String choiceText = bundle.getString("tree_" + i + "_choice_" + j + "_text");
                String responseText = bundle.getString("tree_" + i + "_choice_" + j + "_response");
                tree.addChoice(choiceText, responseText);
            }
            dialogueTrees.put(npcName, tree);
        }
    }
    
    public static class DialogueTree {
        public String npcName;
        public String currentLine;
        public ArrayList<DialogueChoice> choices = new ArrayList<>();
        
        public DialogueTree(String npcName) {
            this.npcName = npcName;
            this.currentLine = "...";
        }
        
        public void addLine(String text) {
            this.currentLine = text;
        }
        
        public void addChoice(String choiceText, String responseText) {
            choices.add(new DialogueChoice(choiceText, responseText));
        }
    }
    
    public static class DialogueChoice {
        public String choiceText;
        public String responseText;
        
        public DialogueChoice(String choiceText, String responseText) {
            this.choiceText = choiceText;
            this.responseText = responseText;
        }
    }
}