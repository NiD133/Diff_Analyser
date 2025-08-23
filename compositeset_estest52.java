package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that the contains() method returns false when checking for the
     * presence of one of its component sets as an element. A CompositeSet
     * contains the *elements* of its component sets, not the component sets
     * themselves.
     */
    @Test
    public void containsShouldReturnFalseWhenCheckingForComponentSetInstance() {
        // Arrange
        final Set<Integer> componentSet = new LinkedHashSet<>();
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Sanity check to ensure the initial state is as expected.
        assertTrue("The composite set should be empty upon creation.", compositeSet.isEmpty());

        // Act
        // Check if the composite set contains the component set instance itself as an element.
        final boolean result = compositeSet.contains(componentSet);

        // Assert
        assertFalse("A composite set should not contain its component set as an element.", result);
    }
}