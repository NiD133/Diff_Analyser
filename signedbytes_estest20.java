package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test class for {@link SignedBytes}.
 * The original test class name "SignedBytes_ESTestTest20" and its scaffolding
 * are artifacts of a test generation tool. A more descriptive name like
 * "SignedBytesTest" is better for a human-written test suite.
 */
public class SignedBytesTest {

    /**
     * Tests that the join method correctly concatenates an array of zero-valued bytes
     * with a given separator.
     */
    @Test
    public void join_withArrayOfZeros_shouldReturnZerosSeparatedByDelimiter() {
        // Arrange
        // Using an explicit array initialization is clearer than `new byte[3]`,
        // which relies on the reader knowing the default value for a byte is 0.
        byte[] bytes = {0, 0, 0};
        String separator = ",";
        String expected = "0,0,0";

        // Act
        String actual = SignedBytes.join(separator, bytes);

        // Assert
        // The assertion is now much easier to read and verify at a glance
        // due to the simpler separator and explicit expected value.
        assertEquals(expected, actual);
    }
}