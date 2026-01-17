package com.shatteredpixel.shatteredpixeldungeon.studio;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;

public class StudioMod {
    
    public static void init() {
        showWelcome();
    }
    
    public static void showWelcome() {
        GameScene.show(new WndOptions(
            "STUDIO MOD",
            "Story & NPC System Loaded!\n\nThis is a simple working version.",
            "Awesome!"
        ) {
            @Override
            protected void onSelect(int index) {
            }
        });
    }
}