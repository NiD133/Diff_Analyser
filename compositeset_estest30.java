package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link CompositeSet#isEmpty()} method.
 */
public class CompositeSet_ESTestTest30 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that isEmpty() returns false for a CompositeSet that is not empty.
     */
    @Test
    public void isEmptyShouldReturnFalseForNonEmptyCompositeSet() {
        // Arrange: Create a CompositeSet containing a non-empty component set.
        Set<Integer> componentSet = new HashSet<>();
        componentSet.add(-6);

        CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Act: Call the method under test.
        boolean isEmpty = compositeSet.isEmpty();

        // Assert: Verify that the CompositeSet is correctly identified as not empty.
        assertFalse("A CompositeSet containing a non-empty set should not be empty", isEmpty);
    }
}