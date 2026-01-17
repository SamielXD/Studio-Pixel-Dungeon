package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class NodeCanvas extends Visual {
    
    public ArrayList<Node> nodes = new ArrayList<>();
    
    private float offsetX = 0;
    private float offsetY = 0;
    private float zoom = 1.0f;
    
    private Node selectedNode = null;
    private Node dragNode = null;
    private PointF dragStart = new PointF();
    
    private static final float NODE_WIDTH = 120f;
    private static final float NODE_HEIGHT = 80f;
    private static final float LABEL_SIZE = 8f;
    private static final float INFO_SIZE = 6f;
    
    public NodeCanvas() {
        super(0, 0, Game.width, Game.height);
    }
    
    @Override
    public void draw() {
        super.draw();
        
        drawConnections();
        drawNodes();
    }
    
    private void drawConnections() {
        for (Node node : nodes) {
            for (Node target : node.connections) {
                float x1 = (node.x + NODE_WIDTH / 2 + offsetX) * zoom;
                float y1 = (node.y + NODE_HEIGHT / 2 + offsetY) * zoom;
                float x2 = (target.x + NODE_WIDTH / 2 + offsetX) * zoom;
                float y2 = (target.y + NODE_HEIGHT / 2 + offsetY) * zoom;
                
                drawLine(x1, y1, x2, y2, Color.WHITE);
            }
        }
    }
    
    private void drawNodes() {
        for (Node node : nodes) {
            float x = (node.x + offsetX) * zoom;
            float y = (node.y + offsetY) * zoom;
            float w = NODE_WIDTH * zoom;
            float h = NODE_HEIGHT * zoom;
            
            drawNodeBox(x, y, w, h, node.color);
            
            drawNodeLabel(node, x, y, w, h);
            
            drawNodeInfo(node, x, y, w, h);
            
            drawNodeType(node, x, y, w);
        }
    }
    
    private void drawNodeBox(float x, float y, float w, float h, Color color) {
    }
    
    private void drawNodeLabel(Node node, float x, float y, float w, float h) {
    }
    
    private void drawNodeInfo(Node node, float x, float y, float w, float h) {
    }
    
    private void drawNodeType(Node node, float x, float y, float w) {
    }
    
    private void drawLine(float x1, float y1, float x2, float y2, Color color) {
    }
    
    public void pan(float dx, float dy) {
        offsetX += dx / zoom;
        offsetY += dy / zoom;
    }
    
    public void zoomIn() {
        zoom = Math.min(zoom * 1.2f, 3.0f);
    }
    
    public void zoomOut() {
        zoom = Math.max(zoom / 1.2f, 0.3f);
    }
    
    public Node getNodeAt(float x, float y) {
        float worldX = (x / zoom) - offsetX;
        float worldY = (y / zoom) - offsetY;
        
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            if (worldX >= node.x && worldX <= node.x + NODE_WIDTH &&
                worldY >= node.y && worldY <= node.y + NODE_HEIGHT) {
                return node;
            }
        }
        return null;
    }
    
    public void addNode(Node node) {
        nodes.add(node);
    }
    
    public void removeNode(Node node) {
        for (Node n : nodes) {
            n.connections.remove(node);
        }
        nodes.remove(node);
    }
    
    public void connectNodes(Node from, Node to) {
        if (!from.connections.contains(to)) {
            from.connections.add(to);
        }
    }
    
    public void clear() {
        nodes.clear();
    }
    
    public void startDrag(Node node, float x, float y) {
        dragNode = node;
        dragStart.set(x, y);
    }
    
    public void updateDrag(float x, float y) {
        if (dragNode != null) {
            float dx = (x - dragStart.x) / zoom;
            float dy = (y - dragStart.y) / zoom;
            dragNode.x += dx;
            dragNode.y += dy;
            dragStart.set(x, y);
        }
    }
    
    public void endDrag() {
        dragNode = null;
    }
    
    public void selectNode(Node node) {
        selectedNode = node;
    }
    
    public Node getSelectedNode() {
        return selectedNode;
    }
}