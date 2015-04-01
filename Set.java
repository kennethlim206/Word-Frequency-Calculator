import java.util.Iterator;

/** An ADT that represents a set: a bunch of items that must be distinct from
 * each other, stored with no defined order.
 * @author Jadrian Miles
 * 
 * @param <T> The type of item to be stored in the set.
 */
public interface Set<T> extends Iterable<T> {
    
    /** Adds the given item to the set, ignoring duplicates. */
    public void add(T item);
    
    /** Removes the given item.
     * @return False if the item wasn't in the set before.
     */
    public boolean remove(T item);
    
    /** Checks whether the given item is in the set. */
    public boolean contains(T item);
    
    /** Returns the number of items in the set.
     * @return the number of items in the set
     */
    public int size();
    
    /** Returns whether the set is empty. */
    public boolean isEmpty();
    
    /**  Removes all items from the set. */
    public void clear();
    
    /** Creates a new Set and returns a union of this set and otherSet.
    * @param otherSet A set to combine with this set.
    * @return The union of the two sets.
    */
    public Set<T> union(Set<T> otherSet);
    
    /** Creates a new Set and returns an intersections of this set and otherSet.
    * @param otherSet A set to combine with this set.
    * @return The intersection of the two sets.
    */
    public Set<T> intersect(Set<T> otherSet);
    
    /** Returns an iterator over the items in the set (which will access the
     * items in some arbitrary order).
     */
    public Iterator<T> iterator();
    
    /** Returns an array containing the same contents as this set, in an
     * arbitrary order.  Note that, for technical reasons, the type of the items
     * contained in the set can't be communicated properly to the caller, so an
     * array of Objects gets returned.
     * @return an array of length size(), with the same items in it as are
     *         stored in the set.
     */
    public Object[] toArray();
}
