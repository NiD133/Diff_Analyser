package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertThrows;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Unit tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that the CollectionBag constructor throws a NullPointerException
     * when the decorated bag is null, as per its contract.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionWhenBagIsNull() {
        // The constructor is documented to throw a NullPointerException if the decorated bag is null.
        // This test confirms that behavior.
        assertThrows(NullPointerException.class, () -> new CollectionBag<Object>((Bag<Object>) null));
    }
}