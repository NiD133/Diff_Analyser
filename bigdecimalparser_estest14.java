package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link BigDecimalParser} focusing on its handling of dependencies.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that parsing a number with a length of 500 characters or more
     * attempts to delegate to the 'fastdoubleparser' library.
     * <p>
     * If this optional dependency is not available on the classpath, the test
     * confirms that a {@link NoClassDefFoundError} is thrown, as expected.
     */
    @Test
    public void parseLargeNumber_whenFastParserDependencyIsMissing_shouldThrowNoClassDefFoundError() {
        // Arrange: Define an input that is long enough (>= 500 chars) to trigger the fast parser path.
        // The actual content of the char array is irrelevant for this test, as the class loading
        // error occurs before the array is accessed.
        final char[] longInputChars = new char[0];
        final int offset = 0;
        final int length = 500; // The threshold for using the fast parser

        // Act & Assert
        try {
            BigDecimalParser.parse(longInputChars, offset, length);
            fail("Expected a NoClassDefFoundError because the fast parser dependency is missing.");
        } catch (NoClassDefFoundError e) {
            // Assert that the error is caused by the specific missing class.
            String expectedMissingClass = "ch/randelshofer/fastdoubleparser/JavaBigDecimalParser";
            assertEquals("The error message should indicate the missing fast parser class.",
                expectedMissingClass, e.getMessage());
        }
    }
}