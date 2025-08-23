package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that toArray() correctly returns an array of all elements
     * when the CompositeSet is backed by a single, non-empty set.
     */
    @Test
    public void toArray_withSingleUnderlyingSet_returnsArrayWithAllElements() {
        // Arrange
        final String element = "test-element";
        final Set<String> underlyingSet = new HashSet<>();
        underlyingSet.add(element);

        final CompositeSet<String> compositeSet = new CompositeSet<>(underlyingSet);
        assertFalse("CompositeSet should not be empty after initialization", compositeSet.isEmpty());
        assertEquals("CompositeSet should have the correct size", 1, compositeSet.size());

        final Object[] expectedArray = { element };

        // Act
        final Object[] actualArray = compositeSet.toArray();

        // Assert
        assertArrayEquals("toArray() should return an array with the expected element", expectedArray, actualArray);
    }
}