package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalAccessor;

/**
 * This test class focuses on the factory method {@link DayOfYear#from(TemporalAccessor)}.
 *
 * Note: The original class name and scaffolding inheritance are preserved to show a direct
 * improvement on the provided code. In a real-world refactoring, these would also be renamed
 * for clarity (e.g., DayOfYearTest).
 */
public class DayOfYear_ESTestTest15 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that DayOfYear.from() throws a NullPointerException when passed a null argument,
     * as required by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void from_withNullTemporalAccessor_shouldThrowNullPointerException() {
        DayOfYear.from((TemporalAccessor) null);
    }
}