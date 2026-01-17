package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import java.util.ArrayList;

public class NodeEditor extends Window {
    
    private static final int WIDTH = 200;
    private static final int HEIGHT = 150;
    
    public NodeCanvas canvas;
    public ArrayList<Node> nodes = new ArrayList<>();
    
    public String editorMode = "story";
    private String currentTool = "move";
    
    private RenderedTextBlock statusLabel;
    private RedButton addButton;
    private RedButton saveButton;
    private RedButton loadButton;
    private RedButton runButton;
    private RedButton modeButton;
    
    public NodeEditor() {
        super(0, 0, Chrome.get(Chrome.Type.WINDOW));
        resize(WIDTH, HEIGHT);
    }
    
    public void show() {
        setupUI();
        PixelScene.scene.addToFront(this);
    }
    
    private void setupUI() {
        canvas = new NodeCanvas();
        add(canvas);
        
        statusLabel = PixelScene.renderTextBlock("MODE: STORY | TOOL: MOVE", 6);
        statusLabel.setPos(5, 5);
        add(statusLabel);
        
        addButton = new RedButton("ADD") {
            @Override
            protected void onClick() {
                showAddNodeMenu();
            }
        };
        addButton.setRect(5, HEIGHT - 45, 35, 18);
        add(addButton);
        
        saveButton = new RedButton("SAVE") {
            @Override
            protected void onClick() {
                saveStory();
            }
        };
        saveButton.setRect(45, HEIGHT - 45, 35, 18);
        add(saveButton);
        
        loadButton = new RedButton("LOAD") {
            @Override
            protected void onClick() {
                loadStory();
            }
        };
        loadButton.setRect(85, HEIGHT - 45, 35, 18);
        add(loadButton);
        
        runButton = new RedButton("RUN") {
            @Override
            protected void onClick() {
                executeStory();
            }
        };
        runButton.setRect(125, HEIGHT - 45, 35, 18);
        add(runButton);
        
        modeButton = new RedButton("MODE") {
            @Override
            protected void onClick() {
                switchMode();
            }
        };
        modeButton.setRect(165, HEIGHT - 45, 35, 18);
        add(modeButton);
        
        RedButton toolButton = new RedButton("TOOL") {
            @Override
            protected void onClick() {
                showToolMenu();
            }
        };
        toolButton.setRect(5, HEIGHT - 25, 35, 18);
        add(toolButton);
        
        RedButton clearButton = new RedButton("CLEAR") {
            @Override
            protected void onClick() {
                canvas.clear();
                nodes.clear();
            }
        };
        clearButton.setRect(45, HEIGHT - 25, 35, 18);
        add(clearButton);
        
        RedButton zoomInButton = new RedButton("Z+") {
            @Override
            protected void onClick() {
                canvas.zoomIn();
            }
        };
        zoomInButton.setRect(85, HEIGHT - 25, 35, 18);
        add(zoomInButton);
        
        RedButton zoomOutButton = new RedButton("Z-") {
            @Override
            protected void onClick() {
                canvas.zoomOut();
            }
        };
        zoomOutButton.setRect(125, HEIGHT - 25, 35, 18);
        add(zoomOutButton);
    }
    
    private void showAddNodeMenu() {
        if (editorMode.equals("story")) {
            PixelScene.scene.addToFront(new WndOptions(
                "Add Node",
                "Select node type",
                "NPC",
                "Dialogue",
                "Quest",
                "Ending",
                "Logic",
                "Action"
            ) {
                @Override
                protected void onSelect(int index) {
                    switch(index) {
                        case 0: showNPCNodes(); break;
                        case 1: showDialogueNodes(); break;
                        case 2: showQuestNodes(); break;
                        case 3: showEndingNodes(); break;
                        case 4: showLogicNodes(); break;
                        case 5: showActionNodes(); break;
                    }
                }
            });
        }
    }
    
    private void showNPCNodes() {
        PixelScene.scene.addToFront(new WndOptions(
            "NPC Nodes",
            "Create or edit NPCs",
            "Create NPC",
            "Edit Existing NPC"
        ) {
            @Override
            protected void onSelect(int index) {
                Node node = new Node();
                node.type = "npc";
                node.label = index == 0 ? "Create NPC" : "Edit Existing NPC";
                node.x = 100;
                node.y = 100;
                node.setupInputs();
                canvas.addNode(node);
                nodes.add(node);
            }
        });
    }
    
    private void showDialogueNodes() {
        PixelScene.scene.addToFront(new WndOptions(
            "Dialogue Nodes",
            "Create dialogue trees",
            "Add Dialogue",
            "Player Choice",
            "Condition Check"
        ) {
            @Override
            protected void onSelect(int index) {
                Node node = new Node();
                node.type = "dialogue";
                node.label = index == 0 ? "Add Dialogue" : index == 1 ? "Player Choice" : "Condition Check";
                node.x = 100;
                node.y = 100;
                node.setupInputs();
                canvas.addNode(node);
                nodes.add(node);
            }
        });
    }
    
    private void showQuestNodes() {
        PixelScene.scene.addToFront(new WndOptions(
            "Quest Nodes",
            "Create quest system",
            "Create Quest",
            "Set Objective",
            "Set Reward"
        ) {
            @Override
            protected void onSelect(int index) {
                Node node = new Node();
                node.type = "quest";
                node.label = index == 0 ? "Create Quest" : index == 1 ? "Set Objective" : "Set Reward";
                node.x = 100;
                node.y = 100;
                node.setupInputs();
                canvas.addNode(node);
                nodes.add(node);
            }
        });
    }
    
    private void showEndingNodes() {
        PixelScene.scene.addToFront(new WndOptions(
            "Ending Nodes",
            "Create multiple endings",
            "Create Ending",
            "Set Condition",
            "Ending Text"
        ) {
            @Override
            protected void onSelect(int index) {
                Node node = new Node();
                node.type = "ending";
                node.label = index == 0 ? "Create Ending" : index == 1 ? "Set Condition" : "Ending Text";
                node.x = 100;
                node.y = 100;
                node.setupInputs();
                canvas.addNode(node);
                nodes.add(node);
            }
        });
    }
    
    private void showLogicNodes() {
        PixelScene.scene.addToFront(new WndOptions(
            "Logic Nodes",
            "Conditional logic",
            "If Has Item",
            "If Quest Done",
            "If Floor Level",
            "Random Chance"
        ) {
            @Override
            protected void onSelect(int index) {
                Node node = new Node();
                node.type = "logic";
                node.label = index == 0 ? "If Has Item" : index == 1 ? "If Quest Done" : index == 2 ? "If Floor Level" : "Random Chance";
                node.x = 100;
                node.y = 100;
                node.setupInputs();
                canvas.addNode(node);
                nodes.add(node);
            }
        });
    }
    
    private void showActionNodes() {
        PixelScene.scene.addToFront(new WndOptions(
            "Action Nodes",
            "Execute actions",
            "Give Item",
            "Spawn Enemy",
            "Teleport Player",
            "Show Message",
            "Unlock Ending"
        ) {
            @Override
            protected void onSelect(int index) {
                Node node = new Node();
                node.type = "action";
                String[] labels = {"Give Item", "Spawn Enemy", "Teleport Player", "Show Message", "Unlock Ending"};
                node.label = labels[index];
                node.x = 100;
                node.y = 100;
                node.setupInputs();
                canvas.addNode(node);
                nodes.add(node);
            }
        });
    }
    
    private void showToolMenu() {
        PixelScene.scene.addToFront(new WndOptions(
            "Select Tool",
            "Current: " + currentTool.toUpperCase(),
            "Move",
            "Edit",
            "Connect",
            "Delete"
        ) {
            @Override
            protected void onSelect(int index) {
                String[] tools = {"move", "edit", "connect", "delete"};
                currentTool = tools[index];
                updateStatusLabel();
            }
        });
    }
    
    private void switchMode() {
        editorMode = editorMode.equals("story") ? "game" : "story";
        updateStatusLabel();
    }
    
    public void updateStatusLabel() {
        statusLabel.text("MODE: " + editorMode.toUpperCase() + " | TOOL: " + currentTool.toUpperCase());
    }
    
    private void saveStory() {
        PixelScene.scene.addToFront(new WndTextInput("Save Story", "Enter filename", "mystory", 20, false, "Save", "Cancel") {
            @Override
            public void onSelect(boolean positive, String text) {
                if (positive && text.length() > 0) {
                    String json = createJSON();
                    try {
                        FileUtils.bundleToFile("studio-stories/" + text + ".json", json);
                        Game.platform.log("STUDIO", "Saved story: " + text);
                    } catch (Exception e) {
                        Game.platform.log("STUDIO", "Save failed: " + e.getMessage());
                    }
                }
            }
        });
    }
    
    private void loadStory() {
    }
    
    private void executeStory() {
        for (Node node : nodes) {
            if (node.type.equals("npc") && node.label.equals("Create NPC")) {
                executeNPCChain(node);
            }
        }
    }
    
    private void executeNPCChain(Node startNode) {
    }
    
    private String createJSON() {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            json.append("{");
            json.append("\"id\":\"").append(node.id).append("\",");
            json.append("\"type\":\"").append(node.type).append("\",");
            json.append("\"label\":\"").append(node.label).append("\",");
            json.append("\"x\":").append(node.x).append(",");
            json.append("\"y\":").append(node.y).append(",");
            json.append("\"value\":\"").append(node.value).append("\"");
            json.append("}");
            if (i < nodes.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
}