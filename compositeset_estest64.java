package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CompositeSet} focusing on handling of self-referential component sets.
 */
public class CompositeSetTest {

    /**
     * Tests that removing a self-referential component set from another composite set
     * results in an empty set, which is correctly considered equal to the (also empty)
     * self-referential set.
     */
    @Test
    public void testRemoveCompositedWithSelfReferentialSet() {
        // Arrange: Create a composite set that contains itself as a component.
        // This is an unusual structure designed to test edge-case handling.
        @SuppressWarnings("unchecked")
        final Set<Integer>[] componentHolder = new Set[1];
        final CompositeSet<Integer> selfReferentialSet = new CompositeSet<>(componentHolder);
        componentHolder[0] = selfReferentialSet; // Now the set contains itself.

        // Create the main composite set under test, which will contain the self-referential set.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(selfReferentialSet);

        // Act: Remove the self-referential set from the main composite set.
        compositeSet.removeComposited(selfReferentialSet);

        // Assert: The main composite set should now be empty.
        assertTrue("The composite set should be empty after its only component is removed.",
                   compositeSet.isEmpty());
        assertEquals("The size of the composite set should be 0.", 0, compositeSet.size());

        // A self-referential set with no actual elements is also considered empty.
        // Since both sets are empty, they must be equal according to the Set contract.
        assertEquals("An empty set should be equal to the empty self-referential set.",
                     selfReferentialSet, compositeSet);
    }
}