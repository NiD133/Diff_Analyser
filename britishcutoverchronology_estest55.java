package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.ZoneId;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 * This test suite focuses on validating the behavior of the public API.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that dateNow(ZoneId) throws a NullPointerException when the provided ZoneId is null.
     * This is standard behavior for methods that do not accept null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void dateNow_withNullZoneId_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology, as recommended by its documentation.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act: Call the method with a null ZoneId.
        // The test will pass only if a NullPointerException is thrown.
        chronology.dateNow((ZoneId) null);
    }
}