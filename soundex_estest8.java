package org.apache.commons.codec.language;

import org.junit.Test;

/**
 * Tests for the {@link Soundex} class, focusing on constructor behavior.
 */
public class SoundexTest {

    /**
     * Verifies that the Soundex constructor throws a NullPointerException
     * when initialized with a null character array for the mapping.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullMapping() {
        // This call is expected to throw a NullPointerException because the mapping array is null.
        new Soundex((char[]) null);
    }
}