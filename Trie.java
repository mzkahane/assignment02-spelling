package assignment02;

import java.util.ArrayList;

public class Trie {
	
	class TrieNode {
		String word;
		ArrayList<TrieNode> children = null;
		long freq;
		
		TrieNode(){
			word = null;
			freq = 0;
			children = new	ArrayList<TrieNode>();
			
			for (int i = 0; i < 26; i++) {
				children.add(null);
			}
		}
	}
	
	TrieNode root = new TrieNode();
	
	/**
	 * Inserts a given word and its frequency(tokens) into the Trie
	 * @param key = the word to insert
	 * @param tokens = frequency of the word, stored in the Trie node with the last letter of the key/word
	 */
	public void insert(String key, long tokens) {
		TrieNode current = root;
		
		for (char letter : key.toCharArray()) {
			if (current.children.get(letter - 'a') == null) {
				current.children.set(letter - 'a', new TrieNode());
			}
			current = current.children.get(letter - 'a');
		}
		current.word = key;
		current.freq = tokens;
	}
	
	/**
	 * Searches for all possible words to suggest based on the type given
	 * @param type = word to start the search from
	 * @return ArrayList with all of the possible suggestions
	 */
	public ArrayList<String> search(String type) {
		ArrayList<String> output = new ArrayList<String>();
		
		TrieNode current = root;
		
		for (char letter : type.toCharArray()) {
			if (current.children.get(letter - 'a') != null) { //get to the end of the input. Assuming the whole input exists.
				current = current.children.get(letter - 'a');
			}
		}
		output = DFS(current);	
		return output;
	}
	
	/**
	 * DFS helper, finds the words for search function
	 * @param node = Trie node to start the search from
	 * @return ArrayList of possible suggestions
	 */
	private ArrayList<String> DFS(TrieNode node){
		ArrayList<String> output = new ArrayList<String>();
		int pos = 0;
		DFS(node, output, pos);
		
		return output;
	}
	
	/*
	 * DFS actual
	 */
	private void DFS(TrieNode node, ArrayList<String> output, int pos) {
		for (int i = node.children.size()-1; i >=0; i--) {
			if (node.children.get(i) != null) {
				DFS(node.children.get(i), output, pos);
			}
		}
		if(node.word != null) {
			output.add(node.word);
		}
	}
	
	public TrieNode find(String type) {
		TrieNode current = root;
		
		for (char letter : type.toCharArray()) {
			if (current.children.get(letter - 'a') != null) {
				current = current.children.get(letter - 'a');
			}
		}
		return current;
	}
}
