package com.serhatmerak.wordlist.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = AppInfo.WIDTH;
		config.height = AppInfo.HEIGHT;
//		config.resizable = true;
//		config.title = "Word List";
//		config.fullscreen = true;
		new LwjglApplication(new AppMain(), config);
	}
}
