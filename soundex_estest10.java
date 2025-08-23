package org.apache.commons.codec.language;

import org.junit.Test;

/**
 * Unit tests for the {@link Soundex} class, focusing on constructor behavior.
 */
public class SoundexTest {

    /**
     * Tests that the Soundex(String) constructor throws a NullPointerException
     * when initialized with a null mapping string. The mapping is a critical
     * component, and a null value is an invalid state.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenConstructedWithNullMappingString() {
        // Attempt to create a Soundex instance with a null mapping string.
        // The @Test(expected) annotation asserts that this action must throw a NullPointerException.
        new Soundex((String) null);
    }
}