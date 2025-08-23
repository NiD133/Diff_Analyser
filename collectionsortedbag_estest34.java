package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Tests for the constructor of {@link CollectionSortedBag}.
 */
public class CollectionSortedBagConstructorTest {

    /**
     * Verifies that the constructor throws a NullPointerException
     * when the decorated bag provided is null. This is a critical
     * contract of the decorator pattern.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionWhenBagIsNull() {
        // The constructor is expected to perform a null check on its argument.
        // We use assertThrows to clearly express that we expect an exception.
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> new CollectionSortedBag<Object>((SortedBag<Object>) null)
        );

        // Further verify the exception message to ensure the correct validation failed.
        // The underlying implementation uses Objects.requireNonNull(collection, "collection"),
        // so we expect the message to be "collection".
        assertEquals("collection", exception.getMessage());
    }
}