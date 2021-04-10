package com.serhatmerak.wordlist.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.serhatmerak.wordlist.kumar.CollectDataFromNet;
import com.serhatmerak.wordlist.word.MeaningDiv;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.Collator;
import java.util.Collection;
import java.util.TreeSet;


public class DataHolder {
    public static final DataHolder dataholder = new DataHolder();
    private DataHolder(){};
    public Array<WordList> wordLists;
    public WordList favList;

    public Array<String> knownWords;
    public Array<String> yourKnownWords;
    public Array<String> firstKnownWords;
    public Array<String> secondKnownWords;
    public Array<String> thirdKnownWords;

    public Array<String> fileTextsForExporting;
    public String favListTextForExporting;

    public int SELECTED_LIST_INDEX = 0;

    public boolean bigWordCards = false;

    public void initialize(){
        //TODO: Read Lists vs
        wordLists = new Array<>();
        knownWords = new Array<>();
        firstKnownWords = new Array<>();
        secondKnownWords = new Array<>();
        thirdKnownWords = new Array<>();
        yourKnownWords = new Array<>();
        fileTextsForExporting = new Array<>();
        loadedWordData = new ObjectMap<>();
        loadWordData();
    }

    public void saveList(final WordList list) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    FileHandle file;
                    if (list == favList)
                        file = Gdx.files.local("fav_list.xml");
                    else
                        file = Gdx.files.local("lists/" + list.listName +".xml");
                    Writer writer = file.writer(false);
                    XmlWriter xmlWriter = new XmlWriter(writer);
                    xmlWriter.element("main");
                    xmlWriter.element("is_starry", list.isStarry);
                    for (Word word : list.words) {
                        xmlWriter.element("word");
                        xmlWriter.element("name", word.name);
                        xmlWriter.element("star",word.star);
                        for (MeaningDiv d : word.meaningDivs) {
                            xmlWriter.element("div");
                            xmlWriter.element("title", d.title);
                            xmlWriter.element("eng-def", d.engDef);
                            xmlWriter.element("tr-def", d.trDef);
                            xmlWriter.element("examples");
                            for (String ex : d.examples) {
                                xmlWriter.element("ex", ex);
                            }
                            xmlWriter.pop().pop();
                        }
                        xmlWriter.pop();
                    }
                    xmlWriter.pop();
                    xmlWriter.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadLists(){
        FileHandle dirHandle = Gdx.files.internal("lists");
        for (FileHandle listFile:dirHandle.list()) {
            WordList wordList = new WordList();
            wordList.listName = listFile.nameWithoutExtension();
            wordList.words = new Array<>();
            XmlReader xmlReader = new XmlReader();

            fileTextsForExporting.add(listFile.readString());

            Reader reader = listFile.reader("ISO-8859-9");
            XmlReader.Element root = xmlReader.parse(reader);
            wordList.isStarry = root.getBoolean("is_starry");
            for (XmlReader.Element wordElement : root.getChildrenByName("word")) {
                Word word = new Word(wordElement.getChildByName("name").getText());
                word.star = wordElement.getBoolean("star");
                word.meaningDivs = new Array<>();
                for (XmlReader.Element divElement:wordElement.getChildrenByName("div")) {
                    MeaningDiv meaningDiv = new MeaningDiv();
                    meaningDiv.title = divElement.getChildByName("title").getText();
                    meaningDiv.engDef = divElement.getChildByName("eng-def").getText();
                    meaningDiv.trDef = divElement.getChildByName("tr-def").getText();
                    XmlReader.Element examplesElement = divElement.getChildByName("examples");
                    meaningDiv.examples = new String[examplesElement.getChildCount()];
                    for (int i = 0; i < meaningDiv.examples.length; i++) {
                        meaningDiv.examples[i] = examplesElement.getChildrenByName("ex").get(i).getText();
                    }
                    word.meaningDivs.add(meaningDiv);
                }
                wordList.words.add(word);
            }
            wordLists.add(wordList);
        }

        //load known words
        loadKnownWords();
        loadFavList();


    }

    public void loadExportedList(String filename){

        FileHandle listFile = Gdx.files.internal("lists/" + filename);
        WordList wordList = new WordList();
        wordList.listName = listFile.nameWithoutExtension();
        wordList.words = new Array<>();
        XmlReader xmlReader = new XmlReader();

        fileTextsForExporting.add(listFile.readString());

        Reader reader = listFile.reader("ISO-8859-9");
        XmlReader.Element root = xmlReader.parse(reader);
        for (XmlReader.Element wordElement : root.getChildrenByName("word")) {
            Word word = new Word(wordElement.getChildByName("name").getText());
            word.star = wordElement.getBoolean("star");
            word.meaningDivs = new Array<>();
            for (XmlReader.Element divElement:wordElement.getChildrenByName("div")) {
                MeaningDiv meaningDiv = new MeaningDiv();
                meaningDiv.title = divElement.getChildByName("title").getText();
                meaningDiv.engDef = divElement.getChildByName("eng-def").getText();
                meaningDiv.trDef = divElement.getChildByName("tr-def").getText();
                XmlReader.Element examplesElement = divElement.getChildByName("examples");
                meaningDiv.examples = new String[examplesElement.getChildCount()];
                for (int i = 0; i < meaningDiv.examples.length; i++) {
                    meaningDiv.examples[i] = examplesElement.getChildrenByName("ex").get(i).getText();
                }
                word.meaningDivs.add(meaningDiv);
            }
            wordList.words.add(word);
        }
        wordLists.add(wordList);
        SELECTED_LIST_INDEX = wordLists.size - 1;
    }

    private void loadFavList() {
        FileHandle favHandle = Gdx.files.internal("fav_list.xml");
        favList = new WordList();
        favList.listName = "Favorite Words";
        favList.words = new Array<>();

        favListTextForExporting = favHandle.readString();

        XmlReader xmlReader = new XmlReader();
        Reader reader = favHandle.reader("ISO-8859-9");
        XmlReader.Element root = xmlReader.parse(reader);
        for (XmlReader.Element wordElement : root.getChildrenByName("word")) {
            Word word = new Word(wordElement.getChildByName("name").getText());
            word.star = wordElement.getBoolean("star");
            word.meaningDivs = new Array<>();
            for (XmlReader.Element divElement:wordElement.getChildrenByName("div")) {
                MeaningDiv meaningDiv = new MeaningDiv();
                meaningDiv.title = divElement.getChildByName("title").getText();
                meaningDiv.engDef = divElement.getChildByName("eng-def").getText();
                meaningDiv.trDef = divElement.getChildByName("tr-def").getText();
                XmlReader.Element examplesElement = divElement.getChildByName("examples");
                meaningDiv.examples = new String[examplesElement.getChildCount()];
                for (int i = 0; i < meaningDiv.examples.length; i++) {
                    meaningDiv.examples[i] = examplesElement.getChildrenByName("ex").get(i).getText();
                }
                word.meaningDivs.add(meaningDiv);
            }
            favList.words.add(word);
        }
    }

    public boolean isThisWordFav(Word word){
        for (Word favWord:favList.words) {
            if (favWord.name.equals(word.name))
                return true;
        }
        return false;
    }

    public void removeWordFromFavorites(Word word){
        for (Word favWord:favList.words){
            if (favWord.name.equals(word.name)){
                favList.words.removeValue(favWord,true);
                saveList(favList);
                return;
            }
        }
    }

    private void loadKnownWords() {
        FileHandle customHandle = Gdx.files.internal("knownWords/customKnownWords.xml");

        XmlReader xmlReader = new XmlReader();
        Reader reader = customHandle.reader("ISO-8859-9");
        XmlReader.Element root = xmlReader.parse(reader);

        XmlReader.Element firstWordsElement = root.getChildByName("first");
        XmlReader.Element secondWordsElement = root.getChildByName("second");
        XmlReader.Element thirdWordsElement = root.getChildByName("third");


        for (XmlReader.Element wordElement : firstWordsElement.getChildrenByName("word")) {
            String title = wordElement.getChildByName("title").getText();
            boolean known = wordElement.getBoolean("known");

            firstKnownWords.add(title);
            if (known)
                knownWords.add(title);
        }

        for (XmlReader.Element wordElement : secondWordsElement.getChildrenByName("word")) {
            String title = wordElement.getChildByName("title").getText();
            boolean known = wordElement.getBoolean("known");

            secondKnownWords.add(title);
            if (known)
                knownWords.add(title);
        }

        for (XmlReader.Element wordElement : thirdWordsElement.getChildrenByName("word")) {
            String title = wordElement.getChildByName("title").getText();
            boolean known = wordElement.getBoolean("known");

            thirdKnownWords.add(title);
            if (known)
                knownWords.add(title);
        }

        FileHandle yourHandle = Gdx.files.internal("knownWords/yourKnownWords.xml");

        XmlReader xmlReader2 = new XmlReader();
        Reader reader2 = yourHandle.reader("ISO-8859-9");
        XmlReader.Element root2 = xmlReader2.parse(reader2);

        for (XmlReader.Element wordElement : root2.getChildrenByName("word")) {
            if (!yourKnownWords.contains(wordElement.getText(),false))
                yourKnownWords.add(wordElement.getText());
            if (!knownWords.contains(wordElement.getText(),false))
                knownWords.add(wordElement.getText());
        }
        sortYourWords();
        sortFirstWords();
        sortSecondWords();
    }

    private void sortSecondWords() {
        Collection<String> countryNames =
                new TreeSet<String>(Collator.getInstance());

        for (int i = 0; i < secondKnownWords.size; i++) {
            countryNames.add(secondKnownWords.get(i));
        }
        secondKnownWords.clear();
        for (String s:countryNames) {
            secondKnownWords.add(s);
        }
    }

    private void sortFirstWords() {
        Collection<String> countryNames =
                new TreeSet<String>(Collator.getInstance());

        for (int i = 0; i < firstKnownWords.size; i++) {
            countryNames.add(firstKnownWords.get(i));
        }
        firstKnownWords.clear();
        for (String s:countryNames) {
            firstKnownWords.add(s);
        }
    }

    private void sortYourWords() {
        Collection<String> countryNames =
                new TreeSet<String>(Collator.getInstance());

        for (int i = 0; i < yourKnownWords.size; i++) {
            countryNames.add(yourKnownWords.get(i));
        }
        yourKnownWords.clear();
        for (String s:countryNames) {
            yourKnownWords.add(s);
        }
    }

    public void removeListFile(int index){
        WordList wordList = wordLists.get(index);
        Gdx.files.local("lists/" + wordList.listName + ".xml").delete();
        wordLists.removeIndex(index);
    }

    public void saveKnownCustomWords() {
        try {

            FileHandle file = Gdx.files.local("knownWords/customKnownWords.xml");
            Writer writer = file.writer(false);
            XmlWriter xmlWriter = new XmlWriter(writer);

            xmlWriter.element("main");
            xmlWriter.element("first");
            for (String word:firstKnownWords) {
                xmlWriter.element("word");
                xmlWriter.element("title", word);
                xmlWriter.element("known", knownWords.contains(word,false));
                xmlWriter.pop();
            }

            xmlWriter.pop();
            xmlWriter.element("second");
            for (String word:secondKnownWords) {
                xmlWriter.element("word");
                xmlWriter.element("title", word);
                xmlWriter.element("known", knownWords.contains(word,false));
                xmlWriter.pop();
            }
            xmlWriter.pop();
            xmlWriter.element("third");
            for (String word:thirdKnownWords) {
                xmlWriter.element("word");
                xmlWriter.element("title", word);
                xmlWriter.element("known", knownWords.contains(word,false));
                xmlWriter.pop();
            }

            xmlWriter.pop();
            xmlWriter.pop();
            xmlWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveYourKnownWords() {
        try {

            FileHandle file = Gdx.files.local("knownWords/yourKnownWords.xml");
            Writer writer = file.writer(false);
            XmlWriter xmlWriter = new XmlWriter(writer);

            xmlWriter.element("main");
            for (String word:yourKnownWords) {
                xmlWriter.element("word",word);
            }

            xmlWriter.pop();
            xmlWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getStarryWordCount(){
        int count = 0;
        for (Word word:getSelectedList().words) {
            if (word.star) count++;
        }

        return count;
    }

    public WordList sortAZlist(WordList wordList){
        Collection<String> countryNames =
                new TreeSet<String>(Collator.getInstance());

        ObjectMap<String,Word> map = new ObjectMap<>();

        WordList newWorList = new WordList();
        newWorList.listName = wordList.listName;
        newWorList.words = new Array<>();

        for (int i = 0; i < wordList.words.size; i++) {
            Word word = wordList.words.get(i);
            map.put(word.name,word);
            countryNames.add(word.name);
        }
        for (String title:countryNames) {
            newWorList.words.add(map.get(title));
        }

        return newWorList;
    }

    public WordList getSelectedList(){
        if (SELECTED_LIST_INDEX >= 0)
            return wordLists.get(SELECTED_LIST_INDEX);
        else{
            return favList;
        }
    }

    public String getSelectedListTextForExporting(){
        if (SELECTED_LIST_INDEX >= 0)
            return fileTextsForExporting.get(SELECTED_LIST_INDEX);
        else{
            return favListTextForExporting;
        }
    }

    public ObjectMap<String, CollectDataFromNet.WordData> loadedWordData;

    public void saveWordData(CollectDataFromNet.WordData wordData){
        if(loadedWordData.get(wordData.word.name) != null)
            return;
        if(wordData.sentences.size == 0)
            return;
        String fileName = getWordDataFileName(wordData.word.name) + ".xml";

        try {

            FileHandle file = Gdx.files.local("word_data/" + fileName);
            String firstText = file.readString();
            firstText = firstText.replace("<main>","").replace("</main>","");

            Writer writer = file.writer(false);
            XmlWriter xmlWriter = new XmlWriter(writer);

            xmlWriter.element("word");

            xmlWriter.element("name", wordData.word.name);
            xmlWriter.element("star",wordData.word.star);
            for (MeaningDiv d : wordData.word.meaningDivs) {
                xmlWriter.element("div");
                xmlWriter.element("title", d.title);
                xmlWriter.element("eng-def", d.engDef);
                xmlWriter.element("tr-def", d.trDef);
                xmlWriter.element("examples");
                for (String ex : d.examples) {
                    xmlWriter.element("ex", ex);
                }
                xmlWriter.pop().pop();
            }

            xmlWriter.element("sentences");
            for (String sentence:wordData.sentences) {
                xmlWriter.element("sentence",sentence);
            }
            xmlWriter.pop();

            xmlWriter.element("definitions");
            for (String definition:wordData.definitions) {
                xmlWriter.element("definition",definition);
            }
            xmlWriter.pop();

            xmlWriter.element("synonyms");
            for (Array<String> synonymPack:wordData.synonyms) {
                if (synonymPack != null) {
                    xmlWriter.element("synonymPack");
                    for (String syn : synonymPack) {
                        xmlWriter.element("syn", syn);
                    }
                    xmlWriter.pop();
                }
            }
            xmlWriter.pop();

            xmlWriter.element("antonyms");
                for (Array<String> antonymPack : wordData.antonyms) {
                    if (antonymPack != null) {
                        xmlWriter.element("antonymPack");
                        for (String ant : antonymPack) {
                            xmlWriter.element("ant", ant);
                        }
                        xmlWriter.pop();
                    }else xmlWriter.element("antonymPack",null);
                }
            xmlWriter.pop();


            xmlWriter.pop();
            xmlWriter.close();

            String additionalText = file.readString();

            String lastText = "<main>\n" + additionalText + firstText + "\n</main>";

            file.writeString(lastText,false);

            loadedWordData.put(wordData.word.name,wordData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadWordData(){
        String[] fileNames = new String[]{"ac","df","gi","jl","mo","pr","su","vx","yz"};
        for (String fileName:fileNames) {
            FileHandle file = Gdx.files.internal("word_data/" + fileName + ".xml");

            XmlReader xmlReader = new XmlReader();

            Reader reader = file.reader("ISO-8859-9");
            XmlReader.Element root = xmlReader.parse(reader);
            for (XmlReader.Element wordElement : root.getChildrenByName("word")) {
                CollectDataFromNet.WordData wordData = new CollectDataFromNet.WordData();
                Word word = new Word(wordElement.getChildByName("name").getText());
                word.star = wordElement.getBoolean("star");
                word.meaningDivs = new Array<>();
                for (XmlReader.Element divElement:wordElement.getChildrenByName("div")) {
                    MeaningDiv meaningDiv = new MeaningDiv();
                    meaningDiv.title = divElement.getChildByName("title").getText();
                    meaningDiv.engDef = divElement.getChildByName("eng-def").getText();
                    meaningDiv.trDef = divElement.getChildByName("tr-def").getText();
                    XmlReader.Element examplesElement = divElement.getChildByName("examples");
                    meaningDiv.examples = new String[examplesElement.getChildCount()];
                    for (int i = 0; i < meaningDiv.examples.length; i++) {
                        meaningDiv.examples[i] = examplesElement.getChildrenByName("ex").get(i).getText();
                    }
                    word.meaningDivs.add(meaningDiv);
                }
                wordData.word = word;
                //sentences
                for (XmlReader.Element sentenceElement:wordElement.getChildByName("sentences").getChildrenByName("sentence")){
                    wordData.sentences.add(sentenceElement.getText());
                }

                //sentences
                for (XmlReader.Element definitionElement:wordElement.getChildByName("definitions").getChildrenByName("definition")){
                    wordData.definitions.add(definitionElement.getText());
                }

                //synonyms
                for (XmlReader.Element synonymPack:wordElement.getChildByName("synonyms").getChildrenByName("synonymPack")){
                    if (synonymPack.getChildCount() == 0)
                        wordData.synonyms.add(null);
                    else {
                        Array<String> synonymsArray = new Array<>();
                        for (XmlReader.Element synonym:synonymPack.getChildrenByName("syn")) {
                            synonymsArray.add(synonym.getText());
                        }
                        wordData.synonyms.add(synonymsArray);
                    }
                }

                //antonym
                for (XmlReader.Element antonymPack:wordElement.getChildByName("antonyms").getChildrenByName("antonymPack")){
                    if (antonymPack.getChildCount() == 0)
                        wordData.antonyms.add(null);
                    else {
                        Array<String> antonymsArray = new Array<>();
                        for (XmlReader.Element antonym:antonymPack.getChildrenByName("ant")) {
                            antonymsArray.add(antonym.getText());
                        }
                        wordData.antonyms.add(antonymsArray);
                    }
                }

                loadedWordData.put(word.name,wordData);
                System.out.println(wordElement.getChildByName("sentences"));
            }
        }

        System.out.println(loadedWordData.size);
    }

    private String getWordDataFileName(String name) {
        String[] fileNames = new String[]{"ac","df","gi","jl","mo","pr","su","vx","yz"};
        String letters = "abcdefghijklmnopqrstuvwxyz";

        int index = letters.indexOf(name.substring(0,1).toLowerCase().replace("ı","i"));
        //remove exceed number
        index -= index % 3;

        int fileIndex = index / 3;

        return fileNames[fileIndex];
    }
}
