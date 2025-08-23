package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.Test;

public class TreeBagTestTest3<T> extends AbstractSortedBagTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public SortedBag<T> makeObject() {
        return new TreeBag<>();
    }

    @SuppressWarnings("unchecked")
    public SortedBag<T> setupBag() {
        final SortedBag<T> bag = makeObject();
        bag.add((T) "C");
        bag.add((T) "A");
        bag.add((T) "B");
        bag.add((T) "D");
        return bag;
    }

    @Test
    void testOrdering() {
        final Bag<T> bag = setupBag();
        assertEquals("A", bag.toArray()[0], "Should get elements in correct order");
        assertEquals("B", bag.toArray()[1], "Should get elements in correct order");
        assertEquals("C", bag.toArray()[2], "Should get elements in correct order");
        assertEquals("A", ((SortedBag<T>) bag).first(), "Should get first key");
        assertEquals("D", ((SortedBag<T>) bag).last(), "Should get last key");
    }
}
