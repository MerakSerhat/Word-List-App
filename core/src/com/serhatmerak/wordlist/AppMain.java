package com.serhatmerak.wordlist;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.screens.FirstScreen;
import com.serhatmerak.wordlist.screens.MetinScreen;

import javax.swing.JFrame;

public class AppMain extends Game {

	public SpriteBatch batch;

	@Override
	public void create () {

// set resolution to default and set full-screen to tru

		batch = new SpriteBatch();
		Fonts.createFonts();
		DataHolder.dataholder.initialize();
		DataHolder.dataholder.loadLists();
		Assets.assets.initalize();

//		WordCount wordCount = new WordCount();


		JFrame f = new JFrame();

		new Thread(new Runnable() {
			@Override
			public void run() {
//				JFileChooser chooser = new JFileChooser();
//				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//				chooser.setDragEnabled(true);
//				JFrame f = new JFrame();
//				f.setVisible(true);
//				f.toFront();
//				f.setVisible(false);
//				int res = chooser.showSaveDialog(f);
//				f.dispose();
//				chooser.setVisible(false);
//				if (res == JFileChooser.APPROVE_OPTION) {
//					FileHandle file = Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath() + "/" +
//							DataHolder.dataholder.getSelectedList().listName + ".wordlist");
//					file.writeString(DataHolder.dataholder.getSelectedListTextForExporting(),false);
//				}
			}
		}).start();




//		setScreen(new MainScreen(this));
//		setScreen(new ReadBookScreen(this));
		setScreen(new FirstScreen(this));
//		setScreen(new ReaderScreen(this));
//		setScreen(new MetinScreen(this));
//		setScreen(new KnownWordsScreen(this));
//		System.out.println(Gdx.graphics.getWidth());
//		Gdx.graphics.setWindowedMode(AppInfo.WIDTH / 4,AppInfo.HEIGHT);
//		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
//		setScreen(new KumarGraf(this));
	}


	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
