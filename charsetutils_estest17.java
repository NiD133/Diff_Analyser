package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.delete() returns an empty string when the input
     * string is empty and the set of characters to delete is also empty.
     * This verifies the behavior for a specific edge case documented in the method's Javadoc.
     */
    @Test
    public void testDelete_fromEmptyStringWithEmptySet_returnsEmptyString() {
        // Arrange: Define the inputs for the test case.
        final String inputString = "";
        final String[] emptyCharSet = new String[0];

        // Act: Call the method under test.
        final String result = CharSetUtils.delete(inputString, emptyCharSet);

        // Assert: Verify the result is as expected.
        assertEquals("Deleting from an empty string should always yield an empty string", "", result);
    }
}