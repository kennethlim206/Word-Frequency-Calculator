import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
  * HashDictionaryImplementation
  * Justin Lim
  * 06/01/14 (Modified 06/06/14)
  * This program contains the constructor and methods of a Hash Dictionary Implementation.
  */
  
public class HashDictionaryImplementation<K, V> implements Dictionary<K, V> {
	private Object[] hashTable;
	private int num_entries;
	private int table_size;
	private static final int DEFAULT_SIZE = 97;
	/** Augmented with a final entry computed by Wolfram Alpha. */
	private static final int[] PRIMES = {
    13, 23, 53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593, 49157,
    98317, 196613, 393241, 786433, 1572869, 3145739, 6291469, 12582917,
    25165843, 50331653, 100663319, 201326611, 402653189, 805306457,
    1610612741 };
	private static final double MAX_LOAD_FACTOR = 0.5;
	
	public HashDictionaryImplementation() {
		this(DEFAULT_SIZE);
	}
	
	public HashDictionaryImplementation(int prime_size) {
		num_entries = 0;
		table_size = prime_size;
		@SuppressWarnings("unchecked")
  		Object[] tmp = (Object[]) new Object[prime_size];
		hashTable = tmp;
	}
	
	/** Node class that keeps track of both the key and the value of a data entry
	  * and the node connected to it if there are multiple entries with the same
	  * key.
	  */	
	private class Node {
		public K key;
		public V value;
		public Node next_node;
		
		public Node() {
			key = null;
			value = null;
			next_node = null;
		}
		public Node(K key_value, V data_value) {
			key = key_value;
			value = data_value;
			next_node = null;
		}
		
		private V getValue() {
			return value;
		}
		private void setValue(V new_value) {
			value = new_value;
		}
		
		private K getKey() {
			return key;
		}
		private void setKey(K new_key) {
			key = new_key;
		}
		
		private boolean hasNext() {
			if (next_node != null) {
				return true;
			} else {
				return false;
			}
		}
		private Node getNext() {
			return next_node;
		}
		private void setNext(Node next) {
			next_node = next;
		}
	}
	
	/** Set class that uses the Java HashSet Class. Some methods are not contained
	  * in HashSet so exceptions are thrown for them in the JavaSetWrapper class.
	  */
	private class JavaSetWrapper<T> implements Set<T> {
		private HashSet<T> set;
		
		public JavaSetWrapper() {
			set = new HashSet<T>();
		}
		
		public void add(T item) {
			set.add(item);
		}
		public boolean remove(T item) {
			return set.remove(item);
		}
		public boolean contains(T item) {
			return set.contains(item);
		}
		public int size() {
			return set.size();
		}
		public boolean isEmpty() {
			return set.isEmpty();
		}
		public void clear() {
			set.clear();
		}
		public Set<T> union(Set<T> otherSet) {
			throw new UnsupportedOperationException();
		}
		public Set<T> intersect(Set<T> otherSet) {
			throw new UnsupportedOperationException();
		}
		public Iterator<T> iterator() {
			return set.iterator();
		}
		public Object[] toArray() {
			throw new UnsupportedOperationException();
		}
	}
	
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
	
	/** A method that takes a key and returns the hashCode() index for that key. */
	private int getHashCode(K key) {
		int hash_code = Math.abs(key.hashCode() % table_size);
		return hash_code;
	}
	
	/** Approximately doubles the size of the hashTable to the nearest prime number
	  * when the Low Load Factor gets larger than a specified value (0.5 in this case).
	  */
	private void rehash() {
		Object[] oldTable = hashTable;
		int old_size = num_entries;
		int new_size = old_size;
		int index = 0;
		while (new_size <= (2 * old_size)) {
			if (new_size < PRIMES[index]) {
				new_size = PRIMES[index];
			}
			index++;
		}
		
		@SuppressWarnings("unchecked")
  		Object[] tmp = (Object[]) new Object[new_size];
		hashTable = tmp;
		num_entries = 0;
		table_size = new_size;
		
		for (int i = 0; i < oldTable.length; i++) {
			if (oldTable[i] != null) {
				@SuppressWarnings("unchecked")
       			Node current_node = (Node) oldTable[i];
       			add(current_node.getKey(), current_node.getValue());
       		}
       	}
	}
	
	/** Checks the size of the hashTable against the number of entries every time a
	  * new entry is made to ensure that the size of the hashTable remains at an
	  * appropriately larger size than the number of entries in the table.
	  */
	private void ensureLowLoadFactor() {
		int load_factor = num_entries/table_size;
		if (load_factor > MAX_LOAD_FACTOR) {
			rehash();
		}
	}
	
	 /** Adds a new entry to this dictionary. If the given key already exists in
     * the dictionary, replaces the corresponding value.
     * @return Either null if the new entry was added to the dictionary
     *         or the value that was associated with key if that value
     *         was replaced.
     */
    public V add(K key, V value) {
    	ensureLowLoadFactor();
    	V return_value = null;
    	boolean successful = false;
    	int index = getHashCode(key);
    	@SuppressWarnings("unchecked")
        Node current_node = (Node) hashTable[index];
    	
    	if (hashTable[index] == null) {
    		/** If spot is available. Add entry to spot. */
    		hashTable[index] = new Node(key, value);
    		num_entries++;
    	} else if (key.equals(current_node.getKey())) {
    		if (!value.equals(current_node.getValue())) {
    			/** If keys are the same, but values are not. Replace old value
    			  * with new value.
    			  */
				return_value = current_node.getValue();
				current_node.setValue(value);
			} else {
				/** If keys are the same and values are the same. Do nothing
				  * and set return_value to existing entry value.
				  */
				return_value = current_node.getValue();
			}
		} else {
			/** If keys are not the same but indexes are the same. Create new node
			  * and check if the current node already has a node connected to it.
			  */
			@SuppressWarnings("unchecked")
			Node new_node = new Node(key, value);
			
			while (!successful) {
				/** If the current node doesn't have another node connected
				  * to it, then connect the new node to the current node.
				  */
				if (current_node.getNext() == null) {
					current_node.setNext(new_node);
					num_entries++;
					successful = true;
				} else {
					/** Else the current node has a node connected to it.
					  * If the node connected to the current node has the
					  * same value as the value given by the user, then do nothing
					  * and set the return value to the value given by the user.
					  */
					if (current_node.getNext().getValue().equals(value)) {
						return_value = new_node.getValue();
						successful = true;
					/** Else we have to check the next node. Set the next node to the
					  * current node and start over.
					  */
					} else {
						current_node = current_node.getNext();
					}
				}
			}		
		}
		return return_value;
    }
    
    /** Removes a specific entry from this dictionary.
     * @param key The key of the entry to be removed.
     * @return Either the value that was associated with the key,
     *         or null if the key was not in the dictionary.
     */
    public V remove(K key) {
    	V return_value = null;
    	int index = getHashCode(key);
    	if (hashTable[index] != null) {
    		@SuppressWarnings("unchecked")
    		Node current_node = (Node) hashTable[index];
    		
    		if (key.equals(current_node.getKey())) {
    			/** If the key of the intended removal node is the same as the
    			  * key of the node at the proper index then remove that entry and 
    			  * move the next node to the current_node position if it exists.
    			  */
    			return_value = current_node.getValue();
    			if (current_node.getNext() == null) {
    				hashTable[index] = null;
    			} else {
    				current_node = current_node.getNext();
    			}
				num_entries--;
			} else {
				/** If the key of the intended removal node is not the same as
				  * as the key of the node at the proper index then go to the next
				  * node and check the key of that node.
				  */
				while (current_node.hasNext()) {
					Node prev_node = current_node;
					current_node = current_node.getNext();
					if (key.equals(current_node.getKey())) {
						return_value = current_node.getValue();
						Node temp_node = current_node.getNext();
						current_node = null;
						prev_node.setNext(temp_node);
						num_entries--;
					}
				}
			}
    	}
    	return return_value;
    }
    
    /** Retrieves from this dictionary the value associated with a given key.
     * @return Either the value that is associated with the search key
     *         or null if no such object exists.
     */
    public V getValue(K key) {
    	V return_value = null;
    	int index = getHashCode(key);
    	if (hashTable[index] != null) {
			@SuppressWarnings("unchecked")
			Node current_node = (Node) hashTable[index];
			if (key.equals(current_node.getKey())) {
				return_value = current_node.getValue();
			} else {
				while (current_node.hasNext()) {
					current_node = current_node.getNext();
					if (key.equals(current_node.getKey())) {
						return_value = current_node.getValue();
					}
				}
			}
		}
    	return return_value;
    }
    
    /** Sees whether a specific entry is in this dictionary.
     * @return true If key is associated with an entry in the dictionary.
     */
    public boolean contains(K key) {
    	boolean contain_value = false; 
    	int index = getHashCode(key);
    	if (hashTable[index] != null) {
    		@SuppressWarnings("unchecked")
    		Node current_node = (Node) hashTable[index];
    		if (key.equals(current_node.getKey())) {
    			/** If the key at the indicated index is the same as the entry at
    			  * that index, then return true.
    			  */
    			contain_value = true;
    		} else {
    			/** If the key at the indicated index is different from the entry at
    			  * that index, then check if the entry has a next entry and check
    			  * that entry.
    			  */
    			while (current_node.hasNext()) {
    				current_node = current_node.getNext();
    				if (key.equals(current_node.getKey())) {
    					contain_value = true;
    				}
    			}
    		}
    	}
    	return contain_value;
    }
    
    /** Returns a Set that contains all the keys stored in the dictionary. */
    public Set<K> getKeySet() {
    	Set<K> keys = new JavaSetWrapper<K>();
    	for (int index = 0; index < table_size; index++) {
    		if (hashTable[index] != null) {
    			@SuppressWarnings("unchecked")
    			Node current_node = (Node) hashTable[index];
    			keys.add(current_node.getKey());
    		}
    	}
    	return keys;
    }
    
    /** Returns a Set of the key-value KVPair stored in the dictionary. */
    public Set<DictPair<K, V>> getEntrySet() {
    	Set<DictPair<K, V>> pair_set = new JavaSetWrapper<DictPair<K, V>>();
    	for (int index = 0; index < table_size; index++) {
    		if (hashTable[index] != null) {
    			@SuppressWarnings("unchecked")
    			Node current_node = (Node) hashTable[index];
    			DictPair<K, V> new_pair = new KVPair<K, V>(current_node.getKey(), current_node.getValue());
    			pair_set.add(new_pair);
    		}
    	}
    	return pair_set;	
    }
    
    /** Gets the size of this dictionary. */
    public int size() {
    	return num_entries;
    }
    
    /** Sees whether this dictionary is empty. */
    public boolean isEmpty() {
    	if (num_entries == 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /** Removes all entries from this dictionary. */
    public void clear() {
    	num_entries = 0;
    	for (int i = 0; i < table_size; i++) {
    		hashTable[i] = null;
    	}
    }
    
    /** (Tests are commented out). Tests the different methods in the HashDictionaryImplementation. Print statements
      * are commented out, and comments give correct returns statements to compare to
      * actual returns statements.
      */
    public static void main(String[] args) {
    	Dictionary<String, String> names =
    	new HashDictionaryImplementation<String, String>();
    	
    	/** Checks the rehashing method as well as the adding, removing and size methods.
    	  */
    	names.add("James", "Lebron");
    	names.add("Wade", "Dwayne");
    	names.add("Bosh", "Chris");
    	names.add("Chalmers", "Mario");
    	names.add("Allen", "Ray");
    	names.add("Battier", "Shane");
    	names.add("Anderson", "Chris");
    	names.add("Cole", "Norris");
    	names.add("Oden", "Greg");
    	names.add("Beasley", "Michael");
    	names.add("Jones", "James");
    	names.add("Haslem", "Udonis");
    	names.add("Douglas", "Tony");
    	names.add("Howard", "Jowan");
    	if (names.size() == 14) {
    		System.out.println
    		("1. add() and size() methods work correctly: 14 items added.");
    	} else {
    		System.out.println
    		("1. add() and size() methods not working correctly: 14 items should have been added.");
    	}
    	names.add("Peter", "Cottontail");
    	if (names.size() == 15) {
    		System.out.println
    		("2. add() and size() methods work correctly: 1 items added.");
    	} else {
    		System.out.println
    		("2. add() and size() methods not working correctly: 1 items should have been added.");
    	}
    	names.add("James", "Michelle");
    	if (names.size() == 15 && names.getValue("James").equals("Michelle")) {
    		System.out.println
    		("3. add() and size() methods work correctly: James value replaced.");
    	} else {
    		System.out.println
    		("3. add() and size() methods not working correctly: James value should have been replaced.");
    	}
    	names.remove("James");
    	if ((names.size() == 14) && !names.contains("James")) {
    		System.out.println
    		("4. remove() method work correctly: 'James' removed.");
    	} else {
    		System.out.println
    		("4. remove() method not working correctly: 'James' should have been removed.");
    	}
    	
    	/** Checks the getValue() and contains() methods. */
    	if (names.getValue("James") == null) {
    		System.out.println
    		("5. getValue() method works correctly: getValue('James') is no longer in dictionary.");
    	} else {
    		System.out.println
    		("5. getValue() not working correctly: getValue('James') should return null.");
    	}
    	if (names.contains("Bosh")) {
    		System.out.println
    		("6. contains() method works correctly: Dictionary contains key 'Bosh'.");
    	} else {
    		System.out.println
    		("6. contains() not working correctly: Dictionary should contain key 'Bosh'.");
    	}
    	
    	/** Checks the getKeySet() method. */
    	Set<String> names_set = names.getKeySet();
    	if(names_set.contains("Wade")) {
    		System.out.println
    		("7. getKeySet() and contains() method work correctly: 'Wade' in set.");
    	} else {
    		System.out.println
    		("7. getKeySet() or contains() method not working correctly: 'Wade' should be in set.");
    	}
    	if(!names_set.contains("James")) {
    		System.out.println
    		("8. getKeySet() and contains() method work correctly: 'James' not in set.");
    	} else {
    		System.out.println
    		("8. getKeySet() or contains() method not working correctly: 'James' should not be in set");
    	}
    	
    	/** Checks the getEntrySet() method. */
    	Set entry_set = names.getEntrySet();
    	if (entry_set.size() == 14 && !entry_set.isEmpty()) {
    		System.out.println
    		("9. getEntrySet() method works: size of Entry Set is correct.");
    	} else {
    		System.out.println
    		("9. getEntrySet() method doesn't works: size of Entry Set is incorrect.");
    	}
    	
    	/** Checks the clear and isEmpty() methods. */
    	names.clear();
    	if (names.isEmpty() && !names.contains("Chalmers")) {
    		System.out.println
    		("10. clear() and isEmpty() methods work: Dictionary is empty and doesn't contain 'Chalmers'.");
    	} else {
    		System.out.println
    		("10. clear() or isEmpty() methods does not work.");
    	}
    	System.out.println("\n");
    }
}
