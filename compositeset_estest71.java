package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CompositeSet}.
 * This class focuses on a specific test case for the addComposited method.
 */
public class CompositeSetTest {

    /**
     * Tests that adding the same empty set instance to a CompositeSet multiple times
     * is a valid operation and does not trigger a collision exception.
     *
     * The addComposited method checks for intersections between the set being added
     * and existing sets. An intersection with an empty set is always empty, so no
     * collision should be detected.
     */
    @Test
    public void testAddCompositedWithSameEmptySetDoesNotCauseCollision() {
        // Arrange: Create an empty set and a CompositeSet containing it.
        final Set<Object> emptySet = new LinkedHashSet<>();
        final CompositeSet<Object> compositeSet = new CompositeSet<>(emptySet);

        // Sanity check: The composite set initially contains one set and is empty.
        assertEquals("The composite set should initially contain one set.", 1, compositeSet.getSets().size());
        assertTrue("The composite set should initially be empty.", compositeSet.isEmpty());

        // Act: Add the same empty set instance again.
        compositeSet.addComposited(emptySet);

        // Assert: Verify the state of the CompositeSet and the original set.
        // The composite set should now contain two references to the same empty set.
        assertEquals("The composite set should now contain two sets.", 2, compositeSet.getSets().size());

        // The total size of the composite set should remain 0.
        assertTrue("The composite set should still be empty after the operation.", compositeSet.isEmpty());

        // The original set should, of course, remain unchanged.
        assertTrue("The original set should not be modified.", emptySet.isEmpty());
    }
}