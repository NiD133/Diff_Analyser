package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.format.ResolverStyle;
import java.time.temporal.TemporalField;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNull;

/**
 * Tests for the resolving behavior of {@link JulianChronology}.
 */
public class JulianChronologyResolveDateTest {

    /**
     * Tests that resolveDate returns null when provided with an empty map of fields,
     * as there is insufficient information to create a date.
     */
    @Test
    public void resolveDate_withEmptyFieldMap_shouldReturnNull() {
        // Arrange
        JulianChronology chronology = JulianChronology.INSTANCE;
        Map<TemporalField, Long> emptyFieldValues = Collections.emptyMap();

        // Act
        JulianDate resolvedDate = chronology.resolveDate(emptyFieldValues, ResolverStyle.STRICT);

        // Assert
        assertNull("Resolving a date with no field values should result in null.", resolvedDate);
    }
}