package xyz.tomclarke.brumhack7;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class FactScraper {
	private ArrayList<String> spitFacts;
	private int factCount;
	
	public FactScraper() {
		spitFacts = new ArrayList<String>();
		factCount = 0;
		try {
			PrintWriter writer = new PrintWriter("facts.txt", "UTF-8");
			String[] years = {"2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016"};
			String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			
			for (String year : years) {
				for (String month : months) {
					scrapePage("https://en.wikipedia.org/wiki/Wikipedia:Recent_additions/" + year + "/" + month);
				}
			}
			
			for (String spitFact : spitFacts) {
				writer.println(spitFact);
			}
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void scrapePage(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements factLists = doc.select(".mw-parser-output>ul");
			
			for (Element factList : factLists) {
				Elements facts = factList.select("li");
				for (Element fact: facts) {
					String factString = Jsoup.clean(fact.toString(), Whitelist.none());
					
					if (factString.length() <= "22:18, 23 September 2005 (UTC)".length()) { // Relax it's just a hackathon
						continue;
					}
					
					factString = factString.replace(" (pictured)", "");
					factString = factString.replace("... that ", "");
					factString = factString.replace("... that, ", "");
					factString = factString.replace("...that ", "");
					factString = factString.replace("... ", "");
					factString = factString.replace("...", "");
					factString = factString.replace("?", "");
					spitFacts.add(factString);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSpitFact() {
		String fact = "We are going to win BrumHack";
		
		if (factCount < spitFacts.size()) {
			fact = spitFacts.get(factCount);
		}
		return fact;
	}
}
