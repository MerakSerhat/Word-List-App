package com.serhatmerak.wordlist.word;

import com.badlogic.gdx.utils.Array;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WordGenerator {
	
	public Word word;

	
	public Word GenerateWord(String wordName) {
		word = new Word(wordName);
		Document doc;
		
		try {
			if (wordName.indexOf("dictionary.cambridge.org") > 0) {
				doc = Jsoup.connect(wordName).get();
			}else {
				doc = Jsoup.connect(CssSelectors.DictionaryLink + wordName).get();
			}
		} catch (IOException e){
			return null;
		}

		Elements meaningDivElements = doc.select(CssSelectors.MeaningMainDiv);
		System.out.println(meaningDivElements.size());
		Array<MeaningDiv> meaningDivs = new Array<>();
		String title = getTextIfExist(doc.select(CssSelectors.Title));;
		word.name = title;

		for (Element element : meaningDivElements) {
			MeaningDiv div =  new MeaningDiv();
			div.isCollacation = element.select(CssSelectors.IsCollacation).size() != 0;
			
			if(div.isCollacation)
				div.title = getTextIfExist(element.select(CssSelectors.Collacation));
			else
				div.title = title;
			
			div.engDef = getTextIfExist(element.select(CssSelectors.EngDefSelector));
			div.trDef = getTextIfExist(element.select(CssSelectors.TrDefSelector));
			Elements exampleElements = element.select(CssSelectors.Examples);
			div.examples = new String[exampleElements.size()];
			for (int i = 0; i <div.examples.length; i++) {
				div.examples[i] = exampleElements.get(i).text();
			}

			div.difficulty = getTextIfExist(element.select(CssSelectors.Difficulty));
			
			
			meaningDivs.add(div);
		}
		
		word.meaningDivs = meaningDivs;


		return word;
	}

	public String getTextIfExist(Elements elements){
		if (elements.size() == 0) return  "";
		else return elements.first().text();
	}

}
