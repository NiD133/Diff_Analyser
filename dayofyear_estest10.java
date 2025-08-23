package org.threeten.extra;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.ZoneId;

/**
 * Test suite for the {@link DayOfYear} class.
 * This suite focuses on the factory method {@code now(ZoneId)}.
 */
public class DayOfYearTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that calling now() with a null ZoneId throws a NullPointerException.
     * The method contract for now(ZoneId) specifies that the zone parameter must not be null.
     */
    @Test
    public void now_withNullZoneId_throwsNullPointerException() {
        // Arrange: Configure the test to expect a NullPointerException.
        // This is the standard exception for null arguments that are contractually forbidden.
        thrown.expect(NullPointerException.class);

        // Act: Call the method under test with a null argument.
        DayOfYear.now((ZoneId) null);

        // Assert: The ExpectedException rule automatically verifies that the
        // expected exception was thrown. If no exception is thrown, the test fails.
    }
}