package com.mweis.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mweis.game.BoxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 720;//1280;
		config.height = 480;//720;
		config.vSyncEnabled = true;
		new LwjglApplication(new BoxGame(), config);
	}
}
