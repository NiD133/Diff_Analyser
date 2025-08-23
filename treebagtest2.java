package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.Test;

public class TreeBagTestTest2<T> extends AbstractSortedBagTest<T> {

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
    void testCollections555() {
        final Bag<Object> bag = new TreeBag<>();
        assertThrows(NullPointerException.class, () -> bag.add(null));
        final Bag<String> bag2 = new TreeBag<>(String::compareTo);
        // jdk bug: adding null to an empty TreeMap works
        // thus ensure that the bag is not empty before adding null
        bag2.add("a");
        assertThrows(NullPointerException.class, () -> bag2.add(null));
    }
}
