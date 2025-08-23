package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.format.ResolverStyle;
import java.time.temporal.TemporalField;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNull;

/**
 * Tests for {@link BritishCutoverChronology#resolveDate(Map, ResolverStyle)}.
 */
public class BritishCutoverChronologyResolveDateTest {

    @Test
    public void resolveDate_withEmptyFieldMap_returnsNull() {
        // Arrange
        // The contract for resolveDate requires returning null for an empty map,
        // regardless of the resolver style.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        Map<TemporalField, Long> emptyFieldValues = Collections.emptyMap();

        // Act
        BritishCutoverDate resolvedDate = chronology.resolveDate(emptyFieldValues, ResolverStyle.STRICT);

        // Assert
        assertNull("Resolving an empty map of fields should return null, as per the Chronology contract.", resolvedDate);
    }
}