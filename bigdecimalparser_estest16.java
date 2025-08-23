package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link BigDecimalParser}.
 */
public class BigDecimalParserTest {

    /**
     * Tests that calling {@code parseWithFastParser} throws a {@link NoClassDefFoundError}
     * if its required dependency, the 'fastdoubleparser' library, is not available on the classpath.
     * <p>
     * This test simulates a missing dependency scenario. The error is expected to be
     * thrown upon the first attempt to use the {@code JavaBigDecimalParser} class from that library.
     */
    @Test
    public void parseWithFastParserShouldThrowErrorWhenDependencyIsMissing() {
        // Arrange: Define input arguments. The specific values for offset and length are not
        // critical, as the class loading error should occur before they are processed.
        char[] inputChars = new char[10];
        int offset = 0;
        int length = -1; // Invalid length

        // Act & Assert
        try {
            BigDecimalParser.parseWithFastParser(inputChars, offset, length);
            fail("Expected a NoClassDefFoundError to be thrown due to the missing 'fastdoubleparser' dependency.");
        } catch (NoClassDefFoundError e) {
            // Assert: Verify that the error is for the expected missing class.
            String expectedMissingClass = "ch/randelshofer/fastdoubleparser/JavaBigDecimalParser";
            String errorMessage = e.getMessage();
            
            assertTrue(
                "The error message should indicate the missing JavaBigDecimalParser class. Got: " + errorMessage,
                errorMessage != null && errorMessage.contains(expectedMissingClass)
            );
        } catch (Throwable t) {
            // Fail the test if any other unexpected exception or error is thrown.
            fail("Caught an unexpected throwable: " + t);
        }
    }
}