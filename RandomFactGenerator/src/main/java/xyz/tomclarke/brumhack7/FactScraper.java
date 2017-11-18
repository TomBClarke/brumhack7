package xyz.tomclarke.brumhack7;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * Scrapes facts from Wikipedia's 'Did You Know' archives
 * 
 * @author Rowan Cole
 *
 */
public class FactScraper {

    private ArrayList<String> spitFacts;
    private int factCount;

    public FactScraper() {
        spitFacts = new ArrayList<String>();
        factCount = 0;
        String[] years = { "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015",
                "2016" };
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };

        for (String year : years) {
            for (String month : months) {
                scrapePage("https://en.wikipedia.org/wiki/Wikipedia:Recent_additions/" + year + "/" + month);
            }
        }
    }

    /**
     * Scrapes a list of facts from a Wikipedia fact page
     * 
     * @param url
     *            The URL to get facts from
     */
    private void scrapePage(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements factLists = doc.select(".mw-parser-output>ul");

            for (Element factList : factLists) {
                Elements facts = factList.select("li");
                for (Element fact : facts) {
                    String factString = Jsoup.clean(fact.toString(), Whitelist.none());

                    if (factString.length() <= "22:18, 23 September 2005 (UTC)".length()) {
                        // Relax it's just a hackathon
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

    public ArrayList<String> getSpitFacts() {
        return spitFacts;
    }

    public void setSpitFacts(ArrayList<String> spitFacts) {
        this.spitFacts = spitFacts;
    }

    public int getFactCount() {
        return factCount;
    }

    public void setFactCount(int factCount) {
        this.factCount = factCount;
    }

    /**
     * Gets the next fact... or not
     * 
     * @return A new fact
     */
    public String getSpitFact() {
        String fact = "We are going to win BrumHack";

        if (factCount < spitFacts.size()) {
            fact = spitFacts.get(factCount);
        }
        return fact;
    }
}
