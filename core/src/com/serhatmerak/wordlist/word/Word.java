package com.serhatmerak.wordlist.word;

import com.badlogic.gdx.utils.Array;

public class Word {
	
	public String name;
	public boolean star;
	public Array<MeaningDiv> meaningDivs;
	public Word(String name) {
		this.name = name;
	}
	
}


