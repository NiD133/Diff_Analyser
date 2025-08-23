package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalQuery;

/**
 * Tests the behavior of the {@link DayOfMonth#query(TemporalQuery)} method.
 */
public class DayOfMonth_ESTestTest13 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that invoking query() with a null argument throws a NullPointerException,
     * as required by the TemporalAccessor contract.
     */
    @Test(expected = NullPointerException.class)
    public void query_withNullArgument_throwsNullPointerException() {
        // Arrange: Create a deterministic DayOfMonth instance.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act & Assert: Calling query with a null argument should throw a NullPointerException.
        // The cast is needed to resolve the generic method signature.
        dayOfMonth.query((TemporalQuery<Object>) null);
    }
}