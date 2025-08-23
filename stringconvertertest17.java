package org.joda.time.convert;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the getDurationMillis method in {@link StringConverter}.
 * <p>
 * This suite focuses on verifying the parsing of ISO 8601 duration strings
 * that contain seconds and fractional seconds (e.g., "PT12.345S").
 */
public class StringConverterGetDurationMillisTest {

    private final StringConverter converter = StringConverter.INSTANCE;

    @Test
    public void getDurationMillis_shouldParsePositiveDurations() {
        // Standard format with 3 decimal places
        assertEquals(12345L, converter.getDurationMillis("PT12.345S"));
        
        // Lowercase format should also be accepted
        assertEquals(12345L, converter.getDurationMillis("pt12.345s"));
        
        // Whole seconds without a decimal part
        assertEquals(12000L, converter.getDurationMillis("pt12s"));
        
        // Whole seconds with a trailing decimal point
        assertEquals(12000L, converter.getDurationMillis("pt12.s"));
    }

    @Test
    public void getDurationMillis_shouldParseNegativeDurations() {
        assertEquals(-12320L, converter.getDurationMillis("pt-12.32s"));
        assertEquals(-320L, converter.getDurationMillis("pt-0.32s"));
    }

    @Test
    public void getDurationMillis_shouldParseZeroDuration() {
        assertEquals(0L, converter.getDurationMillis("pt-0.0s"));
        assertEquals(0L, converter.getDurationMillis("pt0.0s"));
    }

    @Test
    public void getDurationMillis_shouldTruncateSubMillisecondPrecision() {
        // The parser should ignore digits beyond the third decimal place (milliseconds)
        assertEquals(12345L, converter.getDurationMillis("pt12.3456s"));
    }
}