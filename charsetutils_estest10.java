package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Test suite for the {@link CharSetUtils} class.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.squeeze() returns null when the input string is null,
     * regardless of the character set provided. This aligns with the method's Javadoc.
     */
    @Test
    public void squeezeWithNullStringShouldReturnNull() {
        // Arrange: A null input string and a non-null character set.
        // The content of the set should not affect the outcome.
        final String[] anyCharSet = {"a", "b", "c"};

        // Act: Call the squeeze method with the null string.
        final String result = CharSetUtils.squeeze(null, anyCharSet);

        // Assert: The result should be null, as specified in the contract.
        assertNull("Squeezing a null string should return null.", result);
    }
}