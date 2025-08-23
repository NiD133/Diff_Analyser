package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

public class ZippingIteratorTest {

    // Helper to collect all remaining elements from an iterator
    private static <T> List<T> toList(Iterator<T> it) {
        List<T> out = new ArrayList<>();
        while (it.hasNext()) {
            out.add(it.next());
        }
        return out;
    }

    @Test
    public void interleavesTwoIterators_untilBothAreExhausted() {
        // Arrange
        List<Integer> a = Arrays.asList(1, 3, 5);
        List<Integer> b = Arrays.asList(2, 4);
        ZippingIterator<Integer> zip = new ZippingIterator<>(a.iterator(), b.iterator());

        // Act
        List<Integer> result = toList(zip);

        // Assert
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    public void interleavesThreeIterators_skippingExhaustedOnes() {
        // Arrange
        List<Integer> a = Collections.singletonList(1);
        List<Integer> b = Arrays.asList(2, 4);
        List<Integer> c = Collections.singletonList(3);
        ZippingIterator<Integer> zip = new ZippingIterator<>(a.iterator(), b.iterator(), c.iterator());

        // Act
        List<Integer> result = toList(zip);

        // Assert
        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }

    @Test
    public void hasNext_isFalseWhenAllChildIteratorsAreEmpty() {
        // Arrange
        ZippingIterator<Integer> zip = new ZippingIterator<>(Collections.<Integer>emptyIterator(),
                                                             Collections.<Integer>emptyIterator());

        // Act/Assert
        assertFalse(zip.hasNext());
    }

    @Test
    public void next_onEmptyZippingIterator_throwsNoSuchElementException() {
        // Arrange
        ZippingIterator<Integer> zip = new ZippingIterator<>(Collections.<Integer>emptyIterator(),
                                                             Collections.<Integer>emptyIterator());

        // Act/Assert
        assertThrows(NoSuchElementException.class, zip::next);
    }

    @Test
    public void remove_beforeAnyNext_throwsIllegalStateException() {
        // Arrange
        ZippingIterator<Integer> zip = new ZippingIterator<>(Collections.<Integer>emptyIterator(),
                                                             Collections.<Integer>emptyIterator());

        // Act/Assert
        assertThrows(IllegalStateException.class, zip::remove);
    }

    @Test
    public void remove_afterNext_delegatesToChildIterator() {
        // Arrange
        LinkedList<Integer> list = new LinkedList<>(Collections.singletonList(42));
        ZippingIterator<Integer> zip = new ZippingIterator<>(list.iterator(), Collections.<Integer>emptyIterator());

        // Act
        assertEquals(Integer.valueOf(42), zip.next());
        zip.remove();

        // Assert
        assertTrue("Underlying collection should be updated by remove()", list.isEmpty());
    }

    @Test
    public void remove_afterExternalModification_throwsConcurrentModificationException() {
        // Arrange
        LinkedList<Integer> list = new LinkedList<>(Collections.singletonList(7));
        ZippingIterator<Integer> zip = new ZippingIterator<>(list.iterator(), Collections.<Integer>emptyIterator());

        // Act: consume one element via the iterator, then modify the list externally
        assertEquals(Integer.valueOf(7), zip.next());
        list.removeFirst(); // structural modification outside the iterator

        // Assert: remove() should now fail fast
        assertThrows(ConcurrentModificationException.class, zip::remove);
    }

    @Test
    public void next_failsFastWhenUnderlyingIteratorDetectsConcurrentModification() {
        // Arrange
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> it = list.listIterator(); // iterator created before modification
        list.add(1); // structural modification after iterator creation
        ZippingIterator<Integer> zip = new ZippingIterator<>(it, it);

        // Act/Assert
        assertThrows(ConcurrentModificationException.class, zip::next);
    }

    @Test
    public void supportsNullElements() {
        // Arrange
        List<Integer> withNull = Collections.singletonList(null);
        ZippingIterator<Integer> zip = new ZippingIterator<>(withNull.iterator(),
                                                             Collections.<Integer>emptyIterator());

        // Act/Assert
        assertNull(zip.next());
        assertFalse(zip.hasNext());
    }

    @Test
    public void constructor_varargsRejectsNullIterators() {
        // Arrange
        @SuppressWarnings("unchecked")
        Iterator<Integer>[] iterators = new Iterator[] { Collections.emptyIterator(), null };

        // Act
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new ZippingIterator<>(iterators));

        // Assert
        assertEquals("iterator", npe.getMessage());
    }

    @Test
    public void constructor_twoArgsRejectsNullIterator() {
        // Act
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new ZippingIterator<Integer>(null, Collections.<Integer>emptyIterator()));

        // Assert
        assertEquals("iterator", npe.getMessage());
    }

    @Test
    public void constructor_threeArgsRejectsNullIterator() {
        // Act
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new ZippingIterator<Integer>(Collections.<Integer>emptyIterator(),
                                                   null,
                                                   Collections.<Integer>emptyIterator()));

        // Assert
        assertEquals("iterator", npe.getMessage());
    }
}