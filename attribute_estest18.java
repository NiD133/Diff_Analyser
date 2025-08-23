package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the static helper methods in the {@link Attribute} class.
 */
public class AttributeStaticTest {

    /**
     * Verifies that calling {@code Attribute.isDataAttribute(null)} throws a
     * NullPointerException. This confirms the method's behavior with invalid input,
     * as it does not perform an explicit null check.
     */
    @Test(expected = NullPointerException.class)
    public void isDataAttributeThrowsNullPointerExceptionForNullInput() {
        // The static isDataAttribute method is expected to throw a NullPointerException
        // when called with a null key, as it directly attempts to operate on the input.
        Attribute.isDataAttribute(null);
    }
}