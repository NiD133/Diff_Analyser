package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling toSet() on a CompositeSet initialized with a single
     * empty set results in a new, empty set.
     */
    @Test
    public void testToSetOnCompositeWithEmptyComponentSetReturnsEmptySet() {
        // Arrange: Create a CompositeSet containing one empty set.
        final Set<Integer> componentSet = new HashSet<>();
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Act: Call the toSet() method.
        final Set<Integer> resultSet = compositeSet.toSet();

        // Assert: Verify the returned set is a new, empty set.
        assertNotNull("The returned set should not be null.", resultSet);
        assertTrue("The returned set should be empty.", resultSet.isEmpty());
        assertNotSame("The returned set should be a new instance, not the component set.", componentSet, resultSet);
    }
}