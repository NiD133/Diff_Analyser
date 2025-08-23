package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CompositeSet} class.
 * This test focuses on the behavior of the toSet() method.
 */
// The original test class name is kept for context, but a more descriptive name
// like CompositeSetTest would be better in a real-world scenario.
public class CompositeSet_ESTestTest91 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling toSet() on an empty CompositeSet returns a new, empty Set.
     */
    @Test
    public void toSetOnEmptyCompositeSetShouldReturnEmptySet() {
        // Arrange: Create an empty CompositeSet.
        CompositeSet<Object> compositeSet = new CompositeSet<>();

        // Act: Convert the CompositeSet to a standard Set.
        Set<Object> resultSet = compositeSet.toSet();

        // Assert: The resulting set should be non-null and empty.
        assertNotNull("The returned set should not be null", resultSet);
        assertTrue("The returned set should be empty", resultSet.isEmpty());
    }
}