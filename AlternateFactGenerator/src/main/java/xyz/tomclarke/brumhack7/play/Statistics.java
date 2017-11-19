package xyz.tomclarke.brumhack7.play;

import xyz.tomclarke.brumhack7.BuildFactData;
import xyz.tomclarke.brumhack7.fact.Fact;
import xyz.tomclarke.brumhack7.fact.Facts;

/**
 * Shows statistics on what has been generated
 * 
 * @author Tom Clarke
 *
 */
public class Statistics {

    public static void main(String[] args) {
        Facts facts = BuildFactData.buildFactData(false);

        if (facts == null) {
            System.out.println("AlternateFactGenerator not ran before");
        } else {
            int numOfFacts = facts.size();
            int numOfAltFacts = 0;
            int counter = 0;
            int factIndexProcessed = 0;
            for (Fact fact : facts) {
                counter++;
                if (fact.getAlternative() != null) {
                    numOfAltFacts++;
                    factIndexProcessed = counter;
                }
            }

            System.out.println(String.format(
                    "%s facts scraped, %s alternate facts generated (%s alternate facts failed to generate)",
                    numOfFacts, numOfAltFacts, (factIndexProcessed - numOfAltFacts)));
        }
    }

}
