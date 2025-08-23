package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for exception handling within the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that isBaseNamedEntity() throws a NullPointerException when
     * the input name is null. This confirms that the method correctly
     * rejects invalid null input.
     */
    @Test(expected = NullPointerException.class)
    public void isBaseNamedEntityShouldThrowNullPointerExceptionForNullInput() {
        // The method is expected to throw a NullPointerException as it does not
        // perform an explicit null check before using the input argument.
        Entities.isBaseNamedEntity(null);
    }
}