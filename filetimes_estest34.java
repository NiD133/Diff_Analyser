package org.apache.commons.io.file.attribute;

import org.junit.Test;
import java.nio.file.attribute.FileTime;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@code minusNanos()} throws a NullPointerException when the
     * input FileTime is null.
     */
    @Test(expected = NullPointerException.class)
    public void minusNanos_shouldThrowNullPointerException_whenFileTimeIsNull() {
        // The value of the second argument (nanosToSubtract) is irrelevant,
        // as the null check on the first argument should cause the exception.
        FileTimes.minusNanos(null, 0L);
    }
}