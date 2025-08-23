package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.temporal.TemporalAccessor;

/**
 * This test case evaluates the behavior of the {@link Chronology#from(TemporalAccessor)} method
 * when used with a {@link DayOfMonth} instance.
 */
public class DayOfMonthUnderstandabilityTest {

    /**
     * Tests that {@link Chronology#from(TemporalAccessor)} correctly returns {@link IsoChronology#INSTANCE}
     * when passed a {@code DayOfMonth} object.
     * <p>
     * The {@code DayOfMonth} class is defined within the ISO-8601 calendar system. This test
     * confirms that querying its chronology correctly reflects this fact.
     */
    @Test
    public void chronologyFromDayOfMonthReturnsIsoChronology() {
        // Arrange: Create a DayOfMonth instance. The specific day (15) is arbitrary,
        // as any DayOfMonth is always part of the ISO calendar system.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Obtain the Chronology from the DayOfMonth instance.
        Chronology chronology = Chronology.from(dayOfMonth);

        // Assert: The obtained chronology should be the singleton instance of IsoChronology.
        assertEquals(IsoChronology.INSTANCE, chronology);
    }
}