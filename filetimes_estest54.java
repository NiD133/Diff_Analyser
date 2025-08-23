package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

/**
 * Tests for edge cases in the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that calling {@link FileTimes#minusSeconds(FileTime, long)} with {@link Long#MIN_VALUE}
     * throws an {@link ArithmeticException}.
     * <p>
     * The subtraction operation internally requires negating the number of seconds.
     * In two's complement arithmetic, the negation of {@code Long.MIN_VALUE} results in a value
     * that is too large to be represented as a {@code long}, causing an overflow.
     * </p>
     */
    @Test
    public void minusSecondsWithLongMinValueThrowsArithmeticException() {
        // Arrange: Get the current time and the value that will cause an overflow.
        final FileTime now = FileTimes.now();
        final long secondsToSubtract = Long.MIN_VALUE;

        // Act & Assert: Verify that an ArithmeticException is thrown.
        // The lambda expression ensures the exception is caught from the specific method call.
        final ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            FileTimes.minusSeconds(now, secondsToSubtract);
        });

        // Further assert that the exception message indicates the expected cause.
        assertTrue("The exception message should indicate an overflow.",
            exception.getMessage().contains("long overflow"));
    }
}