import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.text.DecimalFormat;

/**
  * WordFreqs
  * Justin Lim
  * 06/01/14 (Modified 06/06/14)
  * This program stores the frequency in which words are used in a text file given
  * by the command line and then prints the 20 most common words. (This program uses
  * a list implemented by MysteryListImplementation.)
  */
  
public class WordFreqs {
	
	/** Class that stores the key and value of a specific data entry. */
	private class KVPair<K, V> implements DictPair<K, V> {
		private K key;
		private V value;
		
		public KVPair() {
			key = null;
			value = null;
		}
		
		public KVPair(K key_value, V data_value) {
			key = key_value;
			value = data_value;
		}
		
		public K getKey() {
			return key;
		}
    	public V getValue() {
    		return value;
    	}
	}
	
	/** Takes a dictionary of words and number of times each word is used in a text file
	  * and prints the 20 most frequently used words.
	  */
	public void printTopTwenty(Dictionary<String, Integer> d, int n) {
		Dictionary<String, Integer> found_words = d;
		
		/** Creates a set of DictPairs for all the entries in the dictionary. */
		Set<DictPair<String, Integer>> unsorted_set = found_words.getEntrySet();
		
		/** Converts set of DictPairs to array of DictPairs. */
		@SuppressWarnings("unchecked")
		Object[] unsorted_array = (Object[]) new Object[unsorted_set.size()];
		Iterator<DictPair<String, Integer>> unsorted_iterator = unsorted_set.iterator();
		int j = 0;
		while (unsorted_iterator.hasNext()) {
			unsorted_array[j] = unsorted_iterator.next();
			j++;
		}
		
		/** Creates empty array to store sorted entries. */
		@SuppressWarnings("unchecked")
		Object[] sorted_array = (Object[]) new Object[unsorted_array.length];
		
		/** Runs insertion sort on unsorted array return sorted array. */
		sorted_array = insertionSort(unsorted_array, 1, unsorted_array.length);
		
		/** Creates empty array to store 20 most frequently used words. */
		int size_of_final = 0;
		if (sorted_array.length < 20) {
			size_of_final = sorted_array.length;
		} else { 
			size_of_final = 20;
		}
		@SuppressWarnings("unchecked")
		Object[] top_20_array = (Object[]) new Object[size_of_final];
		
		/** Adds 20 most frequent entries to array. */
		if (sorted_array.length < 20) {
			int counter_1 = 0;
			for (int i = (sorted_array.length - 1); i >= 0 ; i--) {
				top_20_array[counter_1] = sorted_array[i];
				counter_1++;
			}
		} else {
			int counter_2 = 0;
			for (int i = (sorted_array.length - 1); i > (sorted_array.length - 21); i--) {
				top_20_array[counter_2] = sorted_array[i];
				counter_2++;
			}
		}
		
		/** Prints the 20 most frequently used words and normalized frequencies. */
		int k = 1;
		for (int i = 0; i < top_20_array.length; i++) {
			@SuppressWarnings("unchecked")
			DictPair<String, Integer> sorted_pair =
			(DictPair<String, Integer>) top_20_array[i];
			String sorted_key = sorted_pair.getKey();
			double sorted_value = sorted_pair.getValue() / (double) n;
    		DecimalFormat second_place = new DecimalFormat("0.00");
    		double print_value = Double.parseDouble(second_place.format(sorted_value));
			System.out.println
			(k + ". " + sorted_key + " - " + print_value);
			k++;
		}
	}			
	
	/** sorts an array of DictPairs by numerical values. */
	private Object[] insertionSort(Object[] a, int first, int last) {
		for (int i = first; i < last; i++) {
			@SuppressWarnings("unchecked")
			DictPair<String, Integer> next_pair = (DictPair<String, Integer>) a[i];
			insertInOrder(next_pair, a, first, i - 1);
		}
		return a;
	}
	private void insertInOrder(DictPair<String, Integer> entry,
							   Object[] array, int begin, int end) {
		boolean sorted = false;
		int index = end;
		while (!sorted) {
			@SuppressWarnings("unchecked")
			DictPair<String, Integer> end_pair = (DictPair<String, Integer>) array[index];
			if ((index >= begin) && (entry.getValue() < end_pair.getValue())) {
				array[index + 1] = array[index];
				index--;
			} else {
				array[index + 1] = entry;
				sorted = true;
			}
		}
	}
	
	/** Takes a text file and prints the 20 most frequently used words from that text
	  * file.
	  */
	 public static void main(String[] args) {
	 	Dictionary<String, Integer> words = new HashDictionaryImplementation<String, Integer>();
		ArrayList<String> words_list = new ArrayList<String>();
		
		String get_words = args[0];
		File input_words = new File(get_words);
		
		Scanner scanner = null;
		try {
            scanner = new Scanner(input_words);
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found");
            System.exit(1);
        } while (scanner.hasNext()) {
        	/** Gets rid of punctuation and puts words from text file
        	  * into a list.
        	  */
        	String word = scanner.next();
        	if (word.contains("'")) {
        		word = word.replace("'", " ");
        	}
        	if (word.contains("-")) {
        		word = word.replace("-", " ");
        	}
        	word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        	words_list.add(word);
        }
        
        /** Checks if text file is empty. */
        if (words_list.isEmpty()) {
        	System.out.println("File contains no text");
        	System.exit(0);
        }
        
        /** Separates words with apostrophes into two separate words. */
		for (int i = 0; i < words_list.size(); i++) {
			if (words_list.get(i).contains(" ")) {
				String[] split_words = words_list.get(i).split(" ");
				String word_1 = null;
				String word_2 = null;
				words_list.remove(i);
				/** Checks if there are letters on both sides of the split. */
				if (split_words.length > 0) {
					word_1 = split_words[0];
					if (!word_1.equals("") && !word_1.equals(" ")) {
						words_list.add(word_1);
					}
				}
				if (split_words.length > 1) {
					word_2 = split_words[1];
					if (!word_2.equals("") && !word_2.equals(" ")) {
						words_list.add(word_2);
					}
				}
			}
		}
		
		/** Adds all words from list to dictionary. */
        for (int i = 0; i < words_list.size(); i++) {
        	String word = words_list.get(i);
			if (!words.contains(word)) {
				words.add(word, 1);
			} else {
				int counter = words.getValue(word);
				counter++;
				words.add(word, counter);
			}
		}
        WordFreqs frequency = new WordFreqs();
        frequency.printTopTwenty(words, words_list.size());
    }
}
