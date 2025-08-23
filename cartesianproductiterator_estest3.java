package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link CartesianProductIterator}.
 */
public class CartesianProductIterator_ESTestTest3 {

    @Test
    public void testConstructorShouldThrowNPEWhenAnIterableIsNull() {
        // Arrange: Create an array of iterables that contains a null element.
        final Iterable<?>[] iterablesWithNull = { null };

        // Act & Assert: The constructor should reject null iterables.
        try {
            new CartesianProductIterator<>(iterablesWithNull);
            fail("Expected a NullPointerException to be thrown for a null iterable.");
        } catch (final NullPointerException e) {
            // The source code uses Objects.requireNonNull(iterable, "iterable"),
            // so we expect the message to be "iterable".
            assertEquals("iterable", e.getMessage());
        }
    }
}