package screenScraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLFinder {
	
	private Document document;
	private ElementFinder finder;
	private DataExtractor extractor;
	
	private String prefix = "";
	private String suffix = "";
	
	public URLFinder(Document document, ElementFinder finder, DataExtractor extractor) {
		this.document = document;
		this.finder = finder;
		this.extractor = extractor;
	}

	public URL[] find() throws MalformedURLException {
		List<URL> urls = new ArrayList<>();
		
		Elements elements = finder.find(document);
		for(Element element : elements){
			try {
				urls.add(new URL(prefix + extractor.extract(element) + suffix));
			} catch (MalformedURLException e) {
				throw new MalformedURLException("Prefix: " + prefix + "\nSuffix: " + suffix + "\nMatched String: " + extractor.extract(element));
			}
		}
		
		return urls.toArray(new URL[urls.size()]);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public ElementFinder getFinder() {
		return finder;
	}

	public void setFinder(ElementFinder finder) {
		this.finder = finder;
	}

	public DataExtractor getExtractor() {
		return extractor;
	}

	public void setExtractor(DataExtractor extractor) {
		this.extractor = extractor;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
