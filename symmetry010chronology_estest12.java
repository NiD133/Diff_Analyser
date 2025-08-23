package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Symmetry010Chronology}.
 * This class replaces the auto-generated test suite for better readability.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void eraOf_returnsBceForValueZero() {
        // Arrange: The Symmetry010Chronology uses ISO eras, where 0 represents BCE.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int bceEraValue = 0;

        // Act: Call the method under test.
        IsoEra actualEra = chronology.eraOf(bceEraValue);

        // Assert: Verify that the returned era is BCE.
        assertEquals("The era for value 0 should be BCE", IsoEra.BCE, actualEra);
    }
}