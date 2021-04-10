package com.serhatmerak.wordlist.word;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class MeaningDiv {
	public boolean isCollacation;
	//coll ise o değilse kelime
	public String title;
	public String engDef;
	public String trDef;
	public String[] examples;
	public String difficulty;

	//;Libgdx için
	public Label titleLabel;
	public Label engDefLabel;
	public Label trDefLabel;
	public Array<Label> exampleLabels;

	
}