package com.serhatmerak.wordlist.kumar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Arrays;

public class WordCount {

    ObjectMap<String ,Integer> mapObject;

    public WordCount(){
        mapObject = new ObjectMap<>();
        FileHandle handle = Gdx.files.internal("icons/books.txt");
        String text = handle.readString();
        text.replaceAll("[,.?]"," ");
        String[] wordsArray = text.split("\\r?\\n? ");

//            loadFileAndWordsTable.setDebug(true);
        if (wordsArray.length > 0) {
            Array<String> checkWords = new Array<>();
            for (String word : wordsArray) {
                String newWord = word.replaceAll(
                        "[^a-zA-Z]", "");
                newWord = newWord.toLowerCase();
                if (!newWord.equals("") && !checkWords.contains(newWord,false)) {
                    checkWords.add(newWord);
                    mapObject.put(newWord,1);
                }else if (!newWord.equals("")){
                    mapObject.put(newWord,mapObject.get(newWord) + 1);
                }
            }
        }
        Array<Integer> integers = mapObject.values().toArray();
        int[] ints = new int[integers.size];
        for (int i=0;i<integers.size;i++) {
            ints[i] = integers.get(i);
        }
        Arrays.sort(ints);
       for (int i=0;i<mapObject.size;i++){
           System.out.println(mapObject.findKey(ints[i],false) + " " + ints[i]);
           FileHandle file = Gdx.files.local("has/" + mapObject.keys().toArray().get(i));
           file.writeString(mapObject.get(mapObject.keys().toArray().get(i)) + "", false);
       }

       System.out.println("coal:" + mapObject.get("coal"));
}}
