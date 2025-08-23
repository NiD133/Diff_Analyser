package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    /**
     * Tests that getNano() correctly returns 0 when a TaiInstant is created
     * with a zero nanosecond component.
     */
    @Test
    public void getNano_whenCreatedWithZeroNanos_returnsZero() {
        // Arrange: Create a TaiInstant with a specific number of TAI seconds
        // and a zero nanosecond adjustment.
        TaiInstant instant = TaiInstant.ofTaiSeconds(1L, 0L);

        // Act: Retrieve the nanosecond component from the instant.
        int actualNanos = instant.getNano();

        // Assert: The retrieved nanosecond component should be zero.
        assertEquals(0, actualNanos);
    }
}