package maintenance;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLInstructionSet {
	//Constant to start index of (2) from start of line
	static final int LINE_BEGINNING = Integer.MIN_VALUE;
	static final int OPEN_TAG = Integer.MIN_VALUE + 1;//"<";
	static final int CLOSE_TAG = Integer.MIN_VALUE + 2;//">";
	//To search only the first occurrence of each instruction set to false
	private boolean searchingAll = true;
	//Key is URL, value is the percent of document that is skipped for performance
	private Object[] urlKey;
	/* List of instructions for each URL to find and extract correct strings
	 * Each inner list consists of 5 fields: 
	 * 		(1) "true" search from start, "false" search from end
	 * 		(2) Offset start index
	 * 		(3) Ending index
	 * 		(4) Matching string for index
	 * 		(5) Conditional matches (optional)
	 */
	private List<List<String>> instructions = new ArrayList<List<String>>();

	URLInstructionSet(URL url, int skipPercent){
		if(skipPercent < 0 || skipPercent > 100){
			throw new IllegalArgumentException("skipPercent argument must be bewteen 0 and 100");
		}
		urlKey = new Object[]{url, skipPercent};
	}
	
	URLInstructionSet(URL url){
		this(url, 0);
	}
	
	void addInstruction(boolean fromStart, int offsetStartIndex, int endIndex, String match, String...conditionalMatches){
		List<String> instruction = new ArrayList<String>();
		//(1)
		instruction.add(0, String.valueOf(fromStart));
		//(2)
		instruction.add(1, "" + offsetStartIndex);
		//(3)
		instruction.add(2, "" + endIndex);
		//(4)
		instruction.add(3, match);
		//(5)
		for(int i = 0; i < conditionalMatches.length; i++){
			instruction.add(4 + i, conditionalMatches[i]);
		}
		instructions.add(instruction);
	}
	
	void addInstruction(boolean fromStart, int offsetStartIndex, int endIndex, String match){
		addInstruction(fromStart, offsetStartIndex, endIndex, match, "");
	}
	
	void addInstruction(int offsetStartIndex, int endIndex, String match){
		addInstruction(true, offsetStartIndex, endIndex, match, "");
	}
	
	void setSearchingAll(boolean searchingAll){
		this.searchingAll = searchingAll;
	}
	
	List<List<String>> getInstructions(){
		return instructions;
	}
	
	boolean isSearchingAll(){
		return searchingAll;
	}
	
	Object[] getUrlKey(){
		return urlKey;
	}
}
