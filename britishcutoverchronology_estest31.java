package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Verifies that creating a ZonedDateTime with a null Instant throws a NullPointerException.
     * <p>
     * The {@code Chronology.zonedDateTime(Instant, ZoneId)} contract requires that the
     * instant argument must not be null. This test ensures our implementation adheres to that contract.
     */
    @Test
    public void zonedDateTime_withNullInstant_throwsNullPointerException() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        ZoneId anyValidZone = ZoneOffset.UTC; // A non-null zone is required, but its specific value is irrelevant here.

        // Act & Assert
        // Use assertThrows to clearly state the expectation that a NullPointerException is thrown.
        // This is safer and more precise than @Test(expected=...) because it pinpoints
        // the exact line of code that is expected to throw the exception.
        assertThrows(NullPointerException.class, () -> {
            chronology.zonedDateTime(null, anyValidZone);
        });
    }
}