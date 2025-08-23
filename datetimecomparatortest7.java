package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DateTimeComparator}, focusing on its handling of null inputs.
 * In DateTimeComparator, a null input is treated as the current time ("now").
 */
public class DateTimeComparatorNullInputTest {

    /**
     * Tests that comparing two null inputs consistently returns 0.
     * This is critical for preventing race conditions where the system clock might
     * tick between the evaluation of the two "now" instances, which could otherwise
     * lead to an unexpected non-zero result.
     *
     * This test repeatedly performs the comparison to increase the chance of
     * detecting such a race condition if one were to exist.
     * (Related to Joda-Time issue #404).
     */
    @Test
    public void compare_nullWithNull_shouldConsistentlyReturnZero() {
        DateTimeComparator comparator = DateTimeComparator.getInstance();

        // Loop many times to increase the likelihood of catching a race condition
        // if the implementation were faulty (e.g., calling DateTimeUtils.currentTimeMillis() twice).
        for (int i = 0; i < 10000; i++) {
            int result = comparator.compare(null, null);
            assertEquals("Comparing two nulls (i.e., now vs. now) must always result in 0.", 0, result);
        }
    }
}