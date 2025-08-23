package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that toSet() returns a new Set containing all elements from the
     * single underlying set, including null.
     */
    @Test
    public void toSetShouldReturnSetWithAllElementsFromSingleCompositedSet() {
        // Arrange: Create a source set with a non-null and a null element.
        Set<Integer> sourceSet = new HashSet<>();
        sourceSet.add(3205);
        sourceSet.add(null);

        // Create a CompositeSet containing the single source set.
        CompositeSet<Integer> compositeSet = new CompositeSet<>(sourceSet);

        // Act: Convert the CompositeSet back to a standard Set.
        Set<Integer> resultSet = compositeSet.toSet();

        // Assert: The resulting set should be equal to the original source set.
        // assertEquals for sets correctly compares size and content.
        assertEquals(sourceSet, resultSet);
    }
}