package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * The Modified Julian Day for the Java epoch (1970-01-01).
     * The MJD epoch starts on 1858-11-17.
     */
    private static final long MJD_OF_JAVA_EPOCH = 40587L;

    /**
     * Tests that creating a UtcInstant from the standard Java epoch
     * results in the correct Modified Julian Day and nano-of-day values.
     */
    @Test
    public void createFromEpochInstant_shouldHaveCorrectMjdAndNanoOfDay() {
        // Arrange: The Java epoch instant (1970-01-01T00:00:00Z).
        Instant epochInstant = Instant.EPOCH;

        // Act: Create a UtcInstant from the epoch instant.
        UtcInstant utcInstant = UtcInstant.of(epochInstant);

        // Assert: Verify the internal state is correctly calculated.
        assertEquals(
            "Nano-of-day should be zero at the start of the epoch day",
            0L,
            utcInstant.getNanoOfDay());
        assertEquals(
            "Modified Julian Day should correspond to the Java epoch date",
            MJD_OF_JAVA_EPOCH,
            utcInstant.getModifiedJulianDay());
    }
}