package org.apache.commons.codec.language;

import org.junit.Test;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Verifies that the constructor throws a NullPointerException when initialized with a null char array mapping.
     * The constructor is expected to fail because it attempts to clone the provided array.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullCharArrayShouldThrowNullPointerException() {
        new RefinedSoundex((char[]) null);
    }
}