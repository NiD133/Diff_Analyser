package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Collections;

/**
 * This class contains tests for the CompactLinkedHashSet class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class CompactLinkedHashSet_ESTestTest10 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Tests that creating a CompactLinkedHashSet from an empty collection
     * results in a new, empty set.
     */
    @Test
    public void createFromEmptyCollection_shouldReturnEmptySet() {
        // Arrange: Create an empty source collection.
        Collection<String> sourceCollection = Collections.emptyList();

        // Act: Create a new CompactLinkedHashSet from the empty collection.
        CompactLinkedHashSet<String> resultSet = CompactLinkedHashSet.create(sourceCollection);

        // Assert: The resulting set should be empty.
        assertTrue("A set created from an empty collection should be empty.", resultSet.isEmpty());
    }
}