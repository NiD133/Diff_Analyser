package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Verifies that a UtcInstant created for the Modified Julian Day epoch (MJD 0)
     * is equal to an instance created by parsing its standard string representation.
     */
    @Test
    public void ofModifiedJulianDay_forEpoch_isEqualToParsedEpochString() {
        // Arrange
        // The Modified Julian Day epoch corresponds to 1858-11-17T00:00:00Z.
        UtcInstant instantFromMjd = UtcInstant.ofModifiedJulianDay(0L, 0L);
        UtcInstant instantFromParsedString = UtcInstant.parse("1858-11-17T00:00:00Z");

        // Assert
        // The two instances should represent the same point in time and thus be equal.
        assertEquals(instantFromMjd, instantFromParsedString);
    }
}