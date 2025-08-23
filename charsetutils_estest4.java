package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.delete() returns null when the input string is null.
     * The content of the character set should not affect the outcome.
     *
     * This corresponds to the Javadoc documentation:
     * <pre>
     * CharSetUtils.delete(null, *) = null
     * </pre>
     */
    @Test
    public void testDeleteWithNullStringShouldReturnNull() {
        // Arrange: A non-empty set of characters to delete.
        // The behavior should be the same regardless of the set provided.
        final String[] charSet = {"a", "b"};

        // Act: Call delete with a null input string.
        final String result = CharSetUtils.delete(null, charSet);

        // Assert: The result must be null, as specified by the method's contract.
        assertNull("Deleting from a null string should return null.", result);
    }
}