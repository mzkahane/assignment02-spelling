package assignment02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Spelling extends Trie {
	private String[] types = new String[333333];
	private long[] tokens = new long[333333];
	Trie Dictionary;
	
	/**
	 * Reads the CSV into two Arrays, one with the types and another with the tokens
	 * @throws FileNotFoundException = file not found
	 */
	private void readList() throws FileNotFoundException {
		String[] line = new  String[2];
		Scanner scan = new Scanner(new File("unigram_freq.csv"));
		
		scan.nextLine();
		int i = 0;
		while (scan.hasNext()) {
			line = scan.nextLine().split(",");
			types[i] = line[0];
			tokens[i] = Long.parseLong(line[1]);
			++i;
		}
		scan.close();
	}
	
	/**
	 * Populates the Trie from a list of words
	 * @param types = Array of words to use to populate the trie
	 * @param tokens = Array of the frequency of each word to be stored in the trie
	 * @return Trie populated with every word from the inputed types array 
	 */
	private Trie populateTrie(String[] types, long[] tokens) {
		//for each type, insert it into the trie and set its token.
		Trie dictionary = new Trie();
		
		for (int i = 0; i < types.length; i++) {
			dictionary.insert(types[i], tokens[i]);
		}
		
		return dictionary;
	}
	/**
	 * Suggests words from a given, possibly misspelled/incomplete, word
	 * @param token - the word given that may or may not be spelled right.
	 * @param count - the amount of suggested words to be output.
	 * @return an Array of suggested words given the prefix from the token. 
	 * @throws Exception if the input is not lower case, or if the file is not found.
	 */
	public List<List<String>> suggest (String token, int count) throws Exception{
		List<List<String>> output = new ArrayList<List<String>>();
		try {
			readList();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Dictionary = populateTrie(types, tokens);
		
		// Initializing variables
		String stem = "";
		List<String> suggestRow = new ArrayList<String>(count);
		for (int i = 0; i < count; i++) {
			suggestRow.add(null);
		}
		
		for (char letter : token.toCharArray()) {
			if (letter < 'a') {
				throw new Exception("Word must be lower case");
			}
			// add the letters to a growing stem, run search on each stem, add that set to the output
			stem += letter;
			ArrayList<String> possibilities = Dictionary.search(stem);
			
			String[] suggestions = mostFrequent(possibilities, count);
 
			for (int i = 0; i < count; i++) {
				if (suggestions[i] != null) {
					if (!suggestRow.contains(suggestions[i])) {
						suggestRow.set(i, suggestions[i]);
					}
				}
			}
			List<String> temp = new ArrayList<String>(suggestRow);
			output.add(temp);
		}
		return output;
	}

	/**
	 * Finds the most frequently used words of the possibilities given
	 * @param possibilities = ArrayList of the words to consider
	 * @param count = the amount of most frequent words that should be returned
	 * @return Array of the (count) most frequent words
	 */
	private String[] mostFrequent(ArrayList<String> possibilities, int count) {
		String[] words = new String[possibilities.size()]; 
		possibilities.toArray(words);
		long[] frequencies = new long[words.length];
		int f = 0;
		
		for (String type : words) {
			TrieNode word = Dictionary.find(type);
			frequencies[f++] = word.freq;
		}
		
		// Sorts both arrays in parallel based on the frequencies array
		for (int i = 1; i < frequencies.length; i++) {
			long temp = frequencies[i];
			String temp2 = words[i];
			int j = i-1;
			while (j >= 0 && frequencies[j] < temp) {
				frequencies[j+1] = frequencies[j];
				words[j+1] = words[j];
				--j;
			}
			frequencies[j+1] = temp;
			words[j+1] = temp2;
		}
		
		// gets the first (count) words and returns them in an array
		String[] suggestions = new String[count];
		
		if (words.length >= count) {
			for (int i = 0; i < count; i++) {
				suggestions[i] = words[i];
			}
		} else { // if there is less than (count) words, only add up to the end of the words array
			for (int i = 0; i < words.length; i++) {
				suggestions[i] = words[i];
			}
		}
		return suggestions;
	}
	
	public static void main(String[] args) throws Exception {
		Spelling s = new Spelling();
		List<List<String>> suggestions = new ArrayList<List<String>>();
		
		suggestions = s.suggest("onomatopoeia", 5);
		
		for (List<String> list : suggestions) {
			System.out.println(list);
		}
	}
}
