package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that calling uniqueSet() on a transformed bag that decorates an
     * empty bag results in an empty set.
     */
    @Test
    public void uniqueSetOnEmptyBagShouldReturnEmptySet() {
        // Arrange: Create an empty bag decorated by a TransformedBag.
        Bag<Object> sourceBag = new HashBag<>();
        
        // The specific transformer doesn't matter since the bag is empty and the
        // transform method will never be called. We use a simple no-op transformer for clarity.
        Transformer<Object, Object> noOpTransformer = input -> input;
        Bag<Object> transformedBag = TransformedBag.transformingBag(sourceBag, noOpTransformer);

        // Act: Get the unique set from the transformed bag.
        Set<Object> uniqueSet = transformedBag.uniqueSet();

        // Assert: The resulting set should be non-null and empty.
        assertNotNull("The unique set should not be null.", uniqueSet);
        assertTrue("The unique set from an empty bag should be empty.", uniqueSet.isEmpty());
    }
}