package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * This class is a refactored version of a single test case from an auto-generated suite.
 * The original class name and inheritance are preserved for context.
 */
public class DayOfYear_ESTestTest39 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that DayOfYear.from() correctly creates a DayOfYear instance
     * when the input TemporalAccessor is another DayOfYear.
     */
    @Test
    public void from_whenGivenDayOfYearInstance_createsEquivalentInstance() {
        // Arrange: Create a source DayOfYear instance.
        // Using DayOfYear.of() is more explicit and deterministic than relying on a mocked DayOfYear.now().
        DayOfYear sourceDayOfYear = DayOfYear.of(45);

        // Act: Create a new DayOfYear from the source instance.
        DayOfYear resultDayOfYear = DayOfYear.from(sourceDayOfYear);

        // Assert: The new instance should be equal to the source,
        // confirming that the value was extracted correctly.
        assertEquals(sourceDayOfYear, resultDayOfYear);
    }
}