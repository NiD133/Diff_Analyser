package org.apache.commons.codec.language;

import org.junit.Test;

/**
 * Tests for the {@link Soundex} class, focusing on constructor behavior with invalid arguments.
 */
public class SoundexTest {

    /**
     * Verifies that the {@link Soundex#Soundex(String, boolean)} constructor
     * throws a {@code NullPointerException} when the mapping string is null.
     * The mapping string is a mandatory parameter for the algorithm's initialization.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullMappingStringShouldThrowNullPointerException() {
        // Attempt to create a Soundex instance with a null mapping string,
        // which is expected to fail immediately.
        new Soundex((String) null, false);
    }
}