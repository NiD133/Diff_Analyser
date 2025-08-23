package org.threeten.extra.chrono;

import static org.junit.Assert.assertThrows;

import java.time.Instant;
import java.time.ZoneId;
import org.junit.Test;

/**
 * Unit tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that creating a ZonedDateTime with a null Instant throws a NullPointerException.
     * The ZoneId must also be non-null, but this test focuses on the Instant parameter.
     */
    @Test
    public void zonedDateTime_whenInstantIsNull_throwsNullPointerException() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ZoneId zone = ZoneId.systemDefault();
        Instant nullInstant = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            chronology.zonedDateTime(nullInstant, zone);
        });
    }
}