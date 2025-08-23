package org.apache.commons.collections4.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

/**
 * Improved, understandable tests for the CompositeSet class.
 *
 * The original test (from CompositeSet_ESTestTest23) was automatically generated
 * and combined multiple concerns, making it difficult to comprehend. These tests
 * are broken down into specific, focused behaviors for clarity and maintainability.
 */
public class CompositeSetUnderstandableTest {

    /**
     * Tests that the constructor accepting a null array of sets correctly
     * initializes an empty CompositeSet.
     */
    @Test
    public void constructorWithNullArrayCreatesEmptySet() {
        // Arrange & Act
        final CompositeSet<String> compositeSet = new CompositeSet<>((Set<String>[]) null);

        // Assert
        assertNotNull(compositeSet);
        assertTrue("Set created with a null array of sets should be empty", compositeSet.isEmpty());
    }

    /**
     * Verifies that a newly created, empty CompositeSet behaves correctly
     * according to the Set contract for empty sets.
     */
    @Test
    public void emptyCompositeSetBehavesAsExpected() {
        // Arrange
        final CompositeSet<String> emptySet = new CompositeSet<>();
        final Collection<String> emptyCollection = Collections.emptyList();

        // Assert basic properties of an empty set
        assertTrue("A new CompositeSet should be empty", emptySet.isEmpty());
        assertEquals("An empty CompositeSet should have a size of 0", 0, emptySet.size());
        assertEquals("An empty CompositeSet should have a hash code of 0", 0, emptySet.hashCode());

        // Assert equals contract
        assertEquals("Two empty CompositeSets should be equal", new CompositeSet<String>(), emptySet);
        assertEquals("Empty CompositeSets with different generic types should also be equal", new CompositeSet<Object>(), emptySet);

        // Act: Perform an operation that should not change the set
        final boolean changed = emptySet.retainAll(emptyCollection);

        // Assert
        assertFalse("retainAll with an empty collection should not report a change", changed);
        assertTrue("The set should remain empty after the retainAll operation", emptySet.isEmpty());
    }

    /**
     * Tests that calling addAll() on a CompositeSet without a configured
     * SetMutator throws an UnsupportedOperationException, as per the class documentation.
     */
    @Test
    public void addAllShouldThrowUnsupportedOperationExceptionWhenNoMutatorIsSet() {
        // Arrange
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        final Collection<Integer> collectionToAdd = Collections.singletonList(1);
        final String expectedMessage = "addAll() is not supported on CompositeSet without a SetMutator strategy";

        // Act & Assert
        try {
            compositeSet.addAll(collectionToAdd);
            fail("Expected an UnsupportedOperationException to be thrown");
        } catch (final UnsupportedOperationException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}