package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.Collection;

/**
 * Contains tests for the {@link CollectionBag} class.
 */
public class CollectionBagTest {

    /**
     * Tests that calling retainAll() with a null collection throws a NullPointerException,
     * as required by the java.util.Collection interface contract.
     */
    @Test(expected = NullPointerException.class)
    public void retainAll_withNullCollection_throwsNullPointerException() {
        // Arrange: Create an empty CollectionBag. The specific type of the decorated
        // bag does not matter for this test, so a simple HashBag is used.
        final Bag<String> decorated = new HashBag<>();
        final Collection<String> bag = new CollectionBag<>(decorated);

        // Act: Attempt to retain a null collection.
        bag.retainAll(null);

        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
    }
}