package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link InternationalFixedChronology} class.
 *
 * Note: The original test class name "InternationalFixedChronology_ESTestTest9"
 * and its scaffolding have been replaced with a standard naming convention for clarity.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void eraOf_shouldReturnCEEra_whenValueIs1() {
        // Arrange: The International Fixed Chronology has only one era, CE, represented by the value 1.
        // We use the recommended singleton instance for the chronology.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedEra expectedEra = InternationalFixedEra.CE;

        // Act: Call the method under test to get the era for the numeric value 1.
        InternationalFixedEra actualEra = chronology.eraOf(1);

        // Assert: Verify that the returned era is the correct Common Era (CE) instance.
        assertEquals(expectedEra, actualEra);
    }
}