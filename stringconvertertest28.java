package org.joda.time.convert;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for the {@link StringConverter} class, focusing on its
 * ability to identify readable intervals.
 */
public class StringConverterTest {

    /**
     * Verifies that isReadableInterval() correctly identifies an empty string
     * as a non-valid interval representation.
     */
    @Test
    public void isReadableInterval_shouldReturnFalse_forEmptyString() {
        // An empty string cannot be parsed into an interval.
        // The method should return false, regardless of the provided Chronology.
        assertFalse("An empty string should not be considered a readable interval",
                StringConverter.INSTANCE.isReadableInterval("", null));
    }
}