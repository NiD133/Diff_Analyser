package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.chrono.Era;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void eras_shouldReturnSingletonListContainingCommonEra() {
        // Arrange: The International Fixed Chronology is a singleton.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Act: Get the list of supported eras.
        List<Era> eras = chronology.eras();

        // Assert: The list should contain only one era, the Common Era (CE).
        assertNotNull("The list of eras should not be null", eras);
        assertEquals("There should be exactly one era", 1, eras.size());
        assertEquals("The single era should be the Common Era (CE)", InternationalFixedEra.CE, eras.get(0));
    }
}