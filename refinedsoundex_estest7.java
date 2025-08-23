package org.apache.commons.codec.language;

import org.junit.Test;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the constructor throws a NullPointerException when the provided
     * mapping string is null. The RefinedSoundex algorithm requires a valid
     * character mapping to be initialized.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullStringShouldThrowNullPointerException() {
        // Act: Attempt to create an instance with a null mapping string.
        // Assert: A NullPointerException is expected, as declared by the @Test annotation.
        new RefinedSoundex((String) null);
    }
}