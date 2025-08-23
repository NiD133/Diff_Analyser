package org.apache.commons.io.file.attribute;

import org.junit.Test;
import java.time.Instant;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Verifies that passing a null Instant to {@link FileTimes#toNtfsTime(Instant)}
     * results in a NullPointerException, which is the expected behavior for
     * invalid null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void toNtfsTimeWithNullInstantShouldThrowException() {
        // Call the method with a null argument to trigger the exception.
        FileTimes.toNtfsTime((Instant) null);
    }
}