package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class evaluates the behavior of the JulianChronology class.
 * Note: The original class name 'JulianChronology_ESTestTest12' was preserved,
 * but a more descriptive name like 'JulianChronologyTest' would be preferable in a real project.
 */
public class JulianChronology_ESTestTest12 {

    /**
     * Tests that creating a date with the AD era but a negative year-of-era
     * correctly resolves to a date in the BC era.
     *
     * <p>The Julian calendar system calculates the proleptic year from the era and year-of-era.
     * A negative year-of-era for AD results in a negative proleptic year, which
     * corresponds to the BC era. This test verifies that this edge case is handled correctly.
     */
    @Test
    public void date_withAdEraAndNegativeYearOfEra_resolvesToBcDate() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        int negativeYearOfEra = -2282;
        int month = 9;
        int day = 2;

        // Act: Create a date using the AD era with a negative year-of-era.
        JulianDate resultingDate = julianChronology.date(JulianEra.AD, negativeYearOfEra, month, day);

        // Assert: The chronology should correctly interpret this as a BC date.
        assertEquals("The era should be resolved to BC", JulianEra.BC, resultingDate.getEra());
    }
}