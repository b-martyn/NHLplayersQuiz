/*
 *  given a jsoup Element, return the required string
 */

package screenScraper;

import org.jsoup.nodes.Element;

public interface DataExtractor {
	String extract(Element element);
}
