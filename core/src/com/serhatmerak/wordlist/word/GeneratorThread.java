package com.serhatmerak.wordlist.word;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.huds.addwordhuds.AddWordWindow;
import com.serhatmerak.wordlist.huds.addwordhuds.AddWordsFromFileHud;
import com.serhatmerak.wordlist.huds.addwordhuds.DividedTextGroup;

public class GeneratorThread {
    AddWordWindow addWordWindow;
    AddWordsFromFileHud addWordsFromFileHud;
    DividedTextGroup dividedTextGroup;
    WordGenerator wordGenerator;

    public void initalize(AddWordWindow addWordWindow){
        this.addWordWindow = addWordWindow;
        wordGenerator = new WordGenerator();
    }

    public void initalize(AddWordsFromFileHud addWordsFromFileHud){
        this.addWordsFromFileHud = addWordsFromFileHud;
        wordGenerator = new WordGenerator();
    }

    public void initalize(){
        wordGenerator = new WordGenerator();
    }

    public void addWordWithNameOrLink(final TextField textField){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Word word = wordGenerator.GenerateWord(textField.getText());
                if (word == null){
                    addWordWindow.errorLbl.setVisible(true);
                    addWordWindow.getWordFromThreadWithName(textField, null);
                }else if (word.meaningDivs.size == 0){
                    addWordWindow.getWordFromThreadWithName(textField,null);
                    if (addWordWindow.errorLbl.isVisible())
                        addWordWindow.errorLbl.setVisible(false);
                }else {
                    addWordWindow.getWordFromThreadWithName(textField,word);
                    if (addWordWindow.errorLbl.isVisible())
                        addWordWindow.errorLbl.setVisible(false);
                }
            }
        }).start();

    }
/*
    public void addWordWithFile(final Label label){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Word word = wordGenerator.GenerateWord(label.getText().toString());
                if (word.meaningDivs.size == 0){
                    System.out.println(word.name + " null");
                    addWordsFromFileHud.getWordFromThreadWithFile(label,null);
                }else {
                    System.out.println(word.name + " eklendi");
                    addWordsFromFileHud.getWordFromThreadWithFile(label,word);
                }
            }
        }).start();

    }
*/
    public void addWordWithFile(final Array<Label> labels,final DividedTextGroup dividedTextGroup){

        final ObjectMap<Label,Word> map = new ObjectMap<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Label l : labels) {
                    Word word = wordGenerator.GenerateWord(l.getText().toString());
                    if (word == null){
                        dividedTextGroup.errorLbl.setVisible(true);
                        dividedTextGroup.getWordFromThreadWithFile(l, 0);
                        map.put(l,null);
                    }else if (word.meaningDivs.size == 0) {
                        dividedTextGroup.getWordFromThreadWithFile(l, 0);
                        map.put(l,null);
                        if (dividedTextGroup.errorLbl.isVisible())
                            dividedTextGroup.errorLbl.setVisible(false);
                    }else if (DataHolder.dataholder.knownWords.contains(word.name,false)){
                        dividedTextGroup.getWordFromThreadWithFile(l, 2);
                        map.put(l,null);
                        if (dividedTextGroup.errorLbl.isVisible())
                            dividedTextGroup.errorLbl.setVisible(false);
                    } else {
                        dividedTextGroup.getWordFromThreadWithFile(l, 1);
                        map.put(l,word);
                        if (dividedTextGroup.errorLbl.isVisible())
                            dividedTextGroup.errorLbl.setVisible(false);
                    }
                }
                dividedTextGroup.wordMap = map;
            }
        }).start();
    }




}
