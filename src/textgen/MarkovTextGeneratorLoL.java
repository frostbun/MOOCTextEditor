package textgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team 
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText)
	{
		if(sourceText.equals("")) {
			return;
		}

		String prevWord = "";
		for(String word: sourceText.split(" +")) {
			boolean exist = false;
			for(ListNode node: wordList) {
				if(node.getWord().equals(prevWord)) {
					node.addNextWord(word);
					exist = true;
				}
			}

			if(!exist) {
				ListNode newNode = new ListNode(prevWord);
				wordList.add(newNode);
				newNode.addNextWord(word);
			}

			if(word.length() > 0 && Character.isUpperCase(word.charAt(0)) && !prevWord.equals("")) {
				wordList.get(0).addNextWord(word);
			}

			prevWord = word;
		}
		wordList.add(new ListNode(prevWord));
	}
	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
		if(wordList.size() == 0) {
			return "";
		}

		String text = "";
		String prevWord = starter;
		for(int i=0; i<numWords; ++i) {

			String word = wordList.get(0).getRandomNextWord(rnGenerator);

			for(ListNode node: wordList) {
				if(node.getWord().equals(prevWord) && node.nextWords.size() != 0) {
					word = node.getRandomNextWord(rnGenerator);
				}
			}

			text = text + word + " ";
			prevWord = word;
		}
		return text;
	}
	
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText)
	{
		wordList.clear();
		this.train(sourceText);
	}	
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		// feed the generator a fixed random value for repeatable behavior
		// MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		// String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		// System.out.println(textString);
		// gen.train(textString);
		// System.out.println(gen);
		// System.out.println(gen.generateText(20));
		// String textString2 = "You say yes, I say no, "+
		// 		"You say stop, and I say go, go, go, "+
		// 		"Oh no. You say goodbye and I say hello, hello, hello, "+
		// 		"I don't know why you say goodbye, I say hello, hello, hello, "+
		// 		"I don't know why you say goodbye, I say hello. "+
		// 		"I say high, you say low, "+
		// 		"You say why, and I say I don't know. "+
		// 		"Oh no. "+
		// 		"You say goodbye and I say hello, hello, hello. "+
		// 		"I don't know why you say goodbye, I say hello, hello, hello, "+
		// 		"I don't know why you say goodbye, I say hello. "+
		// 		"Why, why, why, why, why, why, "+
		// 		"Do you say goodbye. "+
		// 		"Oh no. "+
		// 		"You say goodbye and I say hello, hello, hello. "+
		// 		"I don't know why you say goodbye, I say hello, hello, hello, "+
		// 		"I don't know why you say goodbye, I say hello. "+
		// 		"You say yes, I say no, "+
		// 		"You say stop and I say go, go, go. "+
		// 		"Oh, oh no. "+
		// 		"You say goodbye and I say hello, hello, hello. "+
		// 		"I don't know why you say goodbye, I say hello, hello, hello, "+
		// 		"I don't know why you say goodbye, I say hello, hello, hello, "+
		// 		"I don't know why you say goodbye, I say hello, hello, hello,";
		// System.out.println(textString2);
		// gen.retrain(textString2);
		// System.out.println(gen);
		// System.out.println(gen.generateText(20));

		MarkovTextGeneratorLoL tester = new MarkovTextGeneratorLoL(new Random());
		tester.train("");
		tester.train("Source Ma Text.");
		System.out.println(tester);
		System.out.println(tester.generateText(10));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator)
	{
	    return nextWords.get(generator.nextInt(nextWords.size()));
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
}


