/*
 *  Given a URL returns a jsoup.Document
 */

package screenScraper;

import java.io.IOException;
import java.net.URL;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DocumentProducer{
	
	public static Document create(URL url){
		Document document = null;
		
		Connection connection = Jsoup.connect(url.toString());
		// 'SocketTimeoutException: read timed out' kept occurring without setting the timeout to longer
		// used arbitrary number 35000 with no issues since
		connection.timeout(35000);
		try {
			document = connection.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return document;
	}
}