package org.apache.commons.collections4.bag;

import org.junit.Test;

import java.util.Collections;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the constructor of {@link TreeBag} to ensure it handles
 * invalid element types correctly.
 */
public class TreeBagConstructorTest {

    /**
     * A simple helper class that does not implement {@link Comparable},
     * used to test the behavior of collections that require comparable elements.
     */
    private static class NonComparableObject {
        // The existence of this class is sufficient for the test.
    }

    /**
     * Verifies that the TreeBag constructor throws an IllegalArgumentException
     * when initialized with a collection containing non-Comparable elements.
     * A default TreeBag uses natural ordering and thus requires its elements
     * to be Comparable.
     */
    @Test
    public void constructorWithNonComparableElementCollectionShouldThrowException() {
        // Arrange: Create a collection with an element that does not implement Comparable.
        final Collection<NonComparableObject> nonComparableCollection =
                Collections.singletonList(new NonComparableObject());

        // Act & Assert: Attempt to create the TreeBag and verify the expected exception.
        try {
            new TreeBag<>(nonComparableCollection);
            fail("Expected an IllegalArgumentException because the element is not Comparable.");
        } catch (final IllegalArgumentException e) {
            // Check that the exception message clearly indicates the cause of the failure.
            final String expectedMessage = "does not implement Comparable";
            assertTrue(
                "Exception message should explain that the element is not comparable. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessage)
            );
        }
    }
}