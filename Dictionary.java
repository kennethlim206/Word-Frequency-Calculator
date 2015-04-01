/** 
 * An interface for the Dictionary ADT.
 * @author Jadrian Miles
 * 
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface Dictionary<K, V> {
    /** Adds a new entry to this dictionary. If the given key already exists in
     * the dictionary, replaces the corresponding value.
     * @return Either null if the new entry was added to the dictionary
     *         or the value that was associated with key if that value
     *         was replaced.
     */
    public V add(K key, V value);
    
    /** Removes a specific entry from this dictionary.
     * @param key The key of the entry to be removed.
     * @return Either the value that was associated with the key,
     *         or null if the key was not in the dictionary.
     */
    public V remove(K key);
    
    /** Retrieves from this dictionary the value associated with a given key.
     * @return Either the value that is associated with the search key
     *         or null if no such object exists.
     */
    public V getValue(K key);
    
    /** Sees whether a specific entry is in this dictionary.
     * @return true If key is associated with an entry in the dictionary.
     */
    public boolean contains(K key);
    
    /** Returns a Set that contains all the keys stored in the dictionary. */
    public Set<K> getKeySet();
    
    /** Returns a Set of the key-value pairs stored in the dictionary. */
    public Set<DictPair<K, V>> getEntrySet();
    
    /** Gets the size of this dictionary. */
    public int size();
    
    /** Sees whether this dictionary is empty. */
    public boolean isEmpty();
    
    /** Removes all entries from this dictionary. */
    public void clear();
}
