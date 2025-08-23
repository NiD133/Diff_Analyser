package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling removeComposited with a null argument correctly removes
     * the first null set from the composition, while leaving other sets untouched.
     */
    @Test
    public void removeCompositedShouldRemoveFirstNullSetWhenPresent() {
        // Arrange
        final Set<Integer> nonNullSet = new HashSet<>();
        nonNullSet.add(1);

        // Create a CompositeSet containing a non-null set and two null sets.
        // The varargs constructor is a clear way to add initial sets.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(nonNullSet, null, null);

        assertEquals("Pre-condition: CompositeSet should contain three sets", 3, compositeSet.getSets().size());

        // Act
        // Attempt to remove one of the null sets from the composition.
        compositeSet.removeComposited(null);

        // Assert
        // The size should decrease by one, and the remaining sets should be the expected ones.
        assertEquals("Post-condition: CompositeSet should contain two sets", 2, compositeSet.getSets().size());
        assertTrue("The non-null set should remain in the composition", compositeSet.getSets().contains(nonNullSet));
        assertTrue("One null set should still remain in the composition", compositeSet.getSets().contains(null));
    }
}