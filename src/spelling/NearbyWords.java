/**
 * 
 */
package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * @author UC San Diego Intermediate MOOC team
 *
 */
public class NearbyWords implements SpellingSuggest {
	// THRESHOLD to determine how many words to look through when looking
	// for spelling suggestions (stops prohibitively long searching)
	// For use in the Optional Optimization in Part 2.
	private static final int THRESHOLD = 100000; 

	Dictionary dict;

	public NearbyWords (Dictionary dict) 
	{
		this.dict = dict;
	}

	/** Return the list of Strings that are one modification away
	 * from the input string.  
	 * @param s The original String
	 * @param wordsOnly controls whether to return only words or any String
	 * @return list of Strings which are nearby the original string
	 */
	public List<String> distanceOne(String s, boolean wordsOnly )  {
		   List<String> retList = new ArrayList<String>();
		   substitution(s, retList, wordsOnly);
		   insertions(s, retList, wordsOnly);
		   deletions(s, retList, wordsOnly);
		   return retList;
	}

	
	/** Add to the currentList Strings that are one character mutation away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void substitution(String s, List<String> currentList, boolean wordsOnly) {
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				StringBuffer sb = new StringBuffer(s);
				sb.setCharAt(index, (char)charCode);

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}
	
	/** Add to the currentList Strings that are one character insertion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void insertions(String s, List<String> currentList, boolean wordsOnly ) {
		s = s.toLowerCase();
		for(int i=0; i<s.length(); ++i) {
			for(int c=97; c<123; ++c) {
				String word = s.substring(0, i) + (char) c + s.substring(i);
				if(!wordsOnly || dict.isWord(word)) {
					currentList.add(word);
				}
			}
		}

		for(int c=97; c<123; ++c) {
			String word = s + (char) c;
			if(!wordsOnly || dict.isWord(word)) {
				currentList.add(word);
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void deletions(String s, List<String> currentList, boolean wordsOnly ) {
		s = s.toLowerCase();
		for(int i=1; i<s.length(); ++i) {
			String word = s.substring(0, i-1) + s.substring(i);
			if(!wordsOnly || dict.isWord(word)) {
				currentList.add(word);
			}
		}

		if(s.length() > 1) {
			String word = s.substring(0, s.length()-1);
			if(!wordsOnly || dict.isWord(word)) {
				currentList.add(word);
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param word The misspelled word
	 * @param numSuggestions is the maximum number of suggestions to return 
	 * @return the list of spelling suggestions
	 */
	@Override
	public List<String> suggestions(String word, int numSuggestions) {

		// initial variables
		List<String> queue = new LinkedList<String>();     // String to explore
		HashSet<String> visited = new HashSet<String>();   // to avoid exploring the same  
														   // string multiple times
		List<String> retList = new LinkedList<String>();   // words to return
		 
		
		// insert first node
		queue.add(word);
		visited.add(word);

		for(int i=0; i<THRESHOLD; ++i) {
			if (retList.size() >= numSuggestions || queue.size() == 0) {
				break;
			}

			String s = queue.remove(0);
			if(dict.isWord(s)) {
				retList.add(s);
			}

			List<String> toCheck = new ArrayList<>();
			substitution(s, toCheck, true);
			insertions(s, toCheck, true);
			deletions(s, toCheck, true);

			for(String toAdd: toCheck) {
				if(!visited.contains(toAdd)) {
					queue.add(toAdd);
					visited.add(toAdd);
				}
			}
		}

		return retList;

	}	

   public static void main(String[] args) {
	   Dictionary d = new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   
	   String word = "ii";
	   List<String> l = w.distanceOne(word, false);
	   System.out.println("One away word Strings for for \""+word+"\" are:");
	   System.out.println(l.size());
	   System.out.println(l+"\n");

	   word = "swone";
	   List<String> suggest = w.suggestions(word, 10);
	   System.out.println("Spelling Suggestions for \""+word+"\" are:");
	   System.out.println(suggest.size());
	   System.out.println(suggest);
   }

}
