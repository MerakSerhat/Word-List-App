package com.serhatmerak.wordlist.word;

import com.badlogic.gdx.utils.Array;

public class WordList {
    public String listName;
    public boolean isStarry;
    public Array<Word> words;

    public WordList(){
        words = new Array<>();
    }
}
