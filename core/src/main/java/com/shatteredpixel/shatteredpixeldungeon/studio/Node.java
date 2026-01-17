package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.UUID;

public class Node {
    public String id;
    public String type;
    public String label;
    public float x, y;
    public String value = "";
    public Color color = Color.GRAY;
    public ArrayList<Node> connections = new ArrayList<>();
    public ArrayList<NodeInput> inputs = new ArrayList<>();

    public Node() {
        this.id = UUID.randomUUID().toString();
    }

    public void setupInputs() {
        inputs.clear();

        switch(type) {
            case "npc":
                if(label.equals("Create NPC")) {
                    inputs.add(new NodeInput("NPC Name", "Merchant"));
                    inputs.add(new NodeInput("Spawn Type", "Fixed"));
                    inputs.add(new NodeInput("Floor Level", "5"));
                    inputs.add(new NodeInput("Spawn Chance", "100"));
                } else if(label.equals("Edit Existing NPC")) {
                    inputs.add(new NodeInput("NPC Type", "shopkeeper"));
                    inputs.add(new NodeInput("New Dialogue", "Hello!"));
                }
                break;

            case "dialogue":
                if(label.equals("Add Dialogue")) {
                    inputs.add(new NodeInput("Text", "Hello traveler!"));
                    inputs.add(new NodeInput("Speaker", "NPC"));
                } else if(label.equals("Player Choice")) {
                    inputs.add(new NodeInput("Choice 1", "Yes"));
                    inputs.add(new NodeInput("Choice 2", "No"));
                    inputs.add(new NodeInput("Choice 3", "Maybe"));
                } else if(label.equals("Condition Check")) {
                    inputs.add(new NodeInput("Condition", "Has Item"));
                    inputs.add(new NodeInput("Item Name", "gold"));
                    inputs.add(new NodeInput("Amount", "100"));
                }
                break;

            case "quest":
                if(label.equals("Create Quest")) {
                    inputs.add(new NodeInput("Quest Name", "Find the Amulet"));
                    inputs.add(new NodeInput("Objective Type", "Find Item"));
                    inputs.add(new NodeInput("Target", "Amulet"));
                    inputs.add(new NodeInput("Reward Gold", "500"));
                } else if(label.equals("Set Objective")) {
                    inputs.add(new NodeInput("Type", "Kill Enemy"));
                    inputs.add(new NodeInput("Target", "Rat"));
                    inputs.add(new NodeInput("Count", "10"));
                } else if(label.equals("Set Reward")) {
                    inputs.add(new NodeInput("Reward Type", "Gold"));
                    inputs.add(new NodeInput("Amount", "100"));
                }
                break;

            case "ending":
                if(label.equals("Create Ending")) {
                    inputs.add(new NodeInput("Ending Name", "True Hero"));
                    inputs.add(new NodeInput("Description", "You saved the world!"));
                    inputs.add(new NodeInput("Unlockable", "true"));
                } else if(label.equals("Set Condition")) {
                    inputs.add(new NodeInput("Condition", "Quests Complete"));
                    inputs.add(new NodeInput("Count", "5"));
                } else if(label.equals("Ending Text")) {
                    inputs.add(new NodeInput("Title", "Victory!"));
                    inputs.add(new NodeInput("Message", "Congratulations hero!"));
                }
                break;

            case "logic":
                if(label.equals("If Has Item")) {
                    inputs.add(new NodeInput("Item Name", "gold"));
                    inputs.add(new NodeInput("Amount", "100"));
                } else if(label.equals("If Quest Done")) {
                    inputs.add(new NodeInput("Quest Name", "Main Quest"));
                } else if(label.equals("If Floor Level")) {
                    inputs.add(new NodeInput("Floor", "10"));
                    inputs.add(new NodeInput("Comparison", ">="));
                } else if(label.equals("Random Chance")) {
                    inputs.add(new NodeInput("Chance %", "50"));
                }
                break;

            case "action":
                if(label.equals("Give Item")) {
                    inputs.add(new NodeInput("Item Name", "potion"));
                    inputs.add(new NodeInput("Quantity", "1"));
                } else if(label.equals("Spawn Enemy")) {
                    inputs.add(new NodeInput("Enemy Type", "rat"));
                    inputs.add(new NodeInput("Amount", "3"));
                } else if(label.equals("Teleport Player")) {
                    inputs.add(new NodeInput("Floor", "5"));
                } else if(label.equals("Show Message")) {
                    inputs.add(new NodeInput("Message", "Something happened!"));
                } else if(label.equals("Unlock Ending")) {
                    inputs.add(new NodeInput("Ending Name", "Secret Ending"));
                }
                break;
        }
    }

    public static class NodeInput {
        public String name;
        public String value;

        public NodeInput(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}