/*
 *  Given a jsoup Document return a list of Elements matching a criteria in concrete class
 */

package screenScraper;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public interface ElementFinder {
	Elements find(Document document);
}
