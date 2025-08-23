package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A test suite for the Seconds class.
 */
public class SecondsTest {

    /**
     * Verifies that adding two Seconds instances whose sum would exceed
     * Integer.MAX_VALUE correctly throws an ArithmeticException.
     */
    @Test
    public void plus_whenResultOverflows_throwsArithmeticException() {
        // Arrange: Define the test inputs. We use Seconds.MAX_VALUE to test the overflow boundary.
        Seconds maxSeconds = Seconds.MAX_VALUE;

        // Act & Assert: Attempt the operation and verify the expected exception.
        try {
            // This operation should cause an integer overflow.
            maxSeconds.plus(maxSeconds);
            
            // If we reach this line, the test fails because no exception was thrown.
            fail("Expected an ArithmeticException to be thrown for integer overflow.");
        } catch (ArithmeticException e) {
            // Verify that the exception is the one we expect and that its message
            // clearly indicates an overflow, confirming the cause of the failure.
            String expectedMessage = "The calculation caused an overflow: 2147483647 + 2147483647";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}