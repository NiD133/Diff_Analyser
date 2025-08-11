import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.CollectionBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionBagTest {

    @Test
    public void add_addsCopiesAndReturnsTrue() {
        Bag<String> backing = new HashBag<>();
        CollectionBag<String> bag = new CollectionBag<>(backing);

        assertTrue(bag.add("a"));
        assertEquals(1, bag.getCount("a"));
        assertEquals(1, bag.size());

        assertTrue(bag.add("a", 3));
        assertEquals(4, bag.getCount("a"));
        assertEquals(4, bag.size());
    }

    @Test
    public void containsAll_ignoresCardinality() {
        Bag<String> backing = new HashBag<>();
        CollectionBag<String> bag = new CollectionBag<>(backing);
        bag.add("a", 2); // 2 copies of "a", no "b"

        // Same element repeated in the input collection should still be "contained"
        assertTrue(bag.containsAll(Arrays.asList("a", "a")));

        // Missing element makes it false even if others are present
        assertFalse(bag.containsAll(Arrays.asList("a", "b")));
    }

    @Test
    public void remove_removesSingleOccurrence() {
        Bag<String> backing = new HashBag<>();
        CollectionBag<String> bag = new CollectionBag<>(backing);
        bag.add("x", 2);
        bag.add("y", 1);

        assertTrue(bag.remove("x"));
        assertEquals(1, bag.getCount("x"));
        assertEquals(2, bag.size());

        assertTrue(bag.remove("x"));
        assertEquals(0, bag.getCount("x"));
        assertEquals(1, bag.size());

        // No more "x" left
        assertFalse(bag.remove("x"));
        assertEquals(0, bag.getCount("x"));
        assertEquals(1, bag.size());
    }

    @Test
    public void removeAll_removesAllCopiesOfElementsInCollection() {
        Bag<String> backing = new HashBag<>();
        CollectionBag<String> bag = new CollectionBag<>(backing);
        bag.add("a", 2);
        bag.add("b", 1);
        bag.add("c", 3);

        boolean changed = bag.removeAll(Arrays.asList("a", "c", "z")); // "z" is not present
        assertTrue(changed);

        assertEquals(0, bag.getCount("a"));
        assertEquals(0, bag.getCount("c"));
        assertEquals(1, bag.getCount("b"));
        assertEquals(1, bag.size());
    }

    @Test
    public void retainAll_keepsOnlyElementsPresentInCollection_countsUnchanged() {
        Bag<String> backing = new HashBag<>();
        CollectionBag<String> bag = new CollectionBag<>(backing);
        bag.add("a", 2);
        bag.add("b", 1);
        bag.add("c", 3);

        boolean changed = bag.retainAll(new HashSet<>(Arrays.asList("b", "c")));
        assertTrue(changed);

        assertEquals(0, bag.getCount("a"));
        assertEquals(1, bag.getCount("b"));
        assertEquals(3, bag.getCount("c"));
        assertEquals(4, bag.size());
    }

    @Test
    public void addAll_addsAllElementsFromCollectionIncludingDuplicates() {
        Bag<String> backing = new HashBag<>();
        CollectionBag<String> bag = new CollectionBag<>(backing);
        List<String> src = Arrays.asList("a", "a", "b");

        boolean changed = bag.addAll(src);
        assertTrue(changed);

        assertEquals(2, bag.getCount("a"));
        assertEquals(1, bag.getCount("b"));
        assertEquals(3, bag.size());
    }

    @Test
    public void containsAll_withEmptyCollection_returnsTrue() {
        Bag<Integer> backing = new HashBag<>();
        CollectionBag<Integer> bag = new CollectionBag<>(backing);

        assertTrue(bag.containsAll(Arrays.asList()));
    }

    @Test
    public void operationsOnUnmodifiableDelegate_throwUnsupportedOperationException() {
        Bag<String> delegate = UnmodifiableBag.unmodifiableBag(new HashBag<>());
        CollectionBag<String> bag = new CollectionBag<>(delegate);

        assertThrows(UnsupportedOperationException.class, () -> bag.add("x"));
        assertThrows(UnsupportedOperationException.class, () -> bag.add("x", 2));
        assertThrows(UnsupportedOperationException.class, () -> bag.addAll(Arrays.asList("a", "b")));
        assertThrows(UnsupportedOperationException.class, () -> bag.remove("a"));
        assertThrows(UnsupportedOperationException.class, () -> bag.removeAll(Arrays.asList("a", "b")));
        assertThrows(UnsupportedOperationException.class, () -> bag.retainAll(Arrays.asList("a", "b")));
    }

    @Test
    public void constructor_nullBag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CollectionBag<String>(null));
    }

    @Test
    public void staticFactory_nullBag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> CollectionBag.collectionBag(null));
    }
}