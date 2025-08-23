package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that a round-trip conversion from TaiInstant to UtcInstant and back
     * results in the original TaiInstant value, ensuring the conversion is lossless.
     */
    @Test
    public void testRoundTripConversionFromTaiInstantIsLossless() {
        // Arrange: Create an original TAI instant.
        TaiInstant originalTaiInstant = TaiInstant.ofTaiSeconds(3217L, 1000L);

        // Act: Convert the TAI instant to a UTC instant and then convert it back.
        UtcInstant intermediateUtcInstant = originalTaiInstant.toUtcInstant();
        TaiInstant resultTaiInstant = intermediateUtcInstant.toTaiInstant();

        // Assert: The instant after the round-trip conversion should be equal to the original.
        assertEquals(originalTaiInstant, resultTaiInstant);
    }
}