package maintenance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class URLlineMatcher {
	private URLInstructionSet instructionSet;
	
	URLlineMatcher(URLInstructionSet instructionSet) throws IOException{
		this.instructionSet = instructionSet;
	}
	
	List<String> getMatches() throws IOException{
		URL url = (URL)instructionSet.getUrlKey()[0];
		int skipPercent = (int)instructionSet.getUrlKey()[1];
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setRequestMethod("GET");
		httpConnection.connect();
		InputStream is = httpConnection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		reader.skip((int)(is.available() * (skipPercent * .01)));
		List<String> matches = getMatches(reader);
		reader.close();
		is.close();
		return matches;
	}
	
	private List<String> getMatches(BufferedReader reader) throws IOException{
		List<String> matches = new ArrayList<String>();
		
		List<List<String>> instructions = instructionSet.getInstructions();
		String line = null;
		ReadLineLoop:
		while((line = reader.readLine()) != null){
			for(List<String> instruction : instructions){
				boolean fromStart = Boolean.valueOf(instruction.get(0));
				int offsetStartIndex = Integer.valueOf(instruction.get(1));
				int offsetEndIndex = Integer.valueOf(instruction.get(2));
				String[] matchStrings;
				if(instruction.get(4).isEmpty()){
					matchStrings = new String[]{instruction.get(3)};
				}else{
					matchStrings = new String[instruction.size() - 3];
					matchStrings[0] = instruction.get(3);
					for(int i = 4; i < instruction.size(); i++){
						matchStrings[i - 3] = instruction.get(i);
					}
				}
				int matchingIndex = lineMatches(line, fromStart, matchStrings);
				if(matchingIndex != -1){
					int startIndex = getIndex(line, fromStart, matchingIndex, offsetStartIndex);
					int endIndex = getIndex(line, fromStart, matchingIndex, offsetEndIndex);
					matches.add(line.substring(startIndex, endIndex));
					if(!instructionSet.isSearchingAll()){
						instructions.remove(instruction);
						if(instruction.size() == 0){
							break ReadLineLoop;
						}
					}
				}
			}
		}
		
		return matches;
	}
	
	private int lineMatches(String line, boolean fromStart, String[] matchStrings){
		for(String string : matchStrings){
			if(!line.contains(string)){
				return -1;
			}
		}
		if(fromStart){
			return line.indexOf(matchStrings[0]);
		}else{
			return line.lastIndexOf(matchStrings[0]);
		}
	}
	
	private int getIndex(String line, boolean fromStart, int matchIndex, int index){
		if(index == URLInstructionSet.CLOSE_TAG){
			if(fromStart){
				return line.indexOf(">", matchIndex) + 1;
			}else{
				return line.lastIndexOf(">", matchIndex) + 1;
			}
		}else if(index == URLInstructionSet.OPEN_TAG){
			if(fromStart){
				return line.indexOf("<", matchIndex);
			}else{
				return line.lastIndexOf("<", matchIndex);
			}
		}else if((matchIndex + index) < 0){
			return 0;
		}else{
			return matchIndex + index;
		}
	}
	
	/*
	private BufferedReader reader;
	
	URLlineMatcher(URL url) throws IOException{
		this(url, 0);
	}
	
	URLlineMatcher(URL url, int skipPercent) throws IOException{
		if(skipPercent < 0 || skipPercent > 100){
			throw new IllegalArgumentException("skipPercent argument must be bewteen 0 and 100");
		}
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setRequestMethod("GET");
		httpConnection.connect();
		InputStream is = httpConnection.getInputStream();
		reader = new BufferedReader(new InputStreamReader(is));
		reader.skip((int)(is.available() * (skipPercent * .01)));
	}
	
	List<String> getLinesMatching(String match) throws IOException{
		List<String> lines = new ArrayList<String>();
		String line = null;
		while((line = reader.readLine()) != null){
			if(line.contains(match)){
				lines.add(line);
			}
		}
		return lines;
	}
	
	List<String> getLinesMatching(String...matches) throws IOException{
		List<String> matchingLines = getLinesMatching(matches[0]);
		for(int i = 1; i < matches.length; i++){
			matchingLines = getMatches(matchingLines, matches[i]);
		}
		return matchingLines;
	}
	
	List<String> getAllLinesMatching(String...matches) throws IOException{
		List<String> lines = new ArrayList<String>();
		String line = null;
		readLineLoop:
		while((line = reader.readLine()) != null){
			for(String string : matches){
				boolean match = isMatch(line, string);
				if(match){
					lines.add(line);
					continue readLineLoop;
				}
			}
		}
		return lines;
	}
	
	private boolean isMatch(String line, String match){
		if(line.contains(match)){
			return true;
		}
		return false;
	}
	
	private List<String> getMatches(List<String> lines, String match){
		List<String> matches = new ArrayList<String>();
		
		for(String string : lines){
			if(string.contains(match)){
				matches.add(string);
			}
		}
		
		return matches;
	}
	*/
}
