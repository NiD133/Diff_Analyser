package com.google.common.collect;

import static org.junit.Assert.assertNotNull;

import java.util.Spliterator;
import org.junit.Test;

/**
 * Unit tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    @Test
    public void spliterator_onEmptySet_shouldReturnNonNull() {
        // Arrange: Create an empty CompactLinkedHashSet.
        // Using create() is more direct for this purpose than specifying an initial capacity.
        CompactLinkedHashSet<Integer> emptySet = CompactLinkedHashSet.create();

        // Act: Get the spliterator from the empty set.
        Spliterator<Integer> spliterator = emptySet.spliterator();

        // Assert: Verify that the returned spliterator is not null.
        assertNotNull("spliterator() on an empty set should never return null.", spliterator);
    }
}