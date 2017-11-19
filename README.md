# BrumHack 7 - Alternate Fact Generator

## What does it do?
We scrape [Wikipedia's fact archive](https://en.wikipedia.org/wiki/Wikipedia:Recent_additions) to get a lot of facts (81749 found so far), parse it all using [Stanford CoreNLP](https://stanfordnlp.github.io/CoreNLP/) to get the semantics of a sentence and then use [Word2Vec](https://deeplearning4j.org/word2vec.html) to *alter* the facts.

Word2Vec, while no one seems knows quite how it works, is very good at calculating the difference between words (or at least fun to play with), so if you give it a phrase it can return similar words (based off the preprocessed Google News corpus).

To get our alternate facts:

* we parse facts
* find the noun phrase with the most tokens that don't contain a child noun phrase
* extracted the final noun in the phrase
* combine that token with up to three random tokens from `/usr/share/dict/words`
* add the tokens in Word2Vec and see what we get back (we only allow noun phrases so the structure of the sentence is still technically valid at a hackathon level)
* replace the selected noun phrase with a random noun phrase generated from Word2Vec

## Running this thing
### Prerequisites
* Java 8+ (64-bit)
* Maven (if building)
* [Google News corpus](https://s3.amazonaws.com/dl4j-distribution/GoogleNews-vectors-negative300.bin.gz) (in the same directory as the executable jar)
* An internet connection (to download the Wikipedia information)
* Linux (recommended)
* 10GiB+ RAM, a fast processor (recommended)

### What to run
* To run the processor, run the main class `xyz.tomclarke.brumhack7.play.GenerateFacts`
* To view statistics on what you've generated so far run `xyz.tomclarke.brumhack7.play.Statistics`
* To play with Word2Vec using the Google News Corupus, run `xyz.tomclarke.brumhack7.play.TestWord2Vec`

## What did we achieve
After running it on a 8-thread i7 4th-gen @ 2.4GHz with 8GiBs of RAM for ~7.5 hours, we got the following generated:
```81749 facts scraped, 2670 alternate facts generated (181 alternate facts failed to generate)```

That means we only processed ~3.5% of all facts in Wikipedia's archives... so it should take at least 200 hours at our current rate to attempt generating an alternate fact for each fact we found!

See `alternate_facts_full.txt` for all the alternate facts generated and `alternate_facts_highlights.txt` for highlights.

## Notes

* Generated fact files are written to `alternate_facts.txt` and are in pairs, where the first is the original (tagged `OG`) and the second is the alternate version (tagged `AL`).
* To run you may require the following Java VM arguments for Word2Vec to work: `-Xms1024m -Xmx10g`
* It is supposedly possible to run this on Windows, but configuring it to allow Word2Vec to run is very hard... Just use Linux (the project was made on Ubuntu 14.04) as it runs with basically no extra effort than importing the classes in Java.

## Authors
[TomBClarke](https://github.com/TomBClarke), [damarlow](https://github.com/damarlow), [rowanphilip](https://github.com/rowanphilip)

