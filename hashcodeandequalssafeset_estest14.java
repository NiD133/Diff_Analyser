package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies that the static factory method `of(Iterable)` correctly creates a set
     * and that the `contains()` method can successfully find an element that was used
     * for its creation.
     */
    @Test
    public void shouldContainElementWhenCreatedFromIterable() {
        // Arrange: Create an element and a collection containing it.
        Object element = new Object();
        List<Object> initialElements = List.of(element);

        // Act: Create a HashCodeAndEqualsSafeSet from the collection.
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(initialElements);

        // Assert: Verify that the set contains the original element.
        assertTrue("The set should contain the element it was initialized with.", safeSet.contains(element));
    }
}