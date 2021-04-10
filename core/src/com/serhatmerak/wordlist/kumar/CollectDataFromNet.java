package com.serhatmerak.wordlist.kumar;

import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.word.CssSelectors;
import com.serhatmerak.wordlist.word.Word;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CollectDataFromNet {

    public WordData getData(Word word){
        if (DataHolder.dataholder.loadedWordData.get(word.name) != null)
            return DataHolder.dataholder.loadedWordData.get(word.name);
        WordData wordData = new WordData();
        wordData.word = word;

        getSynonymAndAntonym(word.name,wordData);
        System.out.println("+\n+\n+");
        getExamples(word.name,wordData);

        return wordData;
    }

    private void getExamples(String word, WordData wordData) {
        String link = "https://sentence.yourdictionary.com/";
        Document doc;
        try {
            doc = Jsoup.connect(link + word).get();
        } catch (IOException e){
            return;
        }

        Elements sentencesElements = doc.select("div.sentence-item");
        int i = 0;
        for (Element sentence:sentencesElements) {
            String text = sentence.text();
            wordData.sentences.add(text);

            System.out.println(text);
            i++;
            if (i == 20) break;
        }

    }

    private void getSynonymAndAntonym(String word, WordData wordData) {
        String link = "https://www.collinsdictionary.com/dictionary/english-thesaurus/";
        String packetDiv = "div.sense.opened.moreAnt.moreSyn";

        Document doc;
        try {
                doc = Jsoup.connect(link + word).get();
        } catch (IOException e){
            return;
        }

        Elements meaningDivElements = doc.select(packetDiv);
        System.out.println(meaningDivElements.size());

        for (Element innerGroup :meaningDivElements) {
            Elements synonymGroup = innerGroup.select("div.blockSyn");
            Elements antonymGroup = innerGroup.select("div.blockAnt");

            String def =  innerGroup.select("span.headwordSense").text();
            wordData.definitions.add(def);
            System.out.println("In the sense of " + def);

            if (synonymGroup.size() > 0) {
                Array<String> synonyms = new Array<>();
                for (Element names : synonymGroup.first().select("div.form.type-syn")) {

                    String synonym =  names.select("span.orth").text();
                    if (!synonym.equals("")) {
                        System.out.println(synonym);
                        synonyms.add(synonym);
                    }
                }
                for (Element longNames : synonymGroup.first().select("div.form.type-syn.orth")) {
                    System.out.println(longNames.text());

                    String synonym =  longNames.text();
                    synonyms.add(synonym);
                }
                wordData.synonyms.add(synonyms);
            }else
                wordData.synonyms.add(null);

            if (antonymGroup.size() > 0) {
                System.out.println("---Antonyms---");
                Array<String> antonyms = new Array<>();
                for (Element names : antonymGroup.first().select("div.form.type-ant")) {
                    System.out.println(names.select("span.orth").text());

                    String antonym =  names.select("span.orth").text();
                    antonyms.add(antonym);
                }
                wordData.antonyms.add(antonyms);
            }else
                wordData.antonyms.add(null);

            System.out.println("///////////////////////////////");
        }
    }
    /*
    {
    Def
    syn
    ant
    }
    {
    Def
    Syn
    Ant
    }
     */
    public static class WordData {
        public Array<String> sentences;
        public Array<String> definitions;
        public Array<Array<String>> synonyms;
        public Array<Array<String>> antonyms;
        public Word word;

        public WordData(){
            sentences = new Array<>();
            definitions = new Array<>();
            synonyms = new Array<>();
            antonyms = new Array<>();
        }
    }
}
